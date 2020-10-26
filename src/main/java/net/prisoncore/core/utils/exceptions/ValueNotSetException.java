package net.prisoncore.core.utils.exceptions;

import javax.annotation.Nonnull;

public class ValueNotSetException extends RuntimeException {

    public ValueNotSetException(@Nonnull final String message) {
        super(message);
    }
}
