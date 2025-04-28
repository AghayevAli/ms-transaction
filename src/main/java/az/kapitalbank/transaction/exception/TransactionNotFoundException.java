package az.kapitalbank.transaction.exception;

import static az.kapitalbank.transaction.exception.constant.ErrorCode.TRANSACTION_NOT_FOUND;

import java.text.MessageFormat;

public final class TransactionNotFoundException extends CommonException {

    private TransactionNotFoundException(String message) {
        super(TRANSACTION_NOT_FOUND, message);
    }

    public static TransactionNotFoundException of(String message, Object... args) {
        return new TransactionNotFoundException(MessageFormat.format(message, args));
    }
}
