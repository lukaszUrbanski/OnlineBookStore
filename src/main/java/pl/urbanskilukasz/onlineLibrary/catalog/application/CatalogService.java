package pl.urbanskilukasz.onlineLibrary.catalog.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.urbanskilukasz.onlineLibrary.catalog.application.port.CatalogUseCase;
import pl.urbanskilukasz.onlineLibrary.catalog.db.BookJpaRepository;
import pl.urbanskilukasz.onlineLibrary.catalog.application.domain.Book;
import pl.urbanskilukasz.onlineLibrary.uploads.application.port.UploadUseCase;
import pl.urbanskilukasz.onlineLibrary.uploads.application.port.UploadUseCase.SaveUploadCommand;
import pl.urbanskilukasz.onlineLibrary.uploads.domain.Upload;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class CatalogService implements CatalogUseCase {

    private final BookJpaRepository repository;
    private final UploadUseCase upload;

    @Override
    public List<Book> findByTitle(String title) {
        return repository.findAll()
                .stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }


    @Override
    public List<Book> findByAuthor(String author) {
        return repository.findAll()
                .stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findByAuthorAndTitle(String author, String title) {
        return repository.findAll()
                .stream()
//                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findAll() {
        return repository.findAll();
    }

    @Override
    public void addBook(CreateBookCommand command) {
        Book book = command.toBook();
        repository.save(book);
    }

    @Override
    public Optional<Book> findOneByTitleAndAuthor(String title, String author) {
        return repository.findAll()
                .stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .findFirst();
    }

    @Override
    public void removeById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void updateBookCover(UpdateBookCoverCommand command) {
        repository.findById(command.getId())
                .ifPresent(book -> {
                    Upload savedUpload = upload.save(new SaveUploadCommand(
                            command.getFileName(), command.getFile(), command.getContentType()
                    ));
                    book.setCoverId(savedUpload.getId());
                    repository.save(book);

                });
    }

    @Override
    public void removeBookCover(Long id) {
        repository.findById(id)
                .ifPresent(book -> {
                    if (book.getCoverId() != null) {
                        upload.removeById(book.getCoverId());
                        book.setCoverId(null);
                        repository.save(book);
                    }
                });
    }

    @Override
    public Optional<Book> findOneByTitle(String title) {
        return repository.findAll()
                .stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .findFirst();
    }

    @Override
    public Optional<Book> findById(Long id) {

        return repository.findById(id);
    }

    @Override
    public UpdateBookResponse updateBook(UpdateBookCommand command) {
        return repository.findById(command.getId())
                .map(book -> {
                    Book updatedBook = command.updateFields(book);
                    repository.save(updatedBook);
                    return UpdateBookResponse.SUCCESS;
                })
                .orElseGet(() -> new UpdateBookResponse(false, Arrays.asList("Book not found with id: " + command.getId())));
    }
}
