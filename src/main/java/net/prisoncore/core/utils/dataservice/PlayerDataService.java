package net.prisoncore.core.utils.dataservice;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.Module;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import java.util.*;

import static net.prisoncore.core.utils.Utils.textOf;

public final class PlayerDataService extends Module implements Listener {

    private final PrisonCore core;
    private MongoCollection<Document> usersCollection;
    private BukkitTask runningTask;
    private HashMap<UUID, User> cachedPlayers;

    public PlayerDataService(final PrisonCore core) {
        this.core = core;
    }

    @Override
    public Module init() {
        this.core.getLog().log("&3Initializing PlayerDataService. . .");
        this.core.registerEvent(this);
        this.cachedPlayers = new HashMap<>();
        final MongoClientURI uri = new MongoClientURI(
                "mongodb://admin:Kittens10%40@51.81.85.35");
        MongoClient mongoClient = new MongoClient(uri);
        Bukkit.getLogger().info(textOf("&aSuccessfully connected to MongoDB Database!"));
        this.usersCollection = mongoClient.getDatabase("UserData").getCollection("Data");
        this.runningTask = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getLogger().info(textOf("Saving player data to MongoDB. . ."));
                savePlayerData();
                Bukkit.getLogger().info(textOf("Player data has been successfully updated!"));
            }
        }.runTaskTimerAsynchronously(this.core, 0, 20 * 30);
        this.core.getLog().log("&3PlayerDataService initialized!");
        return this;
    }

    @Override
    public void reload() {
        this.runningTask.cancel();
        this.savePlayerData();
    }

    /**
     * Checks if a user exists in the database, this is a live non-cached database query
     * @param userUuid User UUID to check
     * @return true if user exists
     */
    public final boolean exists(@Nonnull final UUID userUuid) {
        return this.getUserDocument(userUuid) != null;
    }

    /**
     * Ensures a user exists by using PlayerDataService#exists, and then creates it against a false result
     * @param uuid UUID Of User
     * @param name Name of User
     */
    public void ensureUserExists(@Nonnull final UUID uuid, @Nonnull final String name) {
        if (!this.exists(uuid)) this.createUser(uuid, name);
    }

    /**
     * Checks the result of a AsyncPlayerPreLoginEvent
     * @param result Result of login
     * @return true if player did connect
     */
    private boolean didDisconnect(final AsyncPlayerPreLoginEvent.Result result) {
        return result == AsyncPlayerPreLoginEvent.Result.KICK_BANNED || result == AsyncPlayerPreLoginEvent.Result.KICK_FULL
                || result == AsyncPlayerPreLoginEvent.Result.KICK_OTHER || result == AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST;
    }

    /**
     * Saves all user data in the cached players map, call this method async it may cause lag on main thread
     */
    public void savePlayerData() {
        this.cachedPlayers.forEach((uuid, user) -> saveUser(uuid, user.getDocument()));
    }

    /**
     * Gets a user from the cache OR database
     * if a user is not currently loaded into cache, then they will be retrieved from the database and loaded into cache
     * this will effectivly add them into the PrisonCore's asynchornus saving loop
     *
     * @param uuid UUID Of the player to get
     * @return User of the UUID
     */
    public User getUser(@Nonnull final UUID uuid) {
        User user = this.cachedPlayers.get(uuid);
        if (user == null) {
            final Document obj = this.usersCollection.find(new Document("uuid", uuid.toString())).first();
            user = new User(obj, this.core);
            this.addPlayerToCache(uuid, user);
        }
        return user;
    }

    private Document getUserDocument(@Nonnull final UUID uuid) {
        return this.usersCollection.find(new Document("uuid", uuid.toString())).first();
    }

    public void addPlayerToCache(@Nonnull final UUID playerUuid, @Nonnull final User playerUser) {
        this.cachedPlayers.putIfAbsent(playerUuid, playerUser);
    }

    private void createUser(@Nonnull final UUID uuid, @Nonnull final String name) {
        final Document document = new Document();
        document.append("uuid", uuid.toString());
        document.append("balance", 100D);
        document.append("tokens", 10000D);
        document.append("autoSellMultiplier", 1.0D);
        document.append("homes", new ArrayList<>());

        final Document pickaxe = new Document();
        pickaxe.append("name", "&e" + name + "'s Pickaxe");
        pickaxe.append("lore", new ArrayList<>());
        pickaxe.append("level", 1);
        pickaxe.append("currentExp", 0D);
        final List<String> enchantments = new ArrayList<>();
        enchantments.add("unbreaking:10");
        enchantments.add("efficiency:10");
        enchantments.add("fortune:5");
        final List<String> customEnchantments = new ArrayList<>();
        customEnchantments.add("TOKEN_FINDER:1");
        pickaxe.append("enchantments", enchantments);
        pickaxe.append("customEnchants", customEnchantments);
        pickaxe.append("material", "WOODEN_PICKAXE");
        document.append("pickaxe", pickaxe);

        final Document prisonMmo = new Document();
        final Document playerMmoObject = new Document();
        final Document miningMmoObject = new Document();

        playerMmoObject.append("level", 1);
        playerMmoObject.append("currentExp", 0D);
        prisonMmo.append("player", playerMmoObject);

        miningMmoObject.append("level", 1);
        miningMmoObject.append("currentExp", 0D);
        prisonMmo.append("mining", miningMmoObject);

        document.append("prisonMmo", prisonMmo);
        this.usersCollection.insertOne(document);
        new User(document, this.core);
    }

    public void saveUser(@Nonnull final UUID playerUuid, @Nonnull final Document playerDocument) {
        final Document setDocument = new Document("$set", playerDocument);
        setDocument.remove("_id");
        this.usersCollection.updateOne(new Document("_id", playerDocument.get("_id")), setDocument);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(AsyncPlayerPreLoginEvent e) {
        if (!this.didDisconnect(e.getLoginResult())) this.ensureUserExists(e.getUniqueId(), e.getName());
    }
}
