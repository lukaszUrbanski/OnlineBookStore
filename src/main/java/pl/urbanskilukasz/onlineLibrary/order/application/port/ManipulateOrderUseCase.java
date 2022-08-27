package pl.urbanskilukasz.onlineLibrary.order.application.port;

import lombok.*;
import pl.urbanskilukasz.onlineLibrary.order.domain.Order;
import pl.urbanskilukasz.onlineLibrary.order.domain.OrderItem;
import pl.urbanskilukasz.onlineLibrary.order.domain.OrderStatus;
import pl.urbanskilukasz.onlineLibrary.order.domain.Recipient;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;

public interface ManipulateOrderUseCase {
    PlaceOrderResponse placeOrder(PlaceOrderCommand command);

    void updateOrder(UpdateOrderCommand command);

    void deleteOrder(Long id);

    @Builder
    @Value
    @AllArgsConstructor
    class PlaceOrderCommand{
        @Singular
        List<OrderItemCommand> items;
        Recipient recipient;
    }


    @Value
    class PlaceOrderResponse{
        boolean response;
        Long orderId;
        List<String> errors;

        public static PlaceOrderResponse success(Long orderId){
            return new PlaceOrderResponse(true, orderId, emptyList());
        }

        public static PlaceOrderResponse failure(String... errors){
            return new PlaceOrderResponse(false, null, Arrays.asList(errors));
        }
    }
    @Data
    @AllArgsConstructor
    class OrderItemCommand{
        Long bookId;
        int quantity;
    }

    @Data
    @AllArgsConstructor
    class UpdateOrderCommand{
        Long id;
        String status;

        public Order updateOrder(Order order){
            if (status != null){
                order.setStatus(OrderStatus.valueOf(status.toUpperCase()));
            }
            return order;
        }
    }



}
