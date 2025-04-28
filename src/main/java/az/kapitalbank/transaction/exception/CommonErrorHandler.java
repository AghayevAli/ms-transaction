package az.kapitalbank.transaction.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import az.kapitalbank.transaction.exception.model.CommonErrorResponse;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class CommonErrorHandler {

    @ExceptionHandler({ClientException.class})
    public ResponseEntity<CommonErrorResponse> handleClientException(ClientException ex) {
        HttpStatus httpStatus = this.httpStatus(ex.getStatus(), HttpStatus.INTERNAL_SERVER_ERROR);
        addErrorLog(ex.getErrorCode(), ex.getMessage());
        CommonErrorResponse errorResponse = new CommonErrorResponse(ex.getErrorCode(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(InvalidOriginalTransactionException.class)
    public CommonErrorResponse handleInvalidOriginalTransaction(InvalidOriginalTransactionException ex) {
        addErrorLog(ex.getErrorCode(), ex.getMessage());
        return new CommonErrorResponse(
                ex.getErrorCode(),
                ex.getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(RefundAmountExceedsOriginalException.class)
    public CommonErrorResponse handleRefundAmoundExceedsOriginal(RefundAmountExceedsOriginalException ex) {
        addErrorLog(ex.getErrorCode(), ex.getMessage());
        return new CommonErrorResponse(
                ex.getErrorCode(),
                ex.getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(TotalRefundExceedsOriginalException.class)
    public CommonErrorResponse handleTotalRefundExceedsOriginal(TotalRefundExceedsOriginalException ex) {
        addErrorLog(ex.getErrorCode(), ex.getMessage());
        return new CommonErrorResponse(
                ex.getErrorCode(),
                ex.getMessage());
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(TransactionNotFoundException.class)
    public CommonErrorResponse handleTransactionNotFound(TransactionNotFoundException ex) {
        addErrorLog(ex.getErrorCode(), ex.getMessage());
        return new CommonErrorResponse(
                ex.getErrorCode(),
                ex.getMessage());
    }

    protected HttpStatus httpStatus(int httpStatus, HttpStatus defaultHttpStatus) {
        return Optional.ofNullable(HttpStatus.resolve(httpStatus)).orElse(defaultHttpStatus);
    }

    private static void addErrorLog(String errorCode, String errorMessage) {
        log.error("Error Code: {}, Error Message: {}", errorCode, errorMessage);
    }
}
