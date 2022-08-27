package pl.urbanskilukasz.onlineLibrary.order.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.urbanskilukasz.onlineLibrary.catalog.application.port.CatalogUseCase;
import pl.urbanskilukasz.onlineLibrary.order.application.port.ManipulateOrderUseCase;
import pl.urbanskilukasz.onlineLibrary.order.application.port.ManipulateOrderUseCase.OrderItemCommand;
import pl.urbanskilukasz.onlineLibrary.order.application.port.ManipulateOrderUseCase.PlaceOrderCommand;
import pl.urbanskilukasz.onlineLibrary.order.application.port.ManipulateOrderUseCase.UpdateOrderCommand;
import pl.urbanskilukasz.onlineLibrary.order.application.port.QueryOrderUseCase;
import pl.urbanskilukasz.onlineLibrary.order.domain.Order;
import pl.urbanskilukasz.onlineLibrary.order.domain.Recipient;

import java.util.List;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {
    private final QueryOrderUseCase queryOrder;
    private final ManipulateOrderUseCase manipulateOrderUseCase;
    private final CatalogUseCase catalog;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Order> getOrders(){
        return queryOrder.findAll();
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Order> getOrderById(@PathVariable Long id){
        return queryOrder.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createOrder(@RequestBody RestOrderCommand restCommand){
        manipulateOrderUseCase.placeOrder(restCommand.toCommand());
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
        List<OrderItemCommand> restItems;
        Recipient recipient;

        public PlaceOrderCommand toCommand(){
            return new PlaceOrderCommand(restItems, recipient);}
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
