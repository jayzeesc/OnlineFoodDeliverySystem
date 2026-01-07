import java.util.HashMap;
import java.util.List;

public final class RestaurantLookup {
    private final HashMap<String, List<Restaurant>> byLocation;

    public RestaurantLookup() {
        this.byLocation = RestaurantDAO.byLocation();
    }

    public List<Restaurant> findByLocation(String location) {
        if (location == null) return null;
        return byLocation.get(location.toLowerCase().trim());
    }
}
