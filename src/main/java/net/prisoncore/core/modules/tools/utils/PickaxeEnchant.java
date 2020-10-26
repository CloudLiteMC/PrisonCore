package net.prisoncore.core.modules.tools.utils;

import javax.annotation.Nonnull;

public enum PickaxeEnchant {
    TOKEN_FINDER("TOKEN_FINDER"),
    NIGHT_VISION("NIGHT_VISION"),
    SPEED("SPEED"),
    JUMP("JUMP"),
    HASTE("HASTE");

    private final String enchantName;

    PickaxeEnchant(@Nonnull final String enchantName) {
        this.enchantName = enchantName;
    }

    public String getEnchantName() {
        return this.enchantName;
    }
}
