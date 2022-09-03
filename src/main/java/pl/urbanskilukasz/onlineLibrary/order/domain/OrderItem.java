package pl.urbanskilukasz.onlineLibrary.order.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.urbanskilukasz.onlineLibrary.catalog.application.domain.Book;

@Data
@AllArgsConstructor
public class OrderItem {
    private Book book;
    private Integer quantity;
}
