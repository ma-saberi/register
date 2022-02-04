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
                context.getAuthentication() != null;
    }

    public User getCredential() {

        if (!isAuthenticated()) {
            return null;
        }
        SecurityContext context = SecurityContextHolder.getContext();
        return (User) context.getAuthentication().getCredentials();

    }

}
