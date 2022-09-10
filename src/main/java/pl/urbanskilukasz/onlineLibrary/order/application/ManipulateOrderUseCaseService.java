package pl.urbanskilukasz.onlineLibrary.order.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.urbanskilukasz.onlineLibrary.catalog.db.BookJpaRepository;
import pl.urbanskilukasz.onlineLibrary.order.application.port.ManipulateOrderUseCase;
import pl.urbanskilukasz.onlineLibrary.order.db.OrderJpaRepository;
import pl.urbanskilukasz.onlineLibrary.order.domain.Order;
import pl.urbanskilukasz.onlineLibrary.order.domain.OrderStatus;

@Service
@AllArgsConstructor
public class ManipulateOrderUseCaseService implements ManipulateOrderUseCase {
    private final OrderJpaRepository orderRepository;

    @Override
    public PlaceOrderResponse placeOrder(PlaceOrderCommand command) {

        Order order = Order
                .builder()
                .items(command.getItems())
                .recipient(command.getRecipient())
                .build();
        Order save = orderRepository.save(order);
        return PlaceOrderResponse.success(save.getId());
    }

    @Override
    public void updateOrder(UpdateOrderCommand command) {
        orderRepository.findById(command.getId())
                .ifPresent(order -> {
                    Order updatedOrder = command.updateOrder(order);
                    orderRepository.save(updatedOrder);
                });
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public void updateOrderStatus(Long id, OrderStatus status) {
        orderRepository.findById(id)
                .ifPresent(order -> {
                   order.updateStatus(status);
                   orderRepository.save(order);
                });
    }

}
