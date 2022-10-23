package pl.urbanskilukasz.onlineLibrary.catalog.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.urbanskilukasz.onlineLibrary.catalog.application.port.CatalogUseCase;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.Book;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CatalogController.class})
class CatalogControllerTest {

    @MockBean
    CatalogUseCase catalogUseCase;

    @Autowired
    CatalogController controller;

    @Test
    public void shouldGetAllBooks(){
        //given
        Book effectiveJava = new Book("Effective Java", 2005, new BigDecimal("99.90"), 50L);
        Book concurrency = new Book("Java Concurrency", 2006, new BigDecimal("129.90"), 50L);

        Mockito.when(catalogUseCase.findAll()).thenReturn(List.of(effectiveJava, concurrency));
        //when
        List<Book> all = controller.findAll(Optional.empty(), Optional.empty());
        //then
        Assertions.assertEquals(2, all.size());
    }

}