package net.prisoncore.core.modules;

public abstract class Module {

    /**
     * Gets called when a module is first loaded
     */
    public abstract Module init();

    /**
     * Gets called when a module is first loaded
     */
    public abstract void reload();
}
