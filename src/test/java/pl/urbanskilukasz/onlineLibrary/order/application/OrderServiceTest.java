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
import static pl.urbanskilukasz.onlineLibrary.order.application.port.ManipulateOrderUseCase.*;

@DataJpaTest
@Import({ManipulateOrderService.class, QueryOrderService.class})
class OrderServiceTest {

    @Autowired
    BookJpaRepository bookJpaRepository;

    @Autowired
    ManipulateOrderService service;

    @Autowired
    QueryOrderService queryOrderService;

    @Test
    public void userCanPlaceOrder() {
        //given
        Book currencyJava = givenJavaCurrency(50L);
        Book effectiveJava = givenEffectiveJava(50L);

        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient("jan@example.com"))
                .item(new OrderItemCommand(effectiveJava.getId(), 10))
                .item(new OrderItemCommand(currencyJava.getId(), 15))
                .build();
        //when
        PlaceOrderResponse response = service.placeOrder(command);
        //then
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(40, availableCopiesOf(effectiveJava));
        Assertions.assertEquals(35, availableCopiesOf(currencyJava));
    }

    @Test
    public void userCantOrderMoreBookThanIsAvailable() {
        //given
        Book currencyJava = givenJavaCurrency(5L);


        //when
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> placeOrder(currencyJava.getId(), 20));

        //then
        Assertions.assertTrue(exception.getMessage().contains("Too many copies of book " + currencyJava.getId() + " requested"));
    }

    @Test
    public void userCanRevokeOrder() {
        //given
        Book effectiveJava = givenEffectiveJava(50L);
        Long orderId = placeOrder(effectiveJava.getId(), 15);
        assertEquals(35L, availableCopiesOf(effectiveJava));
        UpdateStatusCommand command = new UpdateStatusCommand(orderId, OrderStatus.CANCELED, null);
        //when
        service.updateOrderStatus(command);

        //then
        assertEquals(50L, availableCopiesOf(effectiveJava));
        assertEquals(OrderStatus.CANCELED, getOrderStatus(orderId));
    }

    @Test
    public void useCannotRevokePaidOrder() {
        //given
        Book effectiveJava = givenEffectiveJava(50L);
        Long orderId = placeOrder(effectiveJava.getId(), 20);
        assertEquals(30, availableCopiesOf(effectiveJava));
        service.updateOrderStatus(new UpdateStatusCommand(orderId, OrderStatus.PAID, null));

        //when
        IllegalStateException exception = assertThrows(
                IllegalStateException.class, () -> service.updateOrderStatus(new UpdateStatusCommand(orderId, OrderStatus.CANCELED,null)));
        //then
        assertTrue(exception.getMessage().contains("Cannot cancel paid order."));
        assertEquals(30, availableCopiesOf(effectiveJava));
    }

    @Test
    public void userCannotRevokeShippedOrder() {
        //given
        Book effectiveJava = givenEffectiveJava(50L);
        Long orderId = placeOrder(effectiveJava.getId(), 30);
        UpdateStatusCommand command = new UpdateStatusCommand(orderId, OrderStatus.PAID, null);
        service.updateOrderStatus(command);
        service.updateOrderStatus(command);

        //when
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> service.updateOrderStatus(new UpdateStatusCommand(orderId, OrderStatus.CANCELED, null)));

        //then
        assertTrue(exception.getMessage().contains("Cannot cancel shipped order."));
        assertEquals(20, availableCopiesOf(effectiveJava));



    }

    @Test
    public void userCannotOrderNonExistingBook() {
        //given
        Long nonExistingBookId = 1L;
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> placeOrder(nonExistingBookId, 30));
        //then
        assertTrue(exception.getMessage().contains("Book with id" + nonExistingBookId + " not exists"));
    }

   @Test
    public void userCannotOrderNegativeNumberOfBook() {
        //given
        Book effectiveJava = givenEffectiveJava(50L);
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> placeOrder(effectiveJava.getId(), -10));

        //then
        assertTrue(exception.getMessage().contains("Cannot order negative number of book"));
    }

    @Test
    public void userCannotRevokeOtherUserOrder(){
        //given
        Book effectiveJava = givenEffectiveJava(50L);
        String adam = "adam@example.com";
        Long orderId = placeOrder(effectiveJava.getId(), 15, adam);
        assertEquals(35L, availableCopiesOf(effectiveJava));
        //when
        UpdateStatusCommand command = new UpdateStatusCommand(orderId, OrderStatus.CANCELED, "marek@exampe.com");
        service.updateOrderStatus(command);

        //then
        assertEquals(35, availableCopiesOf(effectiveJava));
        assertEquals(OrderStatus.NEW, getOrderStatus(orderId));
    }
    @Test
    //TODO poprawić przy security
    public void adminCanRevokeOtherUserOrder(){
        //given
        Book effectiveJava = givenEffectiveJava(50L);
        String marek = "marek@example.com";
        Long orderId = placeOrder(effectiveJava.getId(), 15, marek);
        assertEquals(35L, availableCopiesOf(effectiveJava));
        //when
        String admin = "admin@example.org";
        UpdateStatusCommand command = new UpdateStatusCommand(orderId, OrderStatus.CANCELED, admin);
        service.updateOrderStatus(command);

        //then
        assertEquals(50, availableCopiesOf(effectiveJava));
        assertEquals(OrderStatus.CANCELED, getOrderStatus(orderId));
    }
    @Test
    //TODO poprawić przy security
    public void adminCanMarkOrderAsPaid(){
        //given
        Book effectiveJava = givenEffectiveJava(50L);
        String marek = "marek@example.com";
        Long orderId = placeOrder(effectiveJava.getId(), 15, marek);
        assertEquals(35L, availableCopiesOf(effectiveJava));
        //when
        String admin = "admin@example.org";
        UpdateStatusCommand command = new UpdateStatusCommand(orderId, OrderStatus.PAID, admin);
        service.updateOrderStatus(command);

        //then
        assertEquals(35, availableCopiesOf(effectiveJava));
        assertEquals(OrderStatus.PAID, getOrderStatus(orderId));
    }

    @Test
    public void shippingPriceShouldBeAddedToTotalPrice(){
        //given 
        Book book = getBook(50L, "49.90");
        //when
        Long orderId = placeOrder(book.getId(), 1);
        //then
        assertEquals("59.80", orderOf(orderId).getFinalPrice().toPlainString());
    }

    private RichOrder orderOf(Long orderId) {
        return queryOrderService.findById(orderId).get();
    }

    private Book getBook(long quantity, String price) {
        return bookJpaRepository.save(new Book("Effective Java", 2009, new BigDecimal(price), quantity));
    }


    private Long placeOrder(Long id, int copies) {
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient("jan123@email.com"))
                .item(new OrderItemCommand(id, copies))
                .build();

        return service.placeOrder(command).getRight();
    }

    private Long placeOrder(Long id, int copies, String recipient) {
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient(recipient))
                .item(new OrderItemCommand(id, copies))
                .build();

        return service.placeOrder(command).getRight();
    }

    private Book givenEffectiveJava(Long available) {
        return bookJpaRepository.save(new Book("Effective Java", 2005, new BigDecimal("99.90"), available));
    }

    private Book givenJavaCurrency(Long available) {
        return bookJpaRepository.save(new Book("Java Currency in Practice", 2006, new BigDecimal("129.90"), available));
    }

    private Recipient recipient(String recipient) {
        return Recipient.builder().email(recipient).build();
    }

    private Long availableCopiesOf(Book book) {
        return bookJpaRepository.findById(book.getId()).get().getAvailable();
    }

    private OrderStatus getOrderStatus(Long orderId) {
        return queryOrderService.findById(orderId).get().getStatus();
    }
}