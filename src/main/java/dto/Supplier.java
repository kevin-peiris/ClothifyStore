package dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Supplier {
    private String supId;
    private String name;
    private String company;
    private String email;
}
