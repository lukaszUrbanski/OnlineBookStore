package pl.urbanskilukasz.onlineLibrary.catalog.application;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.urbanskilukasz.onlineLibrary.catalog.application.port.CatalogUseCase;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.Book;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.CatalogRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
class CatalogService implements CatalogUseCase {

    private final CatalogRepository repository;


    public CatalogService(@Qualifier("bestsellerCatalogRepository") CatalogRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Book> findByTitle(String title){
        return repository.findAll()
                .stream()
                .filter(book -> book.getTitle().contains(title))
                .collect(Collectors.toList());

    }

    @Override
    public List<Book> findByAuthor(String author){
        return repository.findAll()
                .stream()
                .filter(book -> book.getAuthor().contains(author))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findAll(){
        return null;
    }

    @Override
    public Optional<Book> findOneByTitleAndAuthor(String title, String author){
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id){ }

    @Override
    public void updateBook(){}
}
