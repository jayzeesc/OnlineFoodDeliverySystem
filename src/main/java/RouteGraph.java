import java.util.*;

public final class RouteGraph {
    private final Map<String, Map<String, Integer>> g = new HashMap<>();

    public void addEdge(String a, String b, int w) {
        g.computeIfAbsent(a, k -> new HashMap<>()).put(b, w);
        g.computeIfAbsent(b, k -> new HashMap<>()).put(a, w);
    }

    public int shortestDistance(String start, String end) {
        if (start == null || end == null) return Integer.MAX_VALUE;
        String s = start.toLowerCase().trim();
        String t = end.toLowerCase().trim();
        if (!g.containsKey(s) || !g.containsKey(t)) return Integer.MAX_VALUE;

        Map<String, Integer> dist = new HashMap<>();
        Set<String> vis = new HashSet<>();
        for (String n : g.keySet()) dist.put(n, Integer.MAX_VALUE);
        dist.put(s, 0);

        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(dist::get));
        pq.add(s);

        while (!pq.isEmpty()) {
            String u = pq.poll();
            if (!vis.add(u)) continue;
            if (u.equals(t)) break;
            for (Map.Entry<String, Integer> e : g.getOrDefault(u, Collections.emptyMap()).entrySet()) {
                String v = e.getKey();
                int w = e.getValue();
                if (dist.get(u) == Integer.MAX_VALUE) continue;
                int nd = dist.get(u) + w;
                if (nd < dist.get(v)) {
                    dist.put(v, nd);
                    pq.add(v);
                }
            }
        }
        return dist.getOrDefault(t, Integer.MAX_VALUE);
    }

    public static RouteGraph defaultGraph() {
        RouteGraph rg = new RouteGraph();
        rg.addEdge("muntinlupa", "san pedro", 7);
        rg.addEdge("san pedro", "binan", 6);
        rg.addEdge("binan", "santa rosa", 6);
        rg.addEdge("santa rosa", "cabuyao", 5);
        rg.addEdge("cabuyao", "calamba", 6);
        rg.addEdge("binan", "cabuyao", 8);
        rg.addEdge("san pedro", "santa rosa", 8);
        return rg;
    }
}
