import java.util.Comparator;
import java.util.PriorityQueue;

public final class OrderQueue {
    private static final OrderQueue INSTANCE = new OrderQueue();
    private final PriorityQueue<Order> pq = new PriorityQueue<>(Comparator.comparingInt(Order::getUrgency).reversed());
    private OrderQueue() {}
    public static OrderQueue getInstance() { return INSTANCE; }
    public void add(Order o) { if (o != null) pq.add(o); }
    public Order next() { return pq.poll(); }
    public int size() { return pq.size(); }
}
