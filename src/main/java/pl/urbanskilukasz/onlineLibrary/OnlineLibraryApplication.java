package pl.urbanskilukasz.onlineLibrary;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.urbanskilukasz.onlineLibrary.catalog.application.CatalogController;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.Book;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.CatalogService;

import java.util.List;

@SpringBootApplication
public class OnlineLibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineLibraryApplication.class, args);
	}


}
