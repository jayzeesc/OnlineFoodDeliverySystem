public final class MenuItem {
    private final int id;
    private final int restaurantId;
    private final String name;
    private final double price;

    public MenuItem(int id, int restaurantId, String name, double price) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.name = name;
        this.price = price;
    }

    public int getId() { return id; }
    public int getRestaurantId() { return restaurantId; }
    public String getName() { return name; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return name + "  ₱" + String.format("%.2f", price);
    }
}
