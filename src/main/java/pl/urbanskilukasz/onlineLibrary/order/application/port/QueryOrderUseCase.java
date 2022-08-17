package pl.urbanskilukasz.onlineLibrary.order.application.port;

import pl.urbanskilukasz.onlineLibrary.order.domain.Order;

import java.util.List;

public interface QueryOrderUseCase {

    List<Order> findAll();
}
