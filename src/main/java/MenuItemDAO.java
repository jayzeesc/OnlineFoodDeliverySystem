import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public final class MenuItemDAO {
    private MenuItemDAO() {}

    public static List<MenuItem> forRestaurant(int restaurantId) {
        List<MenuItem> out = new ArrayList<>();
        String sql = "SELECT id, restaurant_id, name, price FROM menu_items WHERE restaurant_id=? ORDER BY name";
        try (Connection c = Database.connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, restaurantId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new MenuItem(rs.getInt("id"), rs.getInt("restaurant_id"), rs.getString("name"), rs.getDouble("price")));
                }
            }
        } catch (Exception e) {
            return out;
        }
        return out;
    }
}
