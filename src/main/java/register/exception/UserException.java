package register.exception;

public class UserException extends Exception {

    protected UserExceptionStatus status;
    protected String message;

    public UserException(UserExceptionStatus status) {
        this.status = status;
        this.message = status.getMessage();
    }

    @Deprecated
    public UserException(UserExceptionStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public UserException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public UserExceptionStatus getStatus() {
        return status;
    }

}
