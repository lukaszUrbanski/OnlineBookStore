package pl.urbanskilukasz.onlineLibrary.service;

import org.springframework.stereotype.Service;
import pl.urbanskilukasz.onlineLibrary.Book;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final Map<Long, Book> storage = new ConcurrentHashMap<>();

    public BookService() {
        storage.put(1L, new Book(1L, "Harry Pootter i Komnata tajemnic", "J. R. Rowling", 1994));
        storage.put(2L, new Book(2L, "Harry Pootter i Kamień flozoficzny", "J. R. Rowling", 1998));
        storage.put(3L, new Book(3L, "Harry Pootter i Więzień Azkabanu", "J. R. Rowling", 2000));
    }

    public List<Book> findByTitle(String title){
        return storage.values()
                .stream()
                .filter(book -> book.title.startsWith(title))
                .collect(Collectors.toList());

    }
}
