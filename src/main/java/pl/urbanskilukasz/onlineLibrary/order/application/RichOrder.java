package pl.urbanskilukasz.onlineLibrary.order.application;

import lombok.Value;
import pl.urbanskilukasz.onlineLibrary.order.application.price.OrderPrice;
import pl.urbanskilukasz.onlineLibrary.order.domain.OrderItem;
import pl.urbanskilukasz.onlineLibrary.order.domain.OrderStatus;
import pl.urbanskilukasz.onlineLibrary.order.domain.Recipient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Value
public
class RichOrder {
    Long id;
    OrderStatus status;
    Set<OrderItem> items;
    Recipient recipient;
    LocalDateTime createdAt;
    OrderPrice orderPrice;
    BigDecimal finalPrice;


}
