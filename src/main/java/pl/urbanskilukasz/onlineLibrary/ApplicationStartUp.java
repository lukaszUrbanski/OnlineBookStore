package pl.urbanskilukasz.onlineLibrary;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.urbanskilukasz.onlineLibrary.catalog.application.CatalogController;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.Book;

import java.util.List;

@Component
public class ApplicationStartUp implements CommandLineRunner {

    private final CatalogController controller;

    public ApplicationStartUp(CatalogController catalogController) {
        this.controller = catalogController;
    }

    @Override
    public void run(String... args){
        //BookService bookService = new BookService();
        List<Book> books = controller.findByTitle("Harry");
        books.forEach(System.out::println);
    }
}
