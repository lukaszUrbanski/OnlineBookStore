package pl.urbanskilukasz.onlineLibrary.catalog.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import pl.urbanskilukasz.onlineLibrary.catalog.application.port.CatalogUseCase;
import pl.urbanskilukasz.onlineLibrary.catalog.application.port.CatalogUseCase.CreateBookCommand;
import pl.urbanskilukasz.onlineLibrary.catalog.db.AuthorJpaRepository;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.Author;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.Book;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class CatalogControllerIT {

    @Autowired
    CatalogController catalogController;

    @Autowired
    AuthorJpaRepository authorJpaRepository;

    @Autowired
    CatalogUseCase catalogUseCase;

    @Test
    public void getAllBooks() {
        //given
        Author geatz = authorJpaRepository.save(new Author("Brian Geatz"));
        Author bloch = authorJpaRepository.save(new Author("Joshua Bloch"));
        catalogUseCase.addBook(new CreateBookCommand(
                "Effective Java",
                Set.of(bloch.getId()),
                2005,
                new BigDecimal("99.90"),
                50L
        ));
        catalogUseCase.addBook(new CreateBookCommand(
                "Java Currency in Practice",
                Set.of(geatz.getId()),
                2004,
                new BigDecimal("129.90"),
                50L
        ));
        //when
        List<Book> all = catalogController.findAll(Optional.empty(), Optional.empty());
        //then
        Assertions.assertEquals(2 , all.size());
    }
    @Test
    public void getBookByAuthor() {
        //given
        Author geatz = authorJpaRepository.save(new Author("Brian Geatz"));
        Author bloch = authorJpaRepository.save(new Author("Joshua Bloch"));
        catalogUseCase.addBook(new CreateBookCommand(
                "Effective Java",
                Set.of(bloch.getId()),
                2005,
                new BigDecimal("99.90"),
                50L
        ));
        catalogUseCase.addBook(new CreateBookCommand(
                "Java Currency in Practice",
                Set.of(geatz.getId()),
                2004,
                new BigDecimal("129.90"),
                50L
        ));
        //when
        List<Book> all = catalogController.findAll(Optional.empty(), Optional.of("Bloch"));
        //then
        Assertions.assertEquals(1 , all.size());
        Assertions.assertEquals("Effective Java", all.get(0).getTitle());
    }

    @Test
    public void getBookByTitle() {
        //given
        Author geatz = authorJpaRepository.save(new Author("Brian Geatz"));
        Author bloch = authorJpaRepository.save(new Author("Joshua Bloch"));
        catalogUseCase.addBook(new CreateBookCommand(
                "Effective Java",
                Set.of(bloch.getId()),
                2005,
                new BigDecimal("99.90"),
                50L
        ));
        catalogUseCase.addBook(new CreateBookCommand(
                "Java Currency in Practice",
                Set.of(geatz.getId()),
                2004,
                new BigDecimal("129.90"),
                50L
        ));
        //when
        List<Book> all = catalogController.findAll(Optional.of("Java Currency in Practice"), Optional.empty());
        //then
        Assertions.assertEquals(1 , all.size());
        Assertions.assertEquals("Java Currency in Practice", all.get(0).getTitle());
    }
}