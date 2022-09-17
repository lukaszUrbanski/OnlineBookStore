package pl.urbanskilukasz.onlineLibrary.order.application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.Book;
import pl.urbanskilukasz.onlineLibrary.order.domain.OrderItem;
import pl.urbanskilukasz.onlineLibrary.order.domain.OrderStatus;
import pl.urbanskilukasz.onlineLibrary.order.domain.Recipient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class RichOrderTest {

    @Test
    public void calculateTotalPriceOfEmptyOrder() {
        //given
        RichOrder richOrder = new RichOrder(
                1L,
                OrderStatus.NEW,
                Collections.emptySet(),
                Recipient.builder().build(),
                LocalDateTime.now()
        );
        //when
        BigDecimal price = richOrder.totalPrice();
        //then
        Assertions.assertEquals(BigDecimal.ZERO, price);
    }

    @Test
    public void calculateTotalPrice(){
        //given
        Book book1 = new Book();
        book1.setPrice(new BigDecimal("12.50"));
        Book book2 = new Book();
        book2.setPrice(new BigDecimal("33.99"));
        Set<OrderItem> items = new HashSet<>(
                Arrays.asList(
                        new OrderItem(book1, 2),
                        new OrderItem(book2, 5)
                )
        );

        RichOrder richOrder = new RichOrder(
                1L,
                OrderStatus.NEW,
                items,
                Recipient.builder().build(),
                LocalDateTime.now()
        );
        //when
        BigDecimal price = richOrder.totalPrice();
        //then
        Assertions.assertEquals(new BigDecimal("194.95"), price);
    }

}