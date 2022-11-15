package pl.urbanskilukasz.onlineLibrary.order.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import pl.urbanskilukasz.onlineLibrary.catalog.db.BookJpaRepository;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.Book;
import pl.urbanskilukasz.onlineLibrary.clock.Clock;
import pl.urbanskilukasz.onlineLibrary.order.application.ManipulateOrderService;
import pl.urbanskilukasz.onlineLibrary.order.application.QueryOrderService;
import pl.urbanskilukasz.onlineLibrary.order.application.port.ManipulateOrderUseCase;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(
        properties = "app.orders.payment-period: 1H"
)
@AutoConfigureTestDatabase
class AbandonedOrderJobTest {

    @TestConfiguration
    static class TestConfig{
        @Bean
        public Clock.Fake clock() {
            return new Clock.Fake();
        }
    }


    @Autowired
    QueryOrderService queryOrderService;

    @Autowired
    BookJpaRepository bookJpaRepository;

    @Autowired
    ManipulateOrderService manipulateOrderService;

    @Autowired
    Clock.Fake clock;

    @Autowired
    AbandonedOrderJob orderJob;

    @Test
    public void shouldMarkOrderAsAbandoned(){
        //given - orders
        Book effectiveJava = givenEffectiveJava(50L);
        Long orderId = placeOrder(effectiveJava.getId(), 15);
        //when - run
        clock.tick(Duration.ofHours(2));
        orderJob.run();

        // then status changed
        assertEquals(50L, availableCopiesOf(effectiveJava));
        assertEquals(OrderStatus.ABANDONED, getOrderStatus(orderId));
    }

    private Long placeOrder(Long id, int copies) {
        ManipulateOrderUseCase.PlaceOrderCommand command = ManipulateOrderUseCase.PlaceOrderCommand
                .builder()
                .recipient(recipient())
                .item(new ManipulateOrderUseCase.OrderItemCommand(id, copies))
                .build();

        return manipulateOrderService.placeOrder(command).getRight();
    }
    private Recipient recipient() {
        return Recipient.builder().email("marek@example.org").build();
    }

    private Book givenEffectiveJava(Long available) {
        return bookJpaRepository.save(new Book("Effective Java", 2005, new BigDecimal("99.90"), available));
    }
    private Long availableCopiesOf(Book book) {
        return bookJpaRepository.findById(book.getId()).get().getAvailable();
    }

    private OrderStatus getOrderStatus(Long orderId) {
        return queryOrderService.findById(orderId).get().getStatus();
    }
}