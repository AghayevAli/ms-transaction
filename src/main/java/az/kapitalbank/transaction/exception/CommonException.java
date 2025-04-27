package az.kapitalbank.transaction.exception;

import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {

    private final String errorCode;
    private final String message;

    protected CommonException(String errorCode, String message) {
        super(errorCode);
        this.errorCode = errorCode;
        this.message = message;
    }

}
