package pl.urbanskilukasz.onlineLibrary.order.application.price;

import pl.urbanskilukasz.onlineLibrary.order.domain.Order;

import java.math.BigDecimal;

public interface DiscountStrategy {
    BigDecimal calculate(Order order);
}
