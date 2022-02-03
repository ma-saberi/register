package register.domain;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import register.api.configuration.JwtTokenUtil;
import register.api.configuration.JwtUserDetailsService;
import register.data.entity.User;
import register.data.entity.UserRole;
import register.data.repository.UserRepository;
import register.exception.UserException;
import register.exception.UserExceptionStatus;


@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    public String login(String username, String password) throws UserException {
        User user = userRepository.getUserByUsername(username);
        if (user == null) {
            throw new UserException(UserExceptionStatus.USERNAME_NOT_FOUND);
        }
        if (!user.getPassword().equals(DigestUtils.md5Hex(password))) {
            throw new UserException(UserExceptionStatus.INVALID_PASSWORD);
        }
        return jwtTokenUtil.generateToken(jwtUserDetailsService.generateUserDetails(user));
    }

    @Transactional
    public User registerUser(String username, String name, String email, String password, Long number, String address, String introducer) throws UserException {

        User introducerUser = null;
        if (introducer != null && !introducer.isEmpty()) {
            introducerUser = userRepository.getUserByUsername(introducer);

            if (introducerUser == null) {
                throw new UserException(UserExceptionStatus.INTRODUCER_NOT_FOUND);
            }
        }

        return userRepository.registerUser(username, name, email, password, number, address, introducerUser, UserRole.USER);
    }
}
