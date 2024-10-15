package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class CartTM {
    private String itemId;
    private String name;
    private int qty;
    private double price;
    private String size;
    private double total;
    private byte[] image;
}
