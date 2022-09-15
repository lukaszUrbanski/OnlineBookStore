package pl.urbanskilukasz.onlineLibrary.catalog.application;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import pl.urbanskilukasz.onlineLibrary.catalog.application.port.CatalogInitializerUseCase;
import pl.urbanskilukasz.onlineLibrary.catalog.application.port.CatalogUseCase;
import pl.urbanskilukasz.onlineLibrary.catalog.db.AuthorJpaRepository;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.Author;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.Book;
import pl.urbanskilukasz.onlineLibrary.order.application.ManipulateOrderUseCaseService;
import pl.urbanskilukasz.onlineLibrary.order.application.QueryOrderService;
import pl.urbanskilukasz.onlineLibrary.order.application.port.ManipulateOrderUseCase;
import pl.urbanskilukasz.onlineLibrary.order.domain.Recipient;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class CatalogInitializerUseCaseService implements CatalogInitializerUseCase {

    private final CatalogUseCase catalog;
    private final ManipulateOrderUseCaseService placeOrder;
    private final QueryOrderService queryOrder;
    private final AuthorJpaRepository authorRepository;

    @Override
    @Transactional
    public void initialize() {
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

        ManipulateOrderUseCase.PlaceOrderCommand command = ManipulateOrderUseCase.PlaceOrderCommand
                .builder()
                .recipient(recipient)
                .item(new ManipulateOrderUseCase.OrderItemCommand(effectiveJava.getId(), 16))
                .item(new ManipulateOrderUseCase.OrderItemCommand(javaPuzzlers.getId(), 7))
                .build();

        ManipulateOrderUseCase.PlaceOrderResponse response = placeOrder.placeOrder(command);
        log.info("Placed order with id: " + response.getOrderId());

        queryOrder.findAll()
                .forEach(order -> {
                    log.info("GOT ORDER WITH TOTAL PRICE: " + order.totalPrice() + " DETAILS: " + order);
                });
    }

    private void dataInit() {
        ClassPathResource resource = new ClassPathResource("books.csv");
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            CsvToBean<CsvBook> build = new CsvToBeanBuilder<CsvBook>(reader)
                    .withType(CsvBook.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            build.stream().forEach(this::initBook);
        } catch (IOException e) {
            throw new IllegalStateException("Filed to parse Csv file", e);
        }
    }

    private void initBook(CsvBook csvBook) {
        CatalogUseCase.CreateBookCommand command = new CatalogUseCase.CreateBookCommand(
                csvBook.title,
                Set.of(),
                csvBook.year,
                csvBook.amount,
                50L
        );
        catalog.addBook(command);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CsvBook {
        @CsvBindByName
        private String title;
        @CsvBindByName
        private String authors;
        @CsvBindByName
        private Integer year;
        @CsvBindByName
        private BigDecimal amount;
        @CsvBindByName
        private String thumbnail;

    }


}
