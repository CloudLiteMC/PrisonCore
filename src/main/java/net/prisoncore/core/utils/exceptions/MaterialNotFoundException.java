package net.prisoncore.core.utils.exceptions;

import javax.annotation.Nonnull;

public final class MaterialNotFoundException extends RuntimeException {

    public MaterialNotFoundException(@Nonnull final String message) {
        super(message);
    }
}
