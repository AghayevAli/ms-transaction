package az.kapitalbank.transaction.exception.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonErrorResponse {

    private String errorCode;
    private String message;

    public CommonErrorResponse(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

}
