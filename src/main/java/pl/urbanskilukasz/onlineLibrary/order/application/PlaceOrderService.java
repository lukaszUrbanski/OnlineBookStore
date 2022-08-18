package pl.urbanskilukasz.onlineLibrary.order.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.urbanskilukasz.onlineLibrary.order.application.port.PlaceOrderUseCase;
import pl.urbanskilukasz.onlineLibrary.order.domain.Order;
import pl.urbanskilukasz.onlineLibrary.order.domain.OrderRepository;

@Service
@AllArgsConstructor
public class PlaceOrderService implements PlaceOrderUseCase {
    private final OrderRepository repository;

    @Override
    public PlaceOrderResponse placeOrder(PlaceOrderCommand command) {
        Order order = Order
                .builder()
                .items(command.getItems())
                .recipient(command.getRecipient())
                .build();
        Order save = repository.save(order);
        return PlaceOrderResponse.success(save.getId());
    }
}
