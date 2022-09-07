package pl.urbanskilukasz.onlineLibrary;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.urbanskilukasz.onlineLibrary.catalog.application.port.CatalogUseCase;
import pl.urbanskilukasz.onlineLibrary.catalog.db.AuthorJpaRepository;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.Author;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.Book;
import pl.urbanskilukasz.onlineLibrary.order.application.ManipulateOrderUseCaseService;
import pl.urbanskilukasz.onlineLibrary.order.application.QueryOrderService;
import pl.urbanskilukasz.onlineLibrary.order.application.port.ManipulateOrderUseCase.PlaceOrderCommand;
import pl.urbanskilukasz.onlineLibrary.order.application.port.ManipulateOrderUseCase.PlaceOrderResponse;
import pl.urbanskilukasz.onlineLibrary.order.domain.OrderItem;
import pl.urbanskilukasz.onlineLibrary.order.domain.Recipient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static pl.urbanskilukasz.onlineLibrary.catalog.application.port.CatalogUseCase.*;

@Component
@AllArgsConstructor
public class ApplicationStartUp implements CommandLineRunner {

    private final CatalogUseCase catalog;
    private final ManipulateOrderUseCaseService placeOrder;
    private final QueryOrderService queryOrder;

    private final AuthorJpaRepository authorRepository;
    @Override
    public void run(String... args) {
        dataInit();
        placeOrder();
    }

    private void placeOrder() {
        Book effectiveJava = catalog.findOneByTitle("Effective Java").orElseThrow(() -> new IllegalStateException("Cannot find a book."));
        Book javaPuzzlers = catalog.findOneByTitle("Java Puzzlers").orElseThrow(() -> new IllegalStateException("Cannot find a book."));

        Recipient recipient = Recipient
                .builder()
                .name(" Jan Kowalski")
                .phone("123-234-456")
                .city("Toruń")
                .street("Długa 123")
                .zipCode("87-100")
                .email("jan@example.pl")
                .build();

        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient)
                .item(new OrderItem(effectiveJava.getId(), 16))
                .item(new OrderItem(javaPuzzlers.getId(), 7))
                .build();

        PlaceOrderResponse response = placeOrder.placeOrder(command);
        System.out.println("Placed order with id: " + response.getOrderId());

        queryOrder.findAll()
                .forEach(order -> {
                    System.out.println("GOT ORDER WITH TOTAL PRICE: " + order.totalPrice() + " DETAILS: " + order);
                });
    }

    private void dataInit() {

        Author joshua = new Author("Joshua", "Bloch");
        Author neal = new Author("Neal", "Gafter");
        authorRepository.save(joshua);
        authorRepository.save(neal);

        CreateBookCommand effectiveJava = new CreateBookCommand(
                "Effective Java",
                Set.of(neal.getId()),
                2005,
                new BigDecimal("79.99")
        );
        CreateBookCommand javaPuzzlers = new CreateBookCommand(
                "Java Puzzlers",
                Set.of(joshua.getId(), neal.getId()),
                2013,
                new BigDecimal("99.99")
        );

        catalog.addBook(effectiveJava);
        catalog.addBook(javaPuzzlers);

//        catalog.addBook(new CreateBookCommand("Harry Potter and the Camber of Secret", null,1998, new BigDecimal("34.99")));
//        catalog.addBook(new CreateBookCommand("Harry Potter and the Prisoner of Azcaban", null,1999, new BigDecimal("34.99")));
//        catalog.addBook(new CreateBookCommand("The Lord of the Rings: The Fellowship of the Ring", null,1954, new BigDecimal("24.99")));
//        catalog.addBook(new CreateBookCommand("The Lord of the Rings: Two towers",null, 1954, new BigDecimal("24.99")));
//        catalog.addBook(new CreateBookCommand("The Lord of the Rings: The Return of the King", null, 1954, new BigDecimal("24.99")));
//        catalog.addBook(new CreateBookCommand("The Little Prince",null,  1943, new BigDecimal("20.99")));
//        catalog.addBook(new CreateBookCommand("The Da Vinci Code", null,2003, new BigDecimal("15.99")));
//        catalog.addBook(new CreateBookCommand("Pan Tadeusz", null,1834, new BigDecimal("23.99")));
//        catalog.addBook(new CreateBookCommand("Ogniem i Mieczem", null,1998, new BigDecimal("34.99")));
//        catalog.addBook(new CreateBookCommand("Chłopi",null, 1999, new BigDecimal("13.99")));
//        catalog.addBook(new CreateBookCommand("Pan Wołodyjowski", null, 1954, new BigDecimal("12.99")));
    }

}
