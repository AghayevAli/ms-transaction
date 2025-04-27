package az.kapitalbank.transaction.exception;

import lombok.Getter;

@Getter
public class ClientException extends RuntimeException {

    private final int status;
    private final String errorCode;
    private final String message;

    public ClientException(int status, String errorCode, String message) {
        super(errorCode);
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
    }

}
