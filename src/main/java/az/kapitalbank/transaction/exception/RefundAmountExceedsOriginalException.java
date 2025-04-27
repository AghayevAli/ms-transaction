package az.kapitalbank.transaction.exception;

import static az.kapitalbank.transaction.exception.constant.ErrorCode.TOTAL_REFUND_EXCEEDS;

import java.text.MessageFormat;

public final class RefundAmountExceedsOriginalException extends CommonException {

    private RefundAmountExceedsOriginalException(String message) {
        super(TOTAL_REFUND_EXCEEDS, message);
    }

    public static RefundAmountExceedsOriginalException of(String message, Object... args) {
        return new RefundAmountExceedsOriginalException(MessageFormat.format(message, args));
    }
}
