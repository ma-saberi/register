package register.exception;

import com.fasterxml.jackson.databind.JsonMappingException;

public class JsonProcessException extends UserRuntimeException {

    private String message;


    public JsonProcessException(String message) {
        this.message = message;
    }

    public JsonProcessException(Exception e, String simpleClassName) {
        String eMessage = e.getMessage();
        if (e instanceof JsonMappingException && eMessage.startsWith("Can not deserialize instance")) {
            message = "Invalid json format - ";
            int index = eMessage.indexOf("line:");
            message += eMessage.substring(index, eMessage.indexOf("]", index));
            message += " - " + simpleClassName;

        } else {
            message = "Invalid json format";
        }
        super.initCause(e);
    }

    public JsonProcessException(Exception e) {
        message = "Invalid json format";
        super.initCause(e);
    }

    public String getMessage() {
        return this.message;
    }
}
