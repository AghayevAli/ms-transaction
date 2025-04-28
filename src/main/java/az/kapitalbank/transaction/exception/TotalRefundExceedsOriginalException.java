package az.kapitalbank.transaction.exception;

import static az.kapitalbank.transaction.exception.constant.ErrorCode.REFUND_AMOUNT_EXCEEDS;

import java.text.MessageFormat;

public final class TotalRefundExceedsOriginalException extends CommonException {

    private TotalRefundExceedsOriginalException(String message) {
        super(REFUND_AMOUNT_EXCEEDS, message);
    }

    public static TotalRefundExceedsOriginalException of(String message, Object... args) {
        return new TotalRefundExceedsOriginalException(MessageFormat.format(message, args));
    }
}
