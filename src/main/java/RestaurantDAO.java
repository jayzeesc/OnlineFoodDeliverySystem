import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class RestaurantDAO {
    private RestaurantDAO() {}

    public static List<Restaurant> all() {
        List<Restaurant> out = new ArrayList<>();
        String sql = "SELECT id, name, location, category FROM restaurants ORDER BY name";
        try (Connection c = Database.connect(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new Restaurant(rs.getInt("id"), rs.getString("name"), rs.getString("location"), rs.getString("category")));
            }
        } catch (Exception e) {
            return out;
        }
        return out;
    }

    public static HashMap<String, List<Restaurant>> byLocation() {
        HashMap<String, List<Restaurant>> map = new HashMap<>();
        for (Restaurant r : all()) {
            map.computeIfAbsent(r.getLocation().toLowerCase(), k -> new ArrayList<>()).add(r);
        }
        return map;
    }
}
