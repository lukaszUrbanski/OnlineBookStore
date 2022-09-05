package pl.urbanskilukasz.onlineLibrary.order.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.urbanskilukasz.onlineLibrary.order.application.port.ManipulateOrderUseCase;
import pl.urbanskilukasz.onlineLibrary.order.application.port.ManipulateOrderUseCase.PlaceOrderCommand;
import pl.urbanskilukasz.onlineLibrary.order.application.port.ManipulateOrderUseCase.UpdateOrderCommand;
import pl.urbanskilukasz.onlineLibrary.order.application.port.QueryOrderUseCase;
import pl.urbanskilukasz.onlineLibrary.order.domain.OrderItem;
import pl.urbanskilukasz.onlineLibrary.order.domain.Recipient;

import java.util.List;
import java.util.stream.Collectors;

import static pl.urbanskilukasz.onlineLibrary.order.application.port.QueryOrderUseCase.*;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {
    private final QueryOrderUseCase queryOrder;
    private final ManipulateOrderUseCase manipulateOrderUseCase;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RichOrder> getOrders(){
        return queryOrder.findAll();
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RichOrder> getOrderById(@PathVariable Long id){
        return queryOrder.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createOrder(@RequestBody RestOrderCommand restCommand){
        manipulateOrderUseCase.placeOrder(restCommand.toPlaceOrderCommand());
    }

    @PutMapping("/{id}/status")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateStatus(@PathVariable Long id, @RequestBody RestUpdateOrderCommand updateOrderCommand){
        manipulateOrderUseCase.updateOrder(updateOrderCommand.toCommand(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Long id){
        manipulateOrderUseCase.deleteOrder(id);
    }
    @Data
    private static class RestOrderCommand{
        List<OrderItemCommand> items;
        RecipientCommand recipient;

        PlaceOrderCommand toPlaceOrderCommand() {
            List<OrderItem> orderItems = items
                    .stream()
                    .map(item -> new OrderItem(item.bookId, item.quantity))
                    .collect(Collectors.toList());
            return new PlaceOrderCommand(orderItems, recipient.toRecipient());
        }
    }
    @Data
    static class OrderItemCommand {
        Long bookId;
        int quantity;
    }
    @Data
    static class RecipientCommand {
        String name;
        String phone;
        String street;
        String city;
        String zipCode;
        String email;
        Recipient toRecipient() {
            return new Recipient(name, phone, street, city, zipCode, email);
        }
    }


    @Data
    private static class RestUpdateOrderCommand{
        String status;

        UpdateOrderCommand toCommand(Long id) {
           return new UpdateOrderCommand(
                    id,
                    status
            );
        }
    }
}
