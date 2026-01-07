import javax.swing.*;
import java.awt.*;

public final class TopBar extends JPanel implements SessionListener {
    private final AppNavigator nav;
    private final JLabel title = new JLabel("Online Food Delivery System");
    private final JLabel hiLabel = new JLabel();
    private final JButton loginBtn = new JButton("Login");
    private final JButton signupBtn = new JButton("Sign Up");
    private final JButton profileBtn = new JButton("Profile");
    private final JButton logoutBtn = new JButton("Logout");

    public TopBar(AppNavigator nav) {
        this.nav = nav;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));

        Dimension btn = new Dimension(90, 28);
        loginBtn.setPreferredSize(btn);
        signupBtn.setPreferredSize(btn);
        profileBtn.setPreferredSize(btn);
        logoutBtn.setPreferredSize(btn);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        hiLabel.setFont(hiLabel.getFont().deriveFont(Font.PLAIN, 13f));
        right.add(hiLabel);
        right.add(loginBtn);
        right.add(signupBtn);
        right.add(profileBtn);
        right.add(logoutBtn);

        add(title, BorderLayout.WEST);
        add(right, BorderLayout.EAST);

        loginBtn.addActionListener(e -> nav.show(AppNavigator.LOGIN));
        signupBtn.addActionListener(e -> nav.show(AppNavigator.SIGNUP));

        profileBtn.addActionListener(e -> {
            if (Session.getUser() == null) {
                nav.show(AppNavigator.LOGIN);
                return;
            }
            if (ProfilePanelRef.get() != null) ProfilePanelRef.get().refresh();
            nav.show(AppNavigator.PROFILE);
        });

        logoutBtn.addActionListener(e -> {
            Session.clearPending();
            SessionDAO.clearRememberedUser();
            Session.logout();
            nav.show(AppNavigator.RESTAURANTS);
        });

        Session.addListener(this);
        refresh();
    }

    private void refresh() {
        Customer u = Session.getUser();
        boolean loggedIn = u != null;

        hiLabel.setText(loggedIn ? ("Hi, " + u.getUsername()) : "");
        loginBtn.setVisible(!loggedIn);
        signupBtn.setVisible(!loggedIn);
        profileBtn.setVisible(loggedIn);
        logoutBtn.setVisible(loggedIn);
    }

    @Override
    public void onSessionChanged() {
        SwingUtilities.invokeLater(this::refresh);
    }
}
