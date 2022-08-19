package pl.urbanskilukasz.onlineLibrary.order.application.port;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import pl.urbanskilukasz.onlineLibrary.order.domain.OrderItem;
import pl.urbanskilukasz.onlineLibrary.order.domain.Recipient;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;

public interface PlaceOrderUseCase {
    PlaceOrderResponse placeOrder(PlaceOrderCommand command);

    @Builder
    @Value
    class PlaceOrderCommand{
        @Singular
        List<OrderItem> items;
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



}