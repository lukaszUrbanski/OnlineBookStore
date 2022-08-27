package pl.urbanskilukasz.onlineLibrary.order.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.urbanskilukasz.onlineLibrary.order.application.port.QueryOrderUseCase;
import pl.urbanskilukasz.onlineLibrary.order.domain.Order;
import pl.urbanskilukasz.onlineLibrary.order.domain.OrderRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class QueryOrderService implements QueryOrderUseCase {
    private final OrderRepository orderRepository;

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }
}
