package pl.urbanskilukasz.onlineLibrary.catalog.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.urbanskilukasz.onlineLibrary.catalog.application.port.CatalogUseCase;
import pl.urbanskilukasz.onlineLibrary.catalog.db.AuthorJpaRepository;
import pl.urbanskilukasz.onlineLibrary.catalog.db.BookJpaRepository;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.Author;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.Book;
import pl.urbanskilukasz.onlineLibrary.uploads.application.port.UploadUseCase;
import pl.urbanskilukasz.onlineLibrary.uploads.application.port.UploadUseCase.SaveUploadCommand;
import pl.urbanskilukasz.onlineLibrary.uploads.domain.Upload;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class CatalogService implements CatalogUseCase {

    private final BookJpaRepository repository;
    private final AuthorJpaRepository authorRepository;
    private final UploadUseCase upload;

    @Override
    public List<Book> findByTitle(String title) {
        return repository.findBooksByTitleContainingIgnoreCase(title);
    }


    @Override
    public List<Book> findByAuthor(String author) {
        return repository.findByAuthor(author);
    }

    @Override
    public List<Book> findByAuthorAndTitle(String author, String title) {


        return repository.findByAuthorAndTitle(author, title);
    }

    @Override
    public List<Book> findAll() {
        return repository.findAllEager();
    }

    @Override
    @Transactional
    public void addBook(CreateBookCommand command) {
        Book book = toBook(command);
        repository.save(book);
    }
    private Book toBook(CreateBookCommand command){
        Book book = new Book(command.getTitle(), command.getYear(), command.getPrice());
        Set<Author> authors = fetchAuthors(command.getAuthors());
        updateAuthors(book, authors);
        return book;
    }

    private static void updateAuthors(Book book, Set<Author> authors) {
        book.removeAuthors();
        authors.forEach(book::addAuthor);
    }

    private Set<Author> fetchAuthors(Set<Long> authorsId){
        return authorsId.stream()
                .map(authorId -> authorRepository
                        .findById(authorId)
                        .orElseThrow(() -> new IllegalArgumentException("Unable to find an author with ID: " + authorId)))
                .collect(Collectors.toSet());
//        Set<Long> authorIds = command.getAuthors();
//        Set<Author> authors = Set.copyOf(authorRepository.findAllById(authorIds));

    }

    @Override
    public Optional<Book> findOneByTitleAndAuthor(String title, String author) {
        return repository.findAll()
                .stream()
//                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
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
        return repository.findDistinctFirstByTitleContainingIgnoreCase(title);
    }

    @Override
    public Optional<Book> findById(Long id) {

        return repository.findById(id);
    }

    @Override
    public UpdateBookResponse updateBook(UpdateBookCommand command) {
        return repository.findById(command.getId())
                .map(book -> {
                    Book updatedBook = updateFields(command, book);
                    repository.save(updatedBook);
                    return UpdateBookResponse.SUCCESS;
                })
                .orElseGet(() -> new UpdateBookResponse(false, Arrays.asList("Book not found with id: " + command.getId())));
    }
    private Book updateFields(UpdateBookCommand command, Book book){
        if(command.getTitle() != null){
            book.setTitle(command.getTitle());
        }
        if(command.getAuthors() != null && command.getAuthors().size() > 0){
            updateAuthors(book, fetchAuthors(command.getAuthors()));
        }
        if(command.getYear() != null){
            book.setYear(command.getYear());
        }
        if(command.getPrice() != null){
            book.setPrice(command.getPrice());
        }
        return book;
    }
}

