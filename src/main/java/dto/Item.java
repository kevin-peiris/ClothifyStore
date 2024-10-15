package dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Item {
    private String itemId;
    private String name;
    private String supId;
    private Double price;
    private Integer qty;
    private String size;
    private byte[] image;
}
