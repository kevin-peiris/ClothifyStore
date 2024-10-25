package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    private String orderId;

    private LocalDateTime dateTime;

    private String empId;

    private String cusEmail;

    private Double total;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderDetailsEntity> orderDetails;
}
