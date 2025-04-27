package az.kapitalbank.transaction.exception;

import static az.kapitalbank.transaction.exception.constant.ErrorCode.INVALID_ORIGINAL_TRANSACTION;

import java.text.MessageFormat;

public final class InvalidOriginalTransactionException extends CommonException {

    private InvalidOriginalTransactionException(String message) {
        super(INVALID_ORIGINAL_TRANSACTION, message);
    }

    public static InvalidOriginalTransactionException of(String message, Object... args) {
        return new InvalidOriginalTransactionException(MessageFormat.format(message, args));
    }
}
