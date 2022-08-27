package pl.urbanskilukasz.onlineLibrary.order.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.CatalogRepository;
import pl.urbanskilukasz.onlineLibrary.order.application.port.ManipulateOrderUseCase;
import pl.urbanskilukasz.onlineLibrary.order.domain.Order;
import pl.urbanskilukasz.onlineLibrary.order.domain.OrderItem;
import pl.urbanskilukasz.onlineLibrary.order.domain.OrderRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ManipulateOrderUseCaseService implements ManipulateOrderUseCase {
    private final OrderRepository orderRepository;
    private final CatalogRepository catalogRepository;

    @Override
    public PlaceOrderResponse placeOrder(PlaceOrderCommand command) {
        List<OrderItem> items = command.getItems()
                .stream()
                .map(item -> new OrderItem(
                        catalogRepository.findById(item.getBookId()).get(),
                        item.getQuantity()))
                .collect(Collectors.toList());
        Order order = Order
                .builder()
                .items(items)
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
        orderRepository.deleteOrder(id);
    }

}
