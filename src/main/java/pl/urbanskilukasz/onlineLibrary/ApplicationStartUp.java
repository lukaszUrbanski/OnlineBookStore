package pl.urbanskilukasz.onlineLibrary;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.urbanskilukasz.onlineLibrary.catalog.application.CatalogController;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.Book;

import java.util.List;

@Component
public class ApplicationStartUp implements CommandLineRunner {

    private final CatalogController controller;
    private final String title;

    public ApplicationStartUp(
            CatalogController catalogController,
            @Value("${onlineLibrary.catalog.query}") String title
    ) {
        this.controller = catalogController;
        this.title = title;
    }

    @Override
    public void run(String... args) {
        //BookService bookService = new BookService();
        List<Book> books = controller.findByTitle(title);
        books.forEach(System.out::println);
    }
}
