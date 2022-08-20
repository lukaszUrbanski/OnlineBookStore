package pl.urbanskilukasz.onlineLibrary.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.urbanskilukasz.onlineLibrary.catalog.application.port.CatalogUseCase;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.Book;

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
            @RequestParam Optional<String> author,
            @RequestParam(defaultValue = "10") int limit
    ){
        if (title.isPresent() && author.isPresent()){
            return catalog.findByAuthorAndTitle(author.get(), title.get()); // todo: resolve problem with searching by title and author
        }else if (title.isPresent()) {
            return catalog.findByTitle(title.get());
        }else if (author.isPresent()){
            return catalog.findByAuthor(author.get());
        }
        return catalog.findAll().stream().limit(limit).collect(Collectors.toList());
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
}
