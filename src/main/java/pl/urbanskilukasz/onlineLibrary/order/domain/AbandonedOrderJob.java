package pl.urbanskilukasz.onlineLibrary.order.domain;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.urbanskilukasz.onlineLibrary.order.application.port.ManipulateOrderUseCase;
import pl.urbanskilukasz.onlineLibrary.order.db.OrderJpaRepository;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static pl.urbanskilukasz.onlineLibrary.order.application.port.ManipulateOrderUseCase.*;

@Slf4j
@Component
@AllArgsConstructor
public class AbandonedOrderJob {
    private final OrderJpaRepository repository;
    private final ManipulateOrderUseCase orderUseCase;
    private final OrdersProperties properties;


    @Transactional
    @Scheduled(cron = "${app.orders.abandon-cron}")
    public void run(){
        Duration paymentPeriod = properties.getPaymentPeriod();
        LocalDateTime olderThan = LocalDateTime.now().minus(paymentPeriod);
        List<Order> orders = repository.findByStatusAndCreatedAtLessThanEqual(OrderStatus.NEW, olderThan);
        log.info("Found orders to be abandoned :" + orders.size());
        orders.forEach(order -> {
            //Todo: naprawić w module security
            UpdateStatusCommand command = new UpdateStatusCommand(order.getId(), OrderStatus.ABANDONED, null);
            orderUseCase.updateOrderStatus(command);
        });
    }
}
