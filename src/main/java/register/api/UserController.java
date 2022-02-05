package register.api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import register.api.model.Login;
import register.api.model.RestResponse;
import register.data.entity.User;
import register.data.entity.UserRole;
import register.data.mapperToApi.UserMapper;
import register.domain.UserService;
import register.domain.annotation.ResourceRole;
import register.exception.UserException;


@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/users/login")
    public ResponseEntity<?> login(
            @ApiParam(value = "Search query")
            @RequestBody Login login
    ) throws UserException {


        String token = userService.login(login);

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

    @ApiOperation(value = "about user")
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized"),
    })
    @GetMapping("/users/me")
    @ResourceRole({UserRole.ADMIN, UserRole.USER})
    public ResponseEntity<?> getMe() throws UserException {

        User user = userService.getMe();
        register.api.model.User result = UserMapper.INSTANCE.modelToApi(user);


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        RestResponse.Builder()
                                .result(result)
                                .status(HttpStatus.OK)
                                .build()
                );

    }

    @ApiOperation(value = "Change role")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid role change."),
            @ApiResponse(code = 404, message = "Not found user with this username."),
    })
    @GetMapping("/users/role")
    @ResourceRole(UserRole.SYS_ADMIN)
    public ResponseEntity<?> changeRole(
            @RequestParam("username") String username,
            @RequestParam("role") UserRole role
    ) throws UserException {

        userService.changeRole(username, role);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        RestResponse.Builder()
                                .status(HttpStatus.OK)
                                .message("تغییر نقش با موفقیت انجام شد.")
                                .build()
                );

    }

}
