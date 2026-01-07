public final class OrderHistoryRow {
    private final int orderId;
    private final String createdAt;
    private final String restaurant;
    private final String status;
    private final int etaMinutes;
    private final double total;

    public OrderHistoryRow(int orderId, String createdAt, String restaurant, String status, int etaMinutes, double total) {
        this.orderId = orderId;
        this.createdAt = createdAt;
        this.restaurant = restaurant;
        this.status = status;
        this.etaMinutes = etaMinutes;
        this.total = total;
    }

    public int getOrderId() { return orderId; }
    public String getCreatedAt() { return createdAt; }
    public String getRestaurant() { return restaurant; }
    public String getStatus() { return status; }
    public int getEtaMinutes() { return etaMinutes; }
    public double getTotal() { return total; }

    public String shortDate() {
        if (createdAt == null) return "";
        String x = createdAt.replace('T', ' ');
        int dot = x.indexOf('.');
        if (dot > 0) x = x.substring(0, dot);
        if (x.length() > 19) x = x.substring(0, 19);
        return x;
    }
}
