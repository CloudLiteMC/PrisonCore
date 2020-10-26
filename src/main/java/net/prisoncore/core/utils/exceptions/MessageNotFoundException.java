package net.prisoncore.core.utils.exceptions;

import javax.annotation.Nonnull;

public class MessageNotFoundException extends RuntimeException {

    public MessageNotFoundException(@Nonnull final String message) {
        super(message);
    }
}
