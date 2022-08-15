package pl.urbanskilukasz.onlineLibrary.catalog.application.port;

import pl.urbanskilukasz.onlineLibrary.catalog.domain.Book;

import java.util.List;
import java.util.Optional;

public interface CatalogUseCase {
    List<Book> findByTitle(String title);

    List<Book> findByAuthor(String author);

    List<Book> findAll();

    Optional<Book> findOneByTitleAndAuthor(String title, String author);

    void deleteById(Long id);

    void updateBook();
}
