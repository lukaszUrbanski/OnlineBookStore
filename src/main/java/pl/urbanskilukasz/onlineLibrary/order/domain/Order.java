package pl.urbanskilukasz.onlineLibrary.order.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {


    @Id
    @GeneratedValue
    private Long id;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.NEW;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name ="order_id")
    private List<OrderItem> items;

    private transient Recipient recipient;

    private LocalDateTime createdAt;

//    public BigDecimal totalPrice() {
//        return items.stream()
//                .map(item -> item.getBook().getPrice().multiply(new BigDecimal(item.getQuantity())))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//    }

}
