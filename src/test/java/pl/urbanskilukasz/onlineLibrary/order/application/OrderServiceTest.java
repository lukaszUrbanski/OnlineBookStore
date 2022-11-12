package pl.urbanskilukasz.onlineLibrary.order.application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import pl.urbanskilukasz.onlineLibrary.catalog.db.BookJpaRepository;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.Book;
import pl.urbanskilukasz.onlineLibrary.order.application.port.ManipulateOrderUseCase.OrderItemCommand;
import pl.urbanskilukasz.onlineLibrary.order.application.port.ManipulateOrderUseCase.PlaceOrderCommand;
import pl.urbanskilukasz.onlineLibrary.order.application.port.ManipulateOrderUseCase.PlaceOrderResponse;
import pl.urbanskilukasz.onlineLibrary.order.domain.OrderStatus;
import pl.urbanskilukasz.onlineLibrary.order.domain.Recipient;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({ManipulateOrderUseCaseService.class, QueryOrderService.class})
class OrderServiceTest {

    @Autowired
    BookJpaRepository bookJpaRepository;

    @Autowired
    ManipulateOrderUseCaseService orderUseCase;

    @Autowired
    QueryOrderService queryOrderService;

    @Test
    public void userCanPlaceOrder() {
        //given
        Book currencyJava = givenJavaCurrency(50L);
        Book effectiveJava = givenEffectiveJava(50L);

        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient())
                .item(new OrderItemCommand(effectiveJava.getId(), 10))
                .item(new OrderItemCommand(currencyJava.getId(), 15))
                .build();
        //when
        PlaceOrderResponse response = orderUseCase.placeOrder(command);
        //then
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(40, availableCopiesOf(effectiveJava));
        Assertions.assertEquals(35, availableCopiesOf(currencyJava));
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
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> orderUseCase.placeOrder(command));

        //then
        Assertions.assertTrue(exception.getMessage().contains("Too many copies of book "+ currencyJava.getId() +" requested"));
    }

    @Test
    public void userCanRevokeOrder(){
        //given
        Book effectiveJava = givenEffectiveJava(50L);
        Long orderId = placeOrder(effectiveJava.getId(), 15);
        assertEquals(35L, availableCopiesOf(effectiveJava));

        //when
        orderUseCase.updateOrderStatus(orderId, OrderStatus.CANCELED);

        //then
        assertEquals(50L, availableCopiesOf(effectiveJava));
        assertEquals(OrderStatus.CANCELED, getOrderStatus(orderId));
    }

    private Long placeOrder(Long id, int copies){
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient())
                .item(new OrderItemCommand(id, copies))
                .build();

        return orderUseCase.placeOrder(command).getRight();
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

    private Long availableCopiesOf(Book effectiveJava) {
        return bookJpaRepository.findById(effectiveJava.getId()).get().getAvailable();
    }

    private OrderStatus getOrderStatus(Long orderId) {
        return queryOrderService.findById(orderId).get().getStatus();
    }
}