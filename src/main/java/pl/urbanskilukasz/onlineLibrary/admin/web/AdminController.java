package pl.urbanskilukasz.onlineLibrary.admin.web;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.urbanskilukasz.onlineLibrary.catalog.application.port.CatalogInitializerUseCase;
import javax.transaction.Transactional;


@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    CatalogInitializerUseCase initializer;

    @PostMapping("/initialize")
    @Transactional
    public void initData(){
       initializer.initialize();
    }



}
