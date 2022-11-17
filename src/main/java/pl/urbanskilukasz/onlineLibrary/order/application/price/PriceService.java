package pl.urbanskilukasz.onlineLibrary.order.application.price;

import org.springframework.stereotype.Service;
import pl.urbanskilukasz.onlineLibrary.order.domain.Order;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PriceService {
    private final List<DiscountStrategy> strategies = List.of(
            new DeliveryDiscountStrategy(),
            new TotalPriceDiscountStrategy()
    );

    public OrderPrice calculateOrderPrice(Order order){
        return new OrderPrice(
                order.getItemsPrice(),
                order.getDeliveryPrice(),
                discounts(order)
        );
    }

    private BigDecimal discounts(Order order) {
        return strategies
                .stream()
                .map(strategy -> strategy.calculate(order))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}