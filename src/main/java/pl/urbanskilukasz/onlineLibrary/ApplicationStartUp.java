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
    private final String author;

    public ApplicationStartUp(
            CatalogController catalogController,
            @Value("${onlineLibrary.catalog.query.title:Lord}") String title,
            @Value("${onlineLibrary.catalog.query.author:Rowling}") String author
    ) {
        this.controller = catalogController;
        this.title = title;
        this.author = author;
    }

    @Override
    public void run(String... args) {
        //BookService bookService = new BookService();
        List<Book> books = controller.findByTitle(title);
        System.out.println("List of books by title:");
        books.forEach(System.out::println);

        List<Book> booksByAuthor = controller.findByAuthor(author);
        System.out.println("List of books by author:");
        booksByAuthor.forEach(System.out::println);
    }
}
