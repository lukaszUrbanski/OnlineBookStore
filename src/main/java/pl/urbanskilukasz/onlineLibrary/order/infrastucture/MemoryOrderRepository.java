package pl.urbanskilukasz.onlineLibrary.order.infrastucture;

import org.springframework.stereotype.Repository;
import pl.urbanskilukasz.onlineLibrary.order.domain.Order;
import pl.urbanskilukasz.onlineLibrary.order.domain.OrderRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MemoryOrderRepository implements OrderRepository {

    private final Map<Long, Order> storage = new ConcurrentHashMap<>();
    private final AtomicLong ID_NEXT_VALUE = new AtomicLong(0L);


    @Override
    public void save(Order order) {
        if (order.getId() != null) {
            storage.put(order.getId(), order);
        }else {
            long nextId = nextId();
            order.setId(nextId);
            order.setCreatedAt(LocalDateTime.now());
            storage.put(nextId, order);
        }
    }

    @Override
    public List<Order> findAll(){
        return new ArrayList<>(storage.values());
    }

    private long nextId() {
        return ID_NEXT_VALUE.getAndIncrement();
    }
}
