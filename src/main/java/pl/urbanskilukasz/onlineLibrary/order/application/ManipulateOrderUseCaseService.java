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
        Long bookId = command.getBookId();
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Book with id" + bookId + " not exists"));
        int quantity = command.getQuantity();
        if (quantity <= 0)
            throw new IllegalArgumentException("Cannot order negative number of book");
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
    public UpdateStatusResponse updateOrderStatus(UpdateStatusCommand command) {
        return orderRepository
                .findById(command.getOrderId())
                .map(order -> {
                    if(!hasAccess(command, order)){
                        return UpdateStatusResponse.failure("Unauthorized!");
                    }
                    UpdateStatusResult result = order.updateStatus(command.getStatus());
                    if(result.isRevoked()) {
                        bookRepository.saveAll(revokeBooks(order.getItems()));
                    }
                    orderRepository.save(order);
                   return UpdateStatusResponse.success(order.getStatus());
                })
                .orElse(UpdateStatusResponse.failure("Order not found"));
    }

    private static boolean hasAccess(UpdateStatusCommand command, Order order) {
        String email = command.getEmail();
        return email.equalsIgnoreCase(order.getRecipient().getEmail()) ||
               email.equalsIgnoreCase("admin@example.org") ;
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
