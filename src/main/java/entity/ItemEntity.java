package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "item")
public class ItemEntity {
    @Id
    private String itemId;

    private String name;

    private String supId;

    private Double price;

    private Integer qty;

    private String size;

    @Lob
    @Column(name = "image", columnDefinition = "LONGTEXT")
    private String image;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private Set<OrderDetailsEntity> orderDetails;
}
