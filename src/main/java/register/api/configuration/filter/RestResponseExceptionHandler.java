package register.api.configuration.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import register.api.model.RestResponse;
import register.exception.UserException;
import register.exception.UserExceptionStatus;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler({UserException.class})
    public ResponseEntity<?> userExceptionHandler(HttpServletRequest request, UserException exception) throws JsonProcessingException {

        UserExceptionStatus status = exception.getStatus();
        return ResponseEntity
                .status(status.getCode())
                .body(new RestResponse<>(
                        status.getCode(),
                        status.getReasonPhrase(),
                        (String) request.getAttribute("userUri"),
                        exception.getMessage()
                ));
    }


    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<?> runtimeExceptionHandler(HttpServletRequest request, RuntimeException exception) throws JsonProcessingException {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String userUri = (String) request.getAttribute("userUri");
        String referenceId = (String) request.getAttribute("referenceId");

        log.error("5xx exception with uri: " + userUri + " ,reff: " + referenceId, exception);

        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new RestResponse<>(
                        status.value(),
                        status.getReasonPhrase(),
                        userUri,
                        "مشکلی در پردازش به وجود آمده است."
                ));
    }


}