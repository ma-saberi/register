package register.exception;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;


@AllArgsConstructor
public enum UserExceptionStatus {

    //region 404 Not Found: The server can not find the requested resource.
    INTRODUCER_NOT_FOUND(404, "Not found introducer user.", "کاربر معرفی کننده یافت نشد"),
    USERNAME_NOT_FOUND(404, "Not found user with this username.", "کاربری با نام کاربری موردنظر یافت نشد"),
    EMAIL_NOT_FOUND(404, "Not found user with this email.", "کاربری با ایمیل موردنظر یافت نشد"),

    //endregion

    //region 401 Unauthorized

    INVALID_PASSWORD(401, "Invalid password", "رمز وارد شده معتبر نمی باشد."),
    UNAUTHORIZED(401, "Unauthorized", "درخواست دسترسی معتبر نیست."),

    //endregion

    //region 403 Forbidden

    INTRODUCER_PERMISSION_DENIED(403, "Introducer permission denied.", "به معرفی کننده موردنظر دسترسی ندارید"),

    //endregion

    //region 400 Bad request

    INVALID_ROLE_CHANGE(400, "Invalid role change.", "تغییر نقش به /از مدیر سیستم معتبر نمی باشد"),
    INVALID_UNIQUE_USERNAME(400, "Username isn't unique.", "نام کاربری وارد شده یکتا نمی باشد."),
    INVALID_UNIQUE_EMAIL(400, "Email isn't unique.", "ایمیل وارد شده یکتا نمی باشد."),

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
