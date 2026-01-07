import javax.swing.*;
import java.awt.*;

public final class MainFrame extends JFrame {
    public MainFrame() {
        super("Online Food Delivery System");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(900, 600);
        setResizable(false);
        setLocationRelativeTo(null);

        AppNavigator nav = new AppNavigator(this);

        LoginPanel login = new LoginPanel(nav);
        SignupPanel signup = new SignupPanel(nav);
        RestaurantsPanel restaurants = new RestaurantsPanel(nav);
        MenuPanel menu = new MenuPanel(nav);
        ProfilePanel profile = new ProfilePanel(nav);
        ReceiptPanel receipt = new ReceiptPanel(nav);

        ProfilePanelRef.set(profile);
        MenuPanelRef.set(menu);
        ReceiptPanelRef.set(receipt);

        nav.add(AppNavigator.LOGIN, login);
        nav.add(AppNavigator.SIGNUP, signup);
        nav.add(AppNavigator.RESTAURANTS, restaurants);
        nav.add(AppNavigator.MENU, menu);
        nav.add(AppNavigator.PROFILE, profile);
        nav.add(AppNavigator.RECEIPT, receipt);

        JPanel root = new JPanel(new BorderLayout());
        root.add(new TopBar(nav), BorderLayout.NORTH);
        root.add(nav.getRoot(), BorderLayout.CENTER);

        setContentPane(root);
        nav.show(AppNavigator.RESTAURANTS);
    }
}
