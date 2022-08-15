package pl.urbanskilukasz.onlineLibrary;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.urbanskilukasz.onlineLibrary.catalog.application.port.CatalogUseCase;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.Book;

import java.util.List;

import static pl.urbanskilukasz.onlineLibrary.catalog.application.port.CatalogUseCase.*;

@Component
public class ApplicationStartUp implements CommandLineRunner {

    private final CatalogUseCase catalog;
    private final String title;
    private final String author;

    public ApplicationStartUp(
                CatalogUseCase catalog,
            @Value("${onlineLibrary.catalog.query.title:Lord}") String title,
            @Value("${onlineLibrary.catalog.query.author:Rowling}") String author
    ) {
        this.catalog = catalog;
        this.title = title;
        this.author = author;
    }

    @Override
    public void run(String... args) {
        dataInit();
        findByTitle();
        findByAuthor();
        findAndUpdate();
        findByAuthor();
    }
    private void dataInit(){
        catalog.addBook(new CreateBookCommand("Harry Pootter and the Philosopher's Stone", "J. K. Rowling", 1997));
        catalog.addBook(new CreateBookCommand("Harry Pootter and the Camber of Secret", "J. K. Rowling", 1998));
        catalog.addBook(new CreateBookCommand("Harry Pootter and the Prisoner of Azcaban", "J. K. Rowling", 1999));
        catalog.addBook(new CreateBookCommand("The Lord of the Rings: The Fellowship of the Ring", "J. R. R. Tolkien", 1954));
        catalog.addBook(new CreateBookCommand("The Lord of the Rings: Two towers", "J. R. R. Tolkien", 1954));
        catalog.addBook(new CreateBookCommand("The Lord of the Rings: The Return of the King", "J. R. R. Tolkien", 1954));
        catalog.addBook(new CreateBookCommand("The Little Prince", "Antoine’a de Saint-Exupéry", 1943));
        catalog.addBook(new CreateBookCommand("The Da Vinci Code","Dan Brown",  2003));
        catalog.addBook(new CreateBookCommand("Pan Tadeusz", "Adam Mickiewicz", 1834));
        catalog.addBook(new CreateBookCommand("Ogniem i Mieczem", "Henryk Sienkiewicz", 1998));
        catalog.addBook(new CreateBookCommand("Chłopi", "Włądysław Rejmont", 1999));
        catalog.addBook(new CreateBookCommand("Pan Wołodyjowski", "Henryk Sienkiewicz", 1954));
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

    private void findAndUpdate(){
        System.out.println("Updating book ...");
        catalog.findOneByTitleAndAuthor("The Lord of the Rings: Two towers","J. R. R. Tolkien" )
                .ifPresent(book -> {
                    UpdateBookCommand command= new UpdateBookCommand(
                            book.getId(),
                            "The Lord of the Rings: The Two Towers",
                            book.getAuthor(),
                            book.getYear()
                    );
                    catalog.updateBook(command);
                });

    }
}
