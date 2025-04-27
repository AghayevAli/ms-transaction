package az.kapitalbank.transaction.exception;

import static az.kapitalbank.transaction.exception.constant.ErrorCode.INVALID_ORIGINAL_TRANSACTION;

import java.text.MessageFormat;

public final class TotalRefundExceedsOriginalException extends CommonException {

    private TotalRefundExceedsOriginalException(String message) {
        super(INVALID_ORIGINAL_TRANSACTION, message);
    }

    public static TotalRefundExceedsOriginalException of(String message, Object... args) {
        return new TotalRefundExceedsOriginalException(MessageFormat.format(message, args));
    }
}
