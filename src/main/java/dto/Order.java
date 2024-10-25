package dto;

import javafx.collections.ObservableList;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Order {
    private String orderId;
    private LocalDateTime dateTime;
    private String empId;
    private String cusEmail;
    private Double total;
    private List<OrderDetails> orderDetailsList;
}
