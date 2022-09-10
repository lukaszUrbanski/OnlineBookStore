package pl.urbanskilukasz.onlineLibrary.catalog.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookJpaRepository extends JpaRepository<Book, Long> {


    @Query("SELECT DISTINCT b FROM Book b JOIN FETCH b.authors")
    List<Book> findAllEager();
    @Query( " SELECT b FROM Book b JOIN b.authors a " +
            " WHERE " +
            " lower(a.firstName) LIKE lower(concat( '%', :name, '%')) " +
            " OR lower(a.lastName) LIKE lower(concat('%', :name, '%')) " )
    List<Book> findByAuthor(@Param("name") String name);

    List<Book> findBooksByTitleContainingIgnoreCase(String title);

    Optional<Book> findDistinctFirstByTitleContainingIgnoreCase(String title);

    @Query(" SELECT b FROM Book b JOIN b.authors a " +
            " WHERE " +
            " lower(a.firstName) LIKE lower(concat('%', :author, '%')) " +
            " OR lower(a.lastName) LIKE lower(concat('%', :author, '%')) " +
            " AND lower(b.title) LIKE lower(concat('%', :title, '%')) ")
    List<Book> findByAuthorAndTitle(@Param("author") String author,@Param("title") String title);
}
