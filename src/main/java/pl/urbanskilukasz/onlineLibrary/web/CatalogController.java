package pl.urbanskilukasz.onlineLibrary.web;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.urbanskilukasz.onlineLibrary.catalog.application.port.CatalogUseCase;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.Book;

import java.util.List;


@RequestMapping("/catalog")
@RestController
@AllArgsConstructor
public class CatalogController {
    private final CatalogUseCase catalog;


    @GetMapping
    public List<Book> findAll(){
        return catalog.findAll();
    }
}
