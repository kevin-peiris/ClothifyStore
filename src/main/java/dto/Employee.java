package dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Employee {
    private String empId;
    private String name;
    private String email;
    private String password;
    private Integer orderCount;
    private byte[] image;
}
