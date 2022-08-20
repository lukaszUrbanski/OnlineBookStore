package pl.urbanskilukasz.onlineLibrary.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.urbanskilukasz.onlineLibrary.catalog.application.port.CatalogUseCase;
import pl.urbanskilukasz.onlineLibrary.catalog.application.port.CatalogUseCase.CreateBookCommand;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.Book;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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
    public void createBook(@RequestBody RestCreateBookCommand restCommand){
        catalog.addBook(restCommand.toCommand());
    }

    @Data
    private static class RestCreateBookCommand{
        private String title;
        private String author;
        private Integer year;
        private BigDecimal price;

        CreateBookCommand toCommand(){
            return  new CreateBookCommand(title, author, year, price);
        }

    }
}
