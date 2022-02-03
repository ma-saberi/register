package register.api;

import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import register.api.model.RestResponse;
import register.data.entity.User;
import register.data.mapperToApi.UserMapper;
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

    @PostMapping("/users")
    public ResponseEntity<?> registerUser(
            @ApiParam(name = "name")
            @RequestParam(value = "name", required = false) String name,
            @ApiParam(name = "username", required = true)
            @RequestParam("username") String username,
            @ApiParam(name = "email", required = true)
            @RequestParam("email") String email,
            @ApiParam(name = "password", required = true)
            @RequestParam("password") String password,
            @ApiParam(name = "introducer")
            @RequestParam(value = "introducer", required = false) String introducer,
            @ApiParam(name = "number")
            @RequestParam(value = "number", required = false) Long number,
            @ApiParam(name = "address")
            @RequestParam(value = "address", required = false) String address

    ) throws UserException {


        User user = userService.registerUser(username, name, email, password, number, address, introducer);
        register.api.model.User userModel = UserMapper.INSTANCE.modelToApi(user);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        RestResponse.Builder()
                                .result(userModel)
                                .status(HttpStatus.OK)
                                .build()
                );

    }
}
