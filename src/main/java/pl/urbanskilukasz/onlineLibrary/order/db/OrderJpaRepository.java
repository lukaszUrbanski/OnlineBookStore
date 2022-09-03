package pl.urbanskilukasz.onlineLibrary.order.db;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.urbanskilukasz.onlineLibrary.order.domain.Order;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
}
