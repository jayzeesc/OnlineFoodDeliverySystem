import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class OrderDAO {
    public static final double DELIVERY_FEE = 35.0;

    private OrderDAO() {}

    public static int createOrder(int userId, int restaurantId, int etaMinutes, int urgency, List<CartItem> items) {
        if (userId <= 0 || restaurantId <= 0 || items == null || items.isEmpty()) return -1;

        double subtotal = 0;
        for (CartItem c : items) subtotal += c.lineTotal();
        double total = subtotal + DELIVERY_FEE;

        String sql = "INSERT INTO orders(user_id, restaurant_id, status, created_at, eta_minutes, delivery_fee, subtotal, total, urgency) " +
                "VALUES(?,?,?,?,?,?,?,?,?)";

        try (Connection c = Database.connect();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, userId);
            ps.setInt(2, restaurantId);
            ps.setString(3, "Placed");
            ps.setString(4, LocalDateTime.now().toString());
            ps.setInt(5, etaMinutes);
            ps.setDouble(6, DELIVERY_FEE);
            ps.setDouble(7, subtotal);
            ps.setDouble(8, total);
            ps.setInt(9, urgency);

            ps.executeUpdate();

            int orderId;
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next()) return -1;
                orderId = keys.getInt(1);
            }

            String itemSql = "INSERT INTO order_items(order_id, item_name, unit_price, qty) VALUES(?,?,?,?)";
            try (PreparedStatement ips = c.prepareStatement(itemSql)) {
                for (CartItem it : items) {
                    ips.setInt(1, orderId);
                    ips.setString(2, it.getName());
                    ips.setDouble(3, it.getUnitPrice());
                    ips.setInt(4, it.getQty());
                    ips.addBatch();
                }
                ips.executeBatch();
            }

            return orderId;
        } catch (Exception e) {
            return -1;
        }
    }

    public static double averageEtaForUser(int userId) {
        String sql = "SELECT AVG(eta_minutes) AS avg_eta FROM orders WHERE user_id=?";
        try (Connection c = Database.connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return 0;
                return rs.getDouble("avg_eta");
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public static List<OrderHistoryRow> historyForUser(int userId) {
        List<OrderHistoryRow> out = new ArrayList<>();
        String sql = "SELECT o.id, o.created_at, o.status, o.eta_minutes, o.total, r.name AS restaurant " +
                "FROM orders o JOIN restaurants r ON r.id=o.restaurant_id " +
                "WHERE o.user_id=? ORDER BY o.id DESC";
        try (Connection c = Database.connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new OrderHistoryRow(
                            rs.getInt("id"),
                            rs.getString("created_at"),
                            rs.getString("restaurant"),
                            rs.getString("status"),
                            rs.getInt("eta_minutes"),
                            rs.getDouble("total")
                    ));
                }
            }
        } catch (Exception e) {
            return out;
        }
        return out;
    }

    public static String receiptForOrder(int orderId) {
        String headSql = "SELECT o.id, o.created_at, o.status, o.eta_minutes, o.delivery_fee, o.subtotal, o.total, " +
                "u.username, u.full_name, u.handle, u.permanent_address, " +
                "r.name AS restaurant, r.location AS location " +
                "FROM orders o JOIN users u ON u.id=o.user_id JOIN restaurants r ON r.id=o.restaurant_id " +
                "WHERE o.id=?";

        String itemsSql = "SELECT item_name, unit_price, qty FROM order_items WHERE order_id=?";

        try (Connection c = Database.connect()) {
            String createdAt;
            String status;
            int eta;
            double fee;
            double subtotal;
            double total;
            String username;
            String fullName;
            String handle;
            String address;
            String restaurant;
            String location;

            try (PreparedStatement ps = c.prepareStatement(headSql)) {
                ps.setInt(1, orderId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) return "Order not found.";
                    createdAt = rs.getString("created_at");
                    status = rs.getString("status");
                    eta = rs.getInt("eta_minutes");
                    fee = rs.getDouble("delivery_fee");
                    subtotal = rs.getDouble("subtotal");
                    total = rs.getDouble("total");
                    username = rs.getString("username");
                    fullName = rs.getString("full_name");
                    handle = rs.getString("handle");
                    address = rs.getString("permanent_address");
                    restaurant = rs.getString("restaurant");
                    location = rs.getString("location");
                }
            }

            List<String> lines = new ArrayList<>();
            try (PreparedStatement ps = c.prepareStatement(itemsSql)) {
                ps.setInt(1, orderId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String n = rs.getString("item_name");
                        double p = rs.getDouble("unit_price");
                        int q = rs.getInt("qty");
                        double line = p * q;
                        lines.add(q + " x " + n + " @ ₱" + String.format("%.2f", p) + " = ₱" + String.format("%.2f", line));
                    }
                }
            }

            StringBuilder sb = new StringBuilder();
            sb.append("===== RECEIPT =====
");
            sb.append("Order ID: ").append(orderId).append("
");
            sb.append("Date: ").append(createdAt).append("
");
            sb.append("Status: ").append(status).append("
");
            sb.append("ETA: ").append(eta).append(" minutes

");

            sb.append("Customer: ").append(fullName).append("
");
            sb.append("Username: ").append(username).append("
");
            sb.append("Handle: ").append(handle).append("
");
            sb.append("Deliver to: ").append(address).append("

");

            sb.append("Restaurant: ").append(restaurant).append("
");
            sb.append("Location: ").append(location).append("

");

            sb.append("Items:
");
            for (String l : lines) sb.append("- ").append(l).append("
");

            sb.append("
Subtotal: ₱").append(String.format("%.2f", subtotal)).append("
");
            sb.append("Delivery Fee: ₱").append(String.format("%.2f", fee)).append("
");
            sb.append("TOTAL: ₱").append(String.format("%.2f", total)).append("
");
            sb.append("===================
");
            return sb.toString();
        } catch (Exception e) {
            return "Failed to load receipt.";
        }
    }
}
