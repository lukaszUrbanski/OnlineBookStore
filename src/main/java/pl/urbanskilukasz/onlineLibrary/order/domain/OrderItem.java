package pl.urbanskilukasz.onlineLibrary.order.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.urbanskilukasz.onlineLibrary.catalog.application.domain.Book;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderItem {

    @Id
    @GeneratedValue
    private Long id;
    private Long bookId;
    private Integer quantity;

    public OrderItem(Long bookId, Integer quantity) {
        this.bookId = bookId;
        this.quantity = quantity;
    }
}
