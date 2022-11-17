package pl.urbanskilukasz.onlineLibrary.order.application.price;

import pl.urbanskilukasz.onlineLibrary.order.domain.Order;

import java.math.BigDecimal;

public class DeliveryDiscountStrategy implements DiscountStrategy {
    @Override
    public BigDecimal calculate(Order order) {
        return null;
    }
}
