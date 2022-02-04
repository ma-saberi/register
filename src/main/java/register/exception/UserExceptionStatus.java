package register.exception;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;


@AllArgsConstructor
public enum UserExceptionStatus {

    //region 404 Not Found: The server can not find the requested resource.
    INTRODUCER_NOT_FOUND(404, "Not found introducer user", "کاربر معرفی کننده یافت نشد"),
    USERNAME_NOT_FOUND(404, "Not found user with this username", "کاربری با نام کاربری موردنظر یافت نشد"),
    EMAIL_NOT_FOUND(404, "Not found user with this email", "کاربری با ایمیل موردنظر یافت نشد"),

    //endregion

    //region 401 Unauthorized

    INVALID_PASSWORD(401, "Invalid password", "رمز وارد شده معتبر نمی باشد."),
    INVALID_AUTHORIZATION_TYPE(401, "Invalid authorization type", "احراز هویت باید از نوع Bearer باشد"),
    UNAUTHORIZED(401, "Unauthorized", "درخواست دسترسی معتبر نیست."),

    //endregion


    //region 400 Bad Request: The server could not understand the request due to invalid syntax.

    //region Invalids data
    // https://stackoverflow.com/questions/6123425/rest-response-code-for-invalid-data
    INVALID_REQUEST(400, "Invalid request", "درخواست معتبر نمی‌باشد."),
    //endregion
    ;

    private final int code;
    private final String reasonPhrase;
    private final String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getReasonPhrase(Pair<String, String>... placeHolders) {
        String res = reasonPhrase;
        for (Pair<String, String> placeHolder : placeHolders) {
            res = res.replace(placeHolder.getKey(), placeHolder.getValue());
        }
        return res;
    }

    public String getMessage(Pair<String, String>... placeHolders) {
        String msg = message;
        for (Pair<String, String> placeHolder : placeHolders) {
            msg = msg.replace(placeHolder.getKey(), placeHolder.getValue());
        }
        return msg;
    }

}
