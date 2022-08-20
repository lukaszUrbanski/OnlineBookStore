package pl.urbanskilukasz.onlineLibrary.catalog.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.urbanskilukasz.onlineLibrary.catalog.application.port.CatalogUseCase;
import pl.urbanskilukasz.onlineLibrary.catalog.application.port.CatalogUseCase.CreateBookCommand;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.Book;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    ){
        if (title.isPresent() && author.isPresent()){
            return catalog.findByAuthorAndTitle(author.get(), title.get()); // todo: resolve problem with searching by title and author
        }else if (title.isPresent()) {
            return catalog.findByTitle(title.get());
        }else if (author.isPresent()){
            return catalog.findByAuthor(author.get());
        }
        return catalog.findAll();
       // return catalog.findAll().stream().limit(limit).collect(Collectors.toList());
    }

    @GetMapping(params = {"title"})
    @ResponseStatus(HttpStatus.OK)// default value
    public List<Book> findAllFiltered(@RequestParam String title){
        return catalog.findByTitle(title);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> findById(@PathVariable Long id){
        return catalog
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createBook(@Valid @RequestBody RestCreateBookCommand restCommand){
        catalog.addBook(restCommand.toCommand());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id){
        catalog.removeById(id);
    }

    @Data
    private static class RestCreateBookCommand{

        @NotBlank(message = "Please provide a title")
        private String title;

        @NotBlank(message = "Please provide an author")
        private String author;

        @NotNull
        private Integer year;

        @DecimalMin(value = "0.00", message =  "Price must be greater than or equal to 0.00")
        private BigDecimal price;

        CreateBookCommand toCommand(){
            return  new CreateBookCommand(title, author, year, price);
        }

    }
}
