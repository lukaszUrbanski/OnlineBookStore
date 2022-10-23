package pl.urbanskilukasz.onlineLibrary.order.application.port;

import lombok.*;
import org.springframework.http.HttpStatus;
import pl.urbanskilukasz.onlineLibrary.commons.Either;
import pl.urbanskilukasz.onlineLibrary.order.domain.Order;
import pl.urbanskilukasz.onlineLibrary.order.domain.OrderItem;
import pl.urbanskilukasz.onlineLibrary.order.domain.OrderStatus;
import pl.urbanskilukasz.onlineLibrary.order.domain.Recipient;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;

public interface ManipulateOrderUseCase {
    PlaceOrderResponse placeOrder(PlaceOrderCommand command);

    void deleteOrder(Long id);

    UpdateStatusResponse updateOrderStatus(Long id, OrderStatus orderStatus);

    @Builder
    @Value
    @AllArgsConstructor
    class PlaceOrderCommand {
        @Singular
        List<OrderItemCommand> items;
        Recipient recipient;
    }

    @Data
    @AllArgsConstructor
    class OrderItemCommand {
        Long bookId;
        int quantity;
    }

    @Data
    @AllArgsConstructor
    class UpdateOrderCommand {
        Long id;
        String status;

        public Order updateOrder(Order order) {
            if (status != null) {
                order.setStatus(OrderStatus.valueOf(status.toUpperCase()));
            }
            return order;
        }
    }

    @Getter
    class PlaceOrderResponse extends Either<String, Long> {

        public PlaceOrderResponse(boolean success, String left, Long right) {
            super(success, left, right);
        }

        public static PlaceOrderResponse success(Long orderId) {
            return new PlaceOrderResponse(true, null, orderId);
        }
        public PlaceOrderResponse failure(String error) {
            return new PlaceOrderResponse(false, error, null);
        }
    }
    class UpdateStatusResponse extends Either<Error, OrderStatus> {

        public UpdateStatusResponse(boolean success, Error left, OrderStatus right) {
            super(success, left, right);
        }

        public static UpdateStatusResponse success(OrderStatus status) {
            return new UpdateStatusResponse(true, null, status);
        }
        public static UpdateStatusResponse failure(Error error) {
            return new UpdateStatusResponse(false, error, null);
        }

    }

    @AllArgsConstructor
    @Getter
    enum Error {
        NOT_FOUND(HttpStatus.NOT_FOUND),
        FORBIDDEN(HttpStatus.FORBIDDEN);
        private final HttpStatus status;

    }


}
