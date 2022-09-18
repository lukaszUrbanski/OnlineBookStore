package pl.urbanskilukasz.onlineLibrary.order.application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import pl.urbanskilukasz.onlineLibrary.catalog.db.BookJpaRepository;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.Book;
import pl.urbanskilukasz.onlineLibrary.order.application.port.ManipulateOrderUseCase;
import pl.urbanskilukasz.onlineLibrary.order.application.port.ManipulateOrderUseCase.OrderItemCommand;
import pl.urbanskilukasz.onlineLibrary.order.application.port.ManipulateOrderUseCase.PlaceOrderCommand;
import pl.urbanskilukasz.onlineLibrary.order.application.port.ManipulateOrderUseCase.PlaceOrderResponse;
import pl.urbanskilukasz.onlineLibrary.order.domain.Recipient;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({ManipulateOrderUseCaseService.class})
class ManipulateOrderUseCaseServiceTest {

    @Autowired
    BookJpaRepository bookJpaRepository;

    @Autowired
    ManipulateOrderUseCaseService orderUseCase;
    @Test
    public void userCanPlaceOrder() {
        //given
        Book currencyJava = givenJavaCurrency(50L);
        Book effectiveJava = givenEffectiveJava(50L);

        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient())
                .item(new OrderItemCommand(effectiveJava.getId(), 10))
                .item(new OrderItemCommand(currencyJava.getId(), 10))
                .build();
        //when
        PlaceOrderResponse response = orderUseCase.placeOrder(command);
        //then
        Assertions.assertTrue
                (response.isResponse());
    }
    @Test
    public void userCantOrderMoreBookThanIsAvailable() {
        //given
        Book currencyJava = givenJavaCurrency(5L);

        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient())
                .item(new OrderItemCommand(currencyJava.getId(), 10))
                .build();
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderUseCase.placeOrder(command);
        });

        //then
        Assertions.assertTrue(exception.getMessage().contains("Too many copies of book "+ currencyJava.getId() +" requested"));
    }

    private Book givenEffectiveJava(Long available) {
        return bookJpaRepository.save(new Book("Effective Java", 2005, new BigDecimal("99.90"), available));
    }

    private Book givenJavaCurrency(Long available) {
        return bookJpaRepository.save(new Book("Java Currency in Practice", 2006, new BigDecimal("129.90"), available));
    }

    private Recipient recipient(){
        return Recipient.builder().email("jan123@email.com").build();
    }
}