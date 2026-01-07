import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class ReceiptBuilder {
    private ReceiptBuilder() {}

    public static String build(Customer user, Restaurant restaurant, int orderId, int etaMinutes, List<CartItem> items, double deliveryFee) {
        StringBuilder sb = new StringBuilder();
        sb.append("ONLINE FOOD DELIVERY SYSTEM\n");
        sb.append("Receipt\n\n");
        sb.append("Order ID: ").append(orderId).append("\n");
        sb.append("Date: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n");
        sb.append("Customer: ").append(user.getFullName()).append(" ").append(user.getHandle()).append("\n");
        sb.append("Deliver to: ").append(user.getPermanentAddress()).append("\n");
        sb.append("Restaurant: ").append(restaurant.getName()).append(" (").append(restaurant.getLocation()).append(")\n");
        sb.append("ETA: ").append(etaMinutes).append(" minutes\n\n");
        double subtotal = 0;
        for (CartItem c : items) {
            double line = c.lineTotal();
            subtotal += line;
            sb.append(c.getQty()).append(" x ").append(c.getName()).append("  ₱").append(String.format("%.2f", line)).append("\n");
        }
        sb.append("\nSubtotal: ₱").append(String.format("%.2f", subtotal)).append("\n");
        sb.append("Delivery Fee: ₱").append(String.format("%.2f", deliveryFee)).append("\n");
        sb.append("Total: ₱").append(String.format("%.2f", subtotal + deliveryFee)).append("\n");
        return sb.toString();
    }
}
