package pl.urbanskilukasz.onlineLibrary.order.domain;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.urbanskilukasz.onlineLibrary.order.application.port.ManipulateOrderUseCase;
import pl.urbanskilukasz.onlineLibrary.order.db.OrderJpaRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class AbandonedOrderJob {
    private final OrderJpaRepository repository;
    private final ManipulateOrderUseCase orderUseCase;


    @Transactional
    @Scheduled(fixedRate = 60_000)
    public void run(){
        LocalDateTime timestamp = LocalDateTime.now().minusMinutes(5);
        List<Order> orders = repository.findByStatusAndCreatedAtLessThanEqual(OrderStatus.NEW, timestamp);
        System.out.println("Found orders to be abandoned :" + orders.size());
        orders.forEach(order -> orderUseCase.updateOrderStatus(order.getId(), OrderStatus.ABANDONED));
    }
}
