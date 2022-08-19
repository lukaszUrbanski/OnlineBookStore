package pl.urbanskilukasz.onlineLibrary.catalog.application.port;

import lombok.Builder;
import lombok.Value;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.Book;

import java.math.BigDecimal;
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

    void removeById(Long id);

    Optional<Book> findOneByTitle(String title);

    Optional<Book> findById(Long id);

    @Value
    class CreateBookCommand{
        String title;
        String author;
        Integer year;
        BigDecimal price;

        public Book toBook(){
            return  new Book(title, author, year, price);
        }
    }

    @Value
    @Builder
    class UpdateBookCommand{
        Long id;
        String title;
        String author;
        Integer year;

       public Book updateFields(Book book){
            if(title != null){
                 book.setTitle(title);
            }
            if(author != null){
                book.setAuthor(author);
            }
            if(year != null){
                book.setYear(year);
            }
            return book;
        }
    }

    @Value
    class UpdateBookResponse{
        public static  UpdateBookResponse SUCCESS = new UpdateBookResponse(true, emptyList());

        boolean success;
        List<String> errors;
    }
}
