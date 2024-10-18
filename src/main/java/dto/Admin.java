package dto;

import lombok.*;
import util.RoleType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Admin {
    private Long id;
    private String email;
    private String password;
    private String role;

    public Admin(String email, String password,String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
