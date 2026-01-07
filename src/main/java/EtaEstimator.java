import java.util.Random;

public final class EtaEstimator {
    private static final Random R = new Random();
    private EtaEstimator() {}

    public static int estimateMinutes(String userAddress, String restaurantLocation, int urgency) {
        int base = 18;
        int jitter = R.nextInt(14);
        int u = Math.max(1, Math.min(10, urgency));
        int urgencyAdj = (10 - u);

        String userLoc = normalizeCity(userAddress);
        String restLoc = normalizeCity(restaurantLocation);

        int distance = RouteGraph.defaultGraph().shortestDistance(userLoc, restLoc);
        int distAdj = (distance == Integer.MAX_VALUE) ? 10 : Math.min(18, distance);

        int eta = base + jitter + distAdj + urgencyAdj;
        if (eta < 15) eta = 15;
        if (eta > 75) eta = 75;
        return eta;
    }

    private static String normalizeCity(String address) {
        if (address == null) return "binan";
        String a = address.toLowerCase();
        if (a.contains("muntinlupa") || a.contains("alabang")) return "muntinlupa";
        if (a.contains("san pedro")) return "san pedro";
        if (a.contains("biñan") || a.contains("binan")) return "binan";
        if (a.contains("santa rosa") || a.contains("sta rosa") || a.contains("sta. rosa")) return "santa rosa";
        if (a.contains("cabuyao")) return "cabuyao";
        if (a.contains("calamba")) return "calamba";
        if (a.contains("laguna")) return "binan";
        return "binan";
    }
}
