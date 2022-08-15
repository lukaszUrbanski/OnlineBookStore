package pl.urbanskilukasz.onlineLibrary.catalog.application;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.urbanskilukasz.onlineLibrary.catalog.application.port.CatalogUseCase;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.Book;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.CatalogRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class CatalogService implements CatalogUseCase {

    private final CatalogRepository repository;

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
        return repository.findAll();
    }

    @Override
    public void addBook(CreateBookCommand command) {
        Book book = new Book(command.getTitle(), command.getAuthor(), command.getYear() );
        repository.save(book);
    }

    @Override
    public Optional<Book> findOneByTitleAndAuthor(String title, String author){
        return repository.findAll()
                .stream()
                .filter(book -> book.getAuthor().contains(author))
                .filter(book -> book.getTitle().contains(title))
                .findFirst();
    }

    @Override
    public void deleteById(Long id){ }

    @Override
    public UpdateBookResponse updateBook(UpdateBookCommand command){
       return repository.findById(command.getId())
               .map(book -> {
                   book.setTitle(command.getTitle());
                   book.setAuthor(command.getAuthor());
                   book.setYear(command.getYear());
                   repository.save(book);
                   return UpdateBookResponse.SUCCESS;
               })
                .orElseGet(()-> new UpdateBookResponse(false, Arrays.asList("Book not found with id: " + command.getId())));
    }
}
