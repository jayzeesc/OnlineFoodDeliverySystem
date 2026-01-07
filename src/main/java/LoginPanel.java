import javax.swing.*;
import java.awt.*;

public final class LoginPanel extends JPanel {
    public LoginPanel(AppNavigator nav) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JLabel header = new JLabel("Login", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 16f));
        add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(7, 7, 7, 7);
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.WEST;

        JTextField username = new JTextField();
        username.setPreferredSize(UiSizes.FIELD);
        JPasswordField password = new JPasswordField();
        password.setPreferredSize(UiSizes.FIELD);

        int y = 0;
        addRow(form, gc, y++, "Username or @handle", username);
        addRow(form, gc, y++, "Password", password);

        JPanel center = new JPanel(new GridBagLayout());
        GridBagConstraints cc = new GridBagConstraints();
        cc.gridx = 0; cc.gridy = 0;
        cc.anchor = GridBagConstraints.NORTH;
        cc.weighty = 1;
        center.add(form, cc);

        add(center, BorderLayout.CENTER);

        JButton login = new JButton("Login");
        JButton signup = new JButton("Sign Up");
        JButton back = new JButton("Back");

        login.setPreferredSize(UiSizes.BUTTON);
        signup.setPreferredSize(UiSizes.BUTTON);
        back.setPreferredSize(UiSizes.BUTTON);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.add(back);
        actions.add(signup);
        actions.add(login);
        add(actions, BorderLayout.SOUTH);

        login.addActionListener(e -> {
            String u = username.getText().trim();
            String p = new String(password.getPassword());
            if (SwingUtil.isBlank(u) || SwingUtil.isBlank(p)) {
                SwingUtil.error(this, "Username and password are required");
                return;
            }
            Customer c = UserDAO.login(u, p);
            if (c == null) {
                SwingUtil.error(this, "Invalid username/handle or password");
                return;
            }
            Session.setUser(c);
            PendingCheckout pending = Session.getPending();
            if (pending != null && MenuPanelRef.get() != null) {
                MenuPanelRef.get().loadFromPending(pending);
                nav.show(AppNavigator.MENU);
            } else {
                nav.show(AppNavigator.RESTAURANTS);
            }
        });

        signup.addActionListener(e -> nav.show(AppNavigator.SIGNUP));
        back.addActionListener(e -> nav.show(AppNavigator.RESTAURANTS));
    }

    private static void addRow(JPanel form, GridBagConstraints gc, int row, String label, JComponent field) {
        gc.gridx = 0;
        gc.gridy = row;
        gc.weightx = 0;
        form.add(new JLabel(label), gc);

        gc.gridx = 1;
        gc.gridy = row;
        gc.weightx = 1;
        form.add(field, gc);
    }
}
