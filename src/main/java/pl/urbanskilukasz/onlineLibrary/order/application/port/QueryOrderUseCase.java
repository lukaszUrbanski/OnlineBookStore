package pl.urbanskilukasz.onlineLibrary.order.application.port;

import pl.urbanskilukasz.onlineLibrary.order.domain.Order;

import java.util.List;
import java.util.Optional;

public interface QueryOrderUseCase {

    List<Order> findAll();

    Optional<Order> findById(Long id);
}
