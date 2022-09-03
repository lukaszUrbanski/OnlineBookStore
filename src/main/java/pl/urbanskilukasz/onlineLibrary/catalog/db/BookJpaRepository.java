package pl.urbanskilukasz.onlineLibrary.catalog.db;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.urbanskilukasz.onlineLibrary.catalog.application.domain.Book;

public interface BookJpaRepository extends JpaRepository<Book, Long> {
}
