package register.data.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import register.data.entity.User;
import register.data.entity.UserRole;
import register.data.repository.UserRepository;

import javax.annotation.PostConstruct;

@Configuration
public class SystemAdminConfiguration {

    @Value("${admin.username}")
    private String ADMIN_USERNAME;
    @Value("${admin.password}")
    private String ADMIN_PASSWORD;
    @Value("${admin.email}")
    private String ADMIN_EMAIL;

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void setAdmin() {
        User admin = userRepository.getUserByUsername(ADMIN_USERNAME);
        if (admin == null) {
            userRepository.registerUser(ADMIN_USERNAME, null, ADMIN_EMAIL, ADMIN_PASSWORD, null, null, null, UserRole.ADMIN);
        }
    }

}
