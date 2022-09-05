package pl.urbanskilukasz.onlineLibrary;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.urbanskilukasz.onlineLibrary.catalog.application.port.CatalogUseCase;
import pl.urbanskilukasz.onlineLibrary.catalog.application.domain.Book;
import pl.urbanskilukasz.onlineLibrary.order.application.ManipulateOrderUseCaseService;
import pl.urbanskilukasz.onlineLibrary.order.application.QueryOrderService;
import pl.urbanskilukasz.onlineLibrary.order.application.port.ManipulateOrderUseCase.OrderItemCommand;
import pl.urbanskilukasz.onlineLibrary.order.application.port.ManipulateOrderUseCase.PlaceOrderCommand;
import pl.urbanskilukasz.onlineLibrary.order.application.port.ManipulateOrderUseCase.PlaceOrderResponse;
import pl.urbanskilukasz.onlineLibrary.order.domain.OrderItem;
import pl.urbanskilukasz.onlineLibrary.order.domain.Recipient;

import java.math.BigDecimal;
import java.util.List;

import static pl.urbanskilukasz.onlineLibrary.catalog.application.port.CatalogUseCase.*;

@Component
public class ApplicationStartUp implements CommandLineRunner {

    private final CatalogUseCase catalog;
    private final ManipulateOrderUseCaseService placeOrder;
    private final QueryOrderService queryOrder;

    private final String title;
    private final String author;

    public ApplicationStartUp(
            CatalogUseCase catalog,
            ManipulateOrderUseCaseService placeOrder,
            QueryOrderService queryOrder,
            @Value("${onlineLibrary.catalog.query.title:Lord}") String title,
            @Value("${onlineLibrary.catalog.query.author:Rowling}") String author
    ) {
        this.catalog = catalog;
        this.title = title;
        this.author = author;
        this.placeOrder = placeOrder;
        this.queryOrder = queryOrder;
    }

    @Override
    public void run(String... args) {
        dataInit();
        searchCatalog();
        placeOrder();
    }

    private void placeOrder() {
        Book panTadeusz = catalog.findOneByTitle("Pan Tadeusz").orElseThrow(() -> new IllegalStateException("Cannot find a book."));
        Book chlopi = catalog.findOneByTitle("Chłopi").orElseThrow(() -> new IllegalStateException("Cannot find a book."));

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
                .item(new OrderItem(panTadeusz.getId(), 16))
                .item(new OrderItem(chlopi.getId(), 7))
                .build();

        PlaceOrderResponse response = placeOrder.placeOrder(command);
        System.out.println("Placed order with id: " + response.getOrderId());

        queryOrder.findAll()
                .forEach(order -> {
                    System.out.println("GOT ORDER WITH TOTAL PRICE: " + order.totalPrice() + " DETAILS: " + order);
                });
    }

    private void searchCatalog() {
        findByTitle();
        findByAuthor();
        findAndUpdate();
        removeById();
        findByAuthor();
    }

    private void removeById() {
        catalog.removeById(5L);
    }

    private void dataInit() {
        catalog.addBook(new CreateBookCommand("Harry Potter and the Philosopher's Stone", "J. K. Rowling", 1997, new BigDecimal("34.99")));
        catalog.addBook(new CreateBookCommand("Harry Potter and the Camber of Secret", "J. K. Rowling", 1998, new BigDecimal("34.99")));
        catalog.addBook(new CreateBookCommand("Harry Potter and the Prisoner of Azcaban", "J. K. Rowling", 1999, new BigDecimal("34.99")));
        catalog.addBook(new CreateBookCommand("The Lord of the Rings: The Fellowship of the Ring", "J. R. R. Tolkien", 1954, new BigDecimal("24.99")));
        catalog.addBook(new CreateBookCommand("The Lord of the Rings: Two towers", "J. R. R. Tolkien", 1954, new BigDecimal("24.99")));
        catalog.addBook(new CreateBookCommand("The Lord of the Rings: The Return of the King", "J. R. R. Tolkien", 1954, new BigDecimal("24.99")));
        catalog.addBook(new CreateBookCommand("The Little Prince", "Antoine’a de Saint-Exupéry", 1943, new BigDecimal("20.99")));
        catalog.addBook(new CreateBookCommand("The Da Vinci Code", "Dan Brown", 2003, new BigDecimal("15.99")));
        catalog.addBook(new CreateBookCommand("Pan Tadeusz", "Adam Mickiewicz", 1834, new BigDecimal("23.99")));
        catalog.addBook(new CreateBookCommand("Ogniem i Mieczem", "Henryk Sienkiewicz", 1998, new BigDecimal("34.99")));
        catalog.addBook(new CreateBookCommand("Chłopi", "Włądysław Rejmont", 1999, new BigDecimal("13.99")));
        catalog.addBook(new CreateBookCommand("Pan Wołodyjowski", "Henryk Sienkiewicz", 1954, new BigDecimal("12.99")));
    }

    private void findByAuthor() {
        List<Book> booksByAuthor = catalog.findByAuthor(author);
        System.out.println("List of books by author:");
        booksByAuthor.forEach(System.out::println);
    }

    private void findByTitle() {
        List<Book> books = catalog.findByTitle(title);
        System.out.println("List of books by title:");
        books.forEach(System.out::println);
    }

    private void findAndUpdate() {
        System.out.println("Updating book ...");
        catalog.findOneByTitleAndAuthor("The Lord of the Rings: Two towers", "J. R. R. Tolkien")
                .ifPresent(book -> {
                    UpdateBookCommand command = UpdateBookCommand
                            .builder()
                            .id(book.getId())
                            .title("The Lord of the Rings: The Two Towers")
                            .build();

                    catalog.updateBook(command);
                });
    }

}
