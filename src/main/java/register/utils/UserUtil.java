package register.utils;


import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import register.data.entity.User;

@Component
public class UserUtil {

    public boolean isAuthenticated() {
        SecurityContext context = SecurityContextHolder.getContext();
        return context != null &&
                context.getAuthentication() != null &&
                context.getAuthentication().getPrincipal() != null &&
                !context.getAuthentication().getPrincipal().equals("_ANONYMOUS_");
    }

    public User getCredential() {

        if (!isAuthenticated()) {
            return null;
        }
        SecurityContext context = SecurityContextHolder.getContext();
        return (User) context.getAuthentication().getCredentials();

    }

    public boolean phoneIsValid(String mobileNumber) {
        String regex1 = "^(\\+98|0|0098|98)?9\\d{9}$";
        String regex2 = "09000000000";
        String regex3 = "09653214569";

        return mobileNumber.matches(regex1) && !mobileNumber.matches(regex2) && !mobileNumber.matches(regex3);
    }

    public boolean emailIsValid(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        return email.matches(regex);
    }
}
