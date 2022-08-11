package pl.urbanskilukasz.onlineLibrary;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.urbanskilukasz.onlineLibrary.service.BookService;

import java.util.List;

@SpringBootApplication
public class OnlineLibraryApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(OnlineLibraryApplication.class, args);
	}

	private final BookService bookService;

	public OnlineLibraryApplication(BookService bookService) {
		this.bookService = bookService;
	}

	@Override
	public void run(String... args){
		//BookService bookService = new BookService();
		List<Book> books = bookService.findByTitle("Harry");
		books.forEach(System.out::println);
	}
}
