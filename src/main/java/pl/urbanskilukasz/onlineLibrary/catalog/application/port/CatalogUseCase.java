package pl.urbanskilukasz.onlineLibrary.catalog.application.port;

import lombok.Value;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.Book;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

public interface CatalogUseCase {
    List<Book> findByTitle(String title);

    List<Book> findByAuthor(String author);

    List<Book> findAll();

    void addBook(CreateBookCommand command);

    Optional<Book> findOneByTitleAndAuthor(String title, String author);

    UpdateBookResponse updateBook(UpdateBookCommand command);

    void deleteById(Long id);

    @Value
    class CreateBookCommand{
        String title;
        String author;
        Integer year;
    }

    @Value
    class UpdateBookCommand{
        Long id;
        String title;
        String author;
        Integer year;
    }

    @Value
    class UpdateBookResponse{
        public static  UpdateBookResponse SUCCESS = new UpdateBookResponse(true, emptyList());

        boolean success;
        List<String> errors;
    }
}
