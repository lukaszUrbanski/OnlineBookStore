package pl.urbanskilukasz.onlineLibrary.order.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.urbanskilukasz.onlineLibrary.catalog.db.BookJpaRepository;
import pl.urbanskilukasz.onlineLibrary.catalog.domain.Book;
import pl.urbanskilukasz.onlineLibrary.order.application.port.ManipulateOrderUseCase;
import pl.urbanskilukasz.onlineLibrary.order.db.OrderJpaRepository;
import pl.urbanskilukasz.onlineLibrary.order.db.RecipientJpaRepository;
import pl.urbanskilukasz.onlineLibrary.order.domain.*;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ManipulateOrderUseCaseService implements ManipulateOrderUseCase {
    private final OrderJpaRepository orderRepository;
    private final BookJpaRepository bookRepository;
    private final RecipientJpaRepository recipientRepository;

    @Override
    public PlaceOrderResponse placeOrder(PlaceOrderCommand command) {

        Set<OrderItem> items = command
                .getItems()
                .stream()
                .map(this::toOrderItem)
                .collect(Collectors.toSet());

        Order order = Order
                .builder()
                .items(items)
                .recipient(getOrCreateRecipient(command.getRecipient()))
                .build();
        Order save = orderRepository.save(order);
        bookRepository.saveAll(reduceBooks(items));
        return PlaceOrderResponse.success(save.getId());
    }

    private Recipient getOrCreateRecipient(Recipient recipient){
        return recipientRepository
                .findByEmailIgnoreCase(recipient.getEmail())
                .orElse(recipient);
    }
    private Set<Book> reduceBooks(Set<OrderItem> items) {
        return items
                .stream()
                .map(item -> {
                    Book book = item.getBook();
                    book.setAvailable(book.getAvailable() - item.getQuantity());
                    return book;
                })
                .collect(Collectors.toSet());
    }

    private OrderItem toOrderItem(OrderItemCommand command) {
        Book book = bookRepository.getById(command.getBookId());
        int quantity = command.getQuantity();
        if (book.getAvailable() >= quantity){
            return new OrderItem(book, quantity);
        }
        throw new IllegalArgumentException("Too many copies of book " + book.getId() + " requested: " + quantity + " of " + book.getAvailable() + " available ");
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public UpdateStatusResponse updateOrderStatus(Long id, OrderStatus status) {
        return orderRepository
                .findById(id)
                .map(order -> {
                    UpdateStatusResult result = order.updateStatus(status);
                    if(result.isRevoked()) {
                        bookRepository.saveAll(revokeBooks(order.getItems()));
                    }
                    orderRepository.save(order);
                   return UpdateStatusResponse.success(order.getStatus());
                })
                .orElse(UpdateStatusResponse.failure(Error.NOT_FOUND));
    }
    private Set<Book> revokeBooks(Set<OrderItem> items) {
        return items
                .stream()
                .map(item -> {
                    Book book = item.getBook();
                    book.setAvailable(book.getAvailable() + item.getQuantity());
                    return book;
                })
                .collect(Collectors.toSet());
    }

}
