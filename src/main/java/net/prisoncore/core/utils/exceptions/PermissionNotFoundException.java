package net.prisoncore.core.utils.exceptions;

import javax.annotation.Nonnull;

public final class PermissionNotFoundException extends RuntimeException {

    public PermissionNotFoundException(@Nonnull final String message) {
        super(message);
    }
}
