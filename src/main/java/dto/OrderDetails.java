package dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderDetails {
    private Long id;
    private String orderId;
    private String itemId;
    private Integer qty;
    private Double price;
    private String size;
    private double total;

    public OrderDetails(String orderId, String itemId, int qty, double price, String size, double total) {
        this.size = size;
        this.price = price;
        this.qty = qty;
        this.itemId = itemId;
        this.orderId = orderId;
        this.total = total;
    }
}
