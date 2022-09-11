package pl.urbanskilukasz.onlineLibrary.catalog.application.port;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.Book;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.emptyList;

public interface CatalogUseCase {
    void addBook(CreateBookCommand command);

    List<Book> findAll();

    List<Book> findByTitle(String title);

    List<Book> findByAuthor(String author);

    List<Book> findByAuthorAndTitle(String author, String title);

    Optional<Book> findById(Long id);

    Optional<Book> findOneByTitleAndAuthor(String title, String author);

    Optional<Book> findOneByTitle(String title);

    UpdateBookResponse updateBook(UpdateBookCommand command);

    void removeById(Long id);

    void updateBookCover(UpdateBookCoverCommand command);

    void removeBookCover(Long id);


    @Value
    class UpdateBookCoverCommand{
        Long id;
        byte[] file;
        String contentType;
        String fileName;
    }
    @Value
    class CreateBookCommand{
        String title;
        Set<Long> authors;
        Integer year;
        BigDecimal price;
        Long available;

    }

    @Value
    @Builder
    @AllArgsConstructor
    class UpdateBookCommand {
        Long id;
        String title;
        Set<Long> authors;
        Integer year;
        BigDecimal price;
        Long available;

    }

    @Value
    class UpdateBookResponse{
        public static  UpdateBookResponse SUCCESS = new UpdateBookResponse(true, emptyList());

        boolean success;
        List<String> errors;
    }
}
