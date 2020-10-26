package net.prisoncore.core.utils.exceptions;

import net.prisoncore.core.modules.ModuleType;

import javax.annotation.Nonnull;

public class ModuleNotFoundException extends RuntimeException {

    public ModuleNotFoundException(@Nonnull final ModuleType type) {
        super("Module could not be found for: " + type.name() + ". (Is it loaded?!)");
    }
}
