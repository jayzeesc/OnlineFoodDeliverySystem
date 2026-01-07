import java.util.List;

public final class PendingCheckout {
    private final Restaurant restaurant;
    private final List<CartItem> cart;
    private final int urgency;

    public PendingCheckout(Restaurant restaurant, List<CartItem> cart, int urgency) {
        this.restaurant = restaurant;
        this.cart = cart;
        this.urgency = urgency;
    }

    public Restaurant getRestaurant() { return restaurant; }
    public List<CartItem> getCart() { return cart; }
    public int getUrgency() { return urgency; }
}
