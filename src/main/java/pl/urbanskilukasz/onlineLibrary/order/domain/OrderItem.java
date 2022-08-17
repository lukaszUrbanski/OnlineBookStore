package pl.urbanskilukasz.onlineLibrary.order.domain;

import lombok.Data;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.Book;

@Data
public class OrderItem {
    private Book book;
    private Integer quantity;
}
