import java.util.ArrayList;
import java.util.List;

public final class Session {
    private static Customer current;
    private static PendingCheckout pending;
    private static final List<SessionListener> listeners = new ArrayList<>();

    private Session() {}

    public static Customer getUser() { return current; }

    public static void setUser(Customer u) {
        current = u;
        notifyListeners();
    }

    public static void logout() {
        current = null;
        notifyListeners();
    }

    public static void setPending(PendingCheckout p) { pending = p; }
    public static PendingCheckout getPending() { return pending; }
    public static void clearPending() { pending = null; }

    public static void addListener(SessionListener l) {
        if (l != null && !listeners.contains(l)) listeners.add(l);
    }

    private static void notifyListeners() {
        for (SessionListener l : new ArrayList<>(listeners)) {
            try { l.onSessionChanged(); } catch (Exception ignored) {}
        }
    }
}
