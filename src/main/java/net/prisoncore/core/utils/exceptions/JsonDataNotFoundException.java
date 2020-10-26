package net.prisoncore.core.utils.exceptions;

import javax.annotation.Nonnull;

public class JsonDataNotFoundException extends RuntimeException {

    public JsonDataNotFoundException(@Nonnull final String message) {
        super(message);
    }
}
