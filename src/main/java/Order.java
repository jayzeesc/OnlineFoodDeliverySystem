public final class Order {
    private final int id;
    private final int userId;
    private final int restaurantId;
    private final String status;
    private final String createdAt;
    private final int etaMinutes;
    private final double deliveryFee;
    private final double subtotal;
    private final double total;
    private final int urgency;

    public Order(int id, int userId, int restaurantId, String status, String createdAt, int etaMinutes, double deliveryFee, double subtotal, double total, int urgency) {
        this.id = id;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.status = status;
        this.createdAt = createdAt;
        this.etaMinutes = etaMinutes;
        this.deliveryFee = deliveryFee;
        this.subtotal = subtotal;
        this.total = total;
        this.urgency = urgency;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getRestaurantId() { return restaurantId; }
    public String getStatus() { return status; }
    public String getCreatedAt() { return createdAt; }
    public int getEtaMinutes() { return etaMinutes; }
    public double getDeliveryFee() { return deliveryFee; }
    public double getSubtotal() { return subtotal; }
    public double getTotal() { return total; }
    public int getUrgency() { return urgency; }
}
