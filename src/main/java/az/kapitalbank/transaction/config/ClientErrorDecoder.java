package az.kapitalbank.transaction.config;

import az.kapitalbank.transaction.exception.ClientException;
import az.kapitalbank.transaction.exception.model.CommonErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.InputStream;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ClientErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder;
    private final ObjectMapper objectMapper;

    public ClientErrorDecoder(ObjectMapper objectMapper) {
        this.defaultErrorDecoder = new Default();
        this.objectMapper = objectMapper;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        try (InputStream bodyIs = response.body().asInputStream()) {
            CommonErrorResponse error = objectMapper.readValue(bodyIs, CommonErrorResponse.class);
            String message = String.format("[%s] %s", methodKey, error.getMessage());
            return new ClientException(response.status(), error.getErrorCode(), message);
        } catch (Exception ex) {
            log.error("Exception occurs while parsing client error response: {}", response, ex);
            return defaultErrorDecoder.decode(methodKey, response);
        }
    }

}
