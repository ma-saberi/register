package register.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import register.api.model.RestResponse;
import register.domain.UserService;
import register.exception.UserException;


@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/users/login")
    public ResponseEntity<?> login(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) throws UserException {


        String token = userService.login(username, password);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        RestResponse.Builder()
                                .result(token)
                                .status(HttpStatus.OK)
                                .build()
                );

    }
}
