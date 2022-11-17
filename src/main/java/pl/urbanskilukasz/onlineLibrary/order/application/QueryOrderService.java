package pl.urbanskilukasz.onlineLibrary.order.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.urbanskilukasz.onlineLibrary.catalog.db.BookJpaRepository;
import pl.urbanskilukasz.onlineLibrary.order.application.port.QueryOrderUseCase;
import pl.urbanskilukasz.onlineLibrary.order.application.price.OrderPrice;
import pl.urbanskilukasz.onlineLibrary.order.application.price.PriceService;
import pl.urbanskilukasz.onlineLibrary.order.db.OrderJpaRepository;
import pl.urbanskilukasz.onlineLibrary.order.domain.Order;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class QueryOrderService implements QueryOrderUseCase {
    private final OrderJpaRepository orderRepository;
    private final PriceService priceService;

    @Override
    @Transactional
    public List<RichOrder> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(this::toRichOrder)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Optional<RichOrder> findById(Long id) {
        return orderRepository.findById(id).map(this::toRichOrder);
    }
    public RichOrder toRichOrder(Order order){
        OrderPrice orderPrice = priceService.calculateOrderPrice(order);
        return new RichOrder(
                order.getId(),
                order.getStatus(),
                order.getItems(),
                order.getRecipient(),
                order.getCreatedAt(),
                orderPrice,
                orderPrice.finalPrice()

        );
    }
}
