import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public final class AppNavigator {
    public static final String LOGIN = "LOGIN";
    public static final String SIGNUP = "SIGNUP";
    public static final String RESTAURANTS = "RESTAURANTS";
    public static final String MENU = "MENU";
    public static final String PROFILE = "PROFILE";
    public static final String RECEIPT = "RECEIPT";

    private final JPanel root;
    private final CardLayout cards;

    public AppNavigator(JFrame frame) {
        this.cards = new CardLayout();
        this.root = new JPanel(cards);
    }

    public JPanel getRoot() { return root; }
    public void add(String key, JPanel panel) { root.add(panel, key); }
    public void show(String key) { cards.show(root, key); }
}
