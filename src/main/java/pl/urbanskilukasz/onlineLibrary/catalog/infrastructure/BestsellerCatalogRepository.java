package pl.urbanskilukasz.onlineLibrary.catalog.infrastructure;

import org.springframework.stereotype.Repository;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.Book;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.CatalogRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class BestsellerCatalogRepository implements CatalogRepository {
    private final Map<Long, Book> storage = new ConcurrentHashMap<>();

    public BestsellerCatalogRepository() {
        storage.put(1L, new Book(1L, "Harry Pootter and the Philosopher's Stone", "J. K. Rowling", 1997));
        storage.put(2L, new Book(2L, "Harry Pootter and the Camber of Secret", "J. K. Rowling", 1998));
        storage.put(3L, new Book(3L, "Harry Pootter and the Prisoner of Azcaban", "J. K. Rowling", 1999));
        storage.put(4L, new Book(4L, "The Lord of the Rings: The Fellowship of the Ring", "J. R. R. Tolkien", 1954));
        storage.put(5L, new Book(5L, "The Lord of the Rings: Two towers", "J. R. R. Tolkien", 1954));
        storage.put(6L, new Book(6L, "The Lord of the Rings: The Return of the King", "J. R. R. Tolkien", 1954));
        storage.put(6L, new Book(7L, "The Little Prince", "Antoine’a de Saint-Exupéry", 1943));
        storage.put(6L, new Book(8L,  "The Da Vinci Code","Dan Brown",  2003));


    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(storage.values());
    }
}
