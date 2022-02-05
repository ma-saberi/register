package register.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import register.data.entity.UserRole;

@Setter
@Getter
@NoArgsConstructor
public class User {

    private String name;
    private String username;
    private String email;
    private String number;
    private String address;
    private String introducer;
    private UserRole role;
    private Long created;

}
