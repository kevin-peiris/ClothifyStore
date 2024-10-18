package dto;

import lombok.*;
import util.RoleType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {
    private Long id;
    private String empId;
    private String email;
    private String password;
    private String role;

    public User(String empId, String email, String password,String role) {
        this.empId = empId;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
