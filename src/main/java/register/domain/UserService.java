package register.domain;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import register.api.configuration.JwtTokenUtil;
import register.api.model.Login;
import register.data.entity.User;
import register.data.entity.UserRole;
import register.data.repository.UserRepository;
import register.exception.UserException;
import register.exception.UserExceptionStatus;
import register.utils.UserUtil;

import java.util.List;


@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private UserUtil userUtil;

    public String login(Login login) throws UserException {

        User user = userRepository.getUserByUsername(login.getUsername());
        if (user == null) {
            throw new UserException(UserExceptionStatus.USERNAME_NOT_FOUND);
        }
        if (!user.getPassword().equals(DigestUtils.md5Hex(login.getPassword()))) {
            throw new UserException(UserExceptionStatus.INVALID_PASSWORD);
        }
        return jwtTokenUtil.generateToken(jwtUserDetailsService.generateUserDetails(user));
    }

    @Transactional
    public User registerUser(String username, String name, String email, String password, Long number, String address, String introducer) throws UserException {

        if (!checkUniqueData("username", username)) {
            throw new UserException(UserExceptionStatus.INVALID_UNIQUE_USERNAME);
        }

        if (!checkUniqueData("email", email)) {
            throw new UserException(UserExceptionStatus.INVALID_UNIQUE_EMAIL);
        }

        User introducerUser = null;
        if (introducer != null && !introducer.isEmpty()) {
            introducerUser = userRepository.getUserByUsername(introducer);

            if (introducerUser == null) {
                throw new UserException(UserExceptionStatus.INTRODUCER_NOT_FOUND);
            }
        }

        return userRepository.registerUser(username, name, email, password, number, address, introducerUser, UserRole.USER);
    }

    @Transactional
    public User getMe() throws UserException {
        User user = userUtil.getCredential();
        if (user == null) {
            throw new UserException(UserExceptionStatus.UNAUTHORIZED);
        }
        return user;
    }

    @Transactional
    public void changeRole(String username, UserRole role) throws UserException {
        User user = userRepository.getUserByUsername(username);

        if (role.equals(UserRole.SYS_ADMIN) || user.getRole().equals(UserRole.SYS_ADMIN)) {
            throw new UserException(UserExceptionStatus.INVALID_ROLE_CHANGE);
        }

        if (user == null) {
            throw new UserException(UserExceptionStatus.USERNAME_NOT_FOUND);
        }
        user.setRole(role);
        userRepository.updateUser(user);
    }

    @Transactional
    public List<User> searchUser(String name, String username, String email, String introducer, Long number, String address) throws UserException {
        User user = userUtil.getCredential();
        if (user.getRole().equals(UserRole.USER)) {
            if (introducer != null && !user.getUsername().equals(introducer)) {
                throw new UserException(UserExceptionStatus.INTRODUCER_PERMISSION_DENIED);
            }

            return userRepository.search(user, name, username, email, user.getUsername(), number, address);
        }

        return userRepository.search(user, name, username, email, introducer, number, address);
    }

    @Transactional
    public boolean checkUniqueData(String column, String value) {
        User user = userRepository.checkUniqueData(column, value);
        if (user != null) {
            return false;
        }
        return true;
    }

    @Transactional
    public List<User> getUserByIntroducer(String introducer) throws UserException {

        User user = userUtil.getCredential();

        User introducerUser = userRepository.getUserByUsername(introducer);
        if (introducerUser == null) {
            throw new UserException(UserExceptionStatus.INTRODUCER_NOT_FOUND);
        }

        if (user.getRole().equals(UserRole.USER)) {
            if (introducer != null && !user.getUsername().equals(introducer)) {
                throw new UserException(UserExceptionStatus.INTRODUCER_PERMISSION_DENIED);
            }
        }

        List<User> users = userRepository.getUserByIntroducer(introducerUser);

        return users;
    }
}
