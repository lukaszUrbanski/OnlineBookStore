package pl.urbanskilukasz.onlineLibrary.catalog.domain;

import org.springframework.stereotype.Repository;

import java.util.List;

public interface CatalogRepository {

    List<Book> findAll();

    void save(Book book);
}
