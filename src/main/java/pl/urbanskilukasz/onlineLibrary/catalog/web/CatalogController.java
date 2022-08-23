package pl.urbanskilukasz.onlineLibrary.catalog.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import pl.urbanskilukasz.onlineLibrary.catalog.application.port.CatalogUseCase;
import pl.urbanskilukasz.onlineLibrary.catalog.application.port.CatalogUseCase.CreateBookCommand;
import pl.urbanskilukasz.onlineLibrary.catalog.application.port.CatalogUseCase.UpdateBookCommand;
import pl.urbanskilukasz.onlineLibrary.catalog.application.port.CatalogUseCase.UpdateBookCoverCommand;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.Book;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@RequestMapping("/catalog")
@RestController
@AllArgsConstructor
public class CatalogController {
    private final CatalogUseCase catalog;


    @GetMapping
    @ResponseStatus(HttpStatus.OK)// default value
    public List<Book> findAll(
            @RequestParam Optional<String> title,
            @RequestParam Optional<String> author
            // @RequestParam(defaultValue = "10") int limit
    ) {
        if (title.isPresent() && author.isPresent()) {
            return catalog.findByAuthorAndTitle(author.get(), title.get());
        } else if (title.isPresent()) {
            return catalog.findByTitle(title.get());
        } else if (author.isPresent()) {
            return catalog.findByAuthor(author.get());
        }
        return catalog.findAll();
        // return catalog.findAll().stream().limit(limit).collect(Collectors.toList());
    }

    @GetMapping(params = {"title"})
    @ResponseStatus(HttpStatus.OK)// default value
    public List<Book> findAllFiltered(@RequestParam String title) {
        return catalog.findByTitle(title);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> findById(@PathVariable Long id) {
        return catalog
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createBook(@Valid @RequestBody CatalogController.RestBookCommand restCommand) {
        catalog.addBook(restCommand.toCommand());
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateBook(@PathVariable Long id, @RequestBody RestBookCommand restCommand) {
        CatalogUseCase.UpdateBookResponse response = catalog.updateBook(restCommand.toUpdateCommand(id));
        if (!response.isSuccess()) {
            String message = String.join(", ", response.getErrors());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    @PutMapping("/{id}/cover")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void addBookCover(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        catalog.updateBookCover(new UpdateBookCoverCommand(
                id,
                file.getBytes(),
                file.getContentType(),
                file.getOriginalFilename()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        catalog.removeById(id);
    }

    @DeleteMapping("/{id}/cover")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeBookCover(@PathVariable Long id){
        catalog.removeBookCover(id);
    }
    @Data
    private static class RestBookCommand {

        @NotBlank(message = "Please provide a title")
        private String title;

        @NotBlank(message = "Please provide an author")
        private String author;

        @NotNull
        private Integer year;

        @DecimalMin(value = "0.00", message = "Price must be greater than or equal to 0.00")
        private BigDecimal price;

        CreateBookCommand toCommand() {
            return new CreateBookCommand(title, author, year, price);
        }

        UpdateBookCommand toUpdateCommand(Long id) {
            return new UpdateBookCommand(id, title, author, year, price);
        }
    }
}
