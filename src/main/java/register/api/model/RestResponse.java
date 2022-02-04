package register.api.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Setter
@Getter
public class RestResponse<T> implements RegisterApiModel {

    private Integer status;
    private String error;
    private String message;
    private String path;
    private Date timestamp;
    private T result;
    private String reference;


    @Builder(builderMethodName = "Builder")
    public RestResponse(HttpStatus status, String error, String path, String message, T result, String reference, Date timestamp) {
        this.status = status.value();
        this.error = error;
        this.path = path;
        this.message = message;
        this.result = result;
        this.reference = reference;
        this.timestamp = timestamp;
    }

    @Builder(builderMethodName = "sBuilder")
    public RestResponse(int status, String error, String path, String message) {
        this.status = status;
        this.error = error;
        this.path = path;
        this.message = message;
        this.result = null;
    }

    @Builder(builderMethodName = "kBuilder")
    public RestResponse(int status, String error, String path, Date timestamp, String message) {
        this.status = status;
        this.error = error;
        this.path = path;
        this.result = null;
        this.timestamp = timestamp;
        this.message = message;
    }

}