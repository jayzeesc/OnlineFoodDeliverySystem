import javax.swing.*;
import java.awt.*;

public final class SignupPanel extends JPanel {
    public SignupPanel(AppNavigator nav) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JLabel header = new JLabel("Sign Up", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 16f));
        add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(7, 7, 7, 7);
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.WEST;

        JTextField fullName = field();
        JTextField username = field();
        JPasswordField password = passField();
        JTextField birthday = field();
        JTextField permanentAddress = field();
        JTextField otherAddresses = field();

        int y = 0;

        addRow(form, gc, y++, "Full Name (required)", fullName);
        addRow(form, gc, y++, "Username (required) - handle will be @username", username);
        addRow(form, gc, y++, "Password (required)", password);
        addRow(form, gc, y++, "Birthday (optional, YYYY-MM-DD)", birthday);
        addRow(form, gc, y++, "Permanent Address (required)", permanentAddress);
        addRow(form, gc, y++, "Other Addresses (optional)", otherAddresses);

        JPanel center = new JPanel(new GridBagLayout());
        GridBagConstraints cc = new GridBagConstraints();
        cc.gridx = 0; cc.gridy = 0;
        cc.anchor = GridBagConstraints.NORTH;
        cc.weighty = 1;
        center.add(form, cc);

        add(new JScrollPane(center), BorderLayout.CENTER);

        JButton create = new JButton("Create");
        JButton back = new JButton("Back");
        create.setPreferredSize(UiSizes.BUTTON);
        back.setPreferredSize(UiSizes.BUTTON);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.add(back);
        actions.add(create);
        add(actions, BorderLayout.SOUTH);

        create.addActionListener(e -> {
            String fn = fullName.getText().trim();
            String un = username.getText().trim();
            String pw = new String(password.getPassword());
            String bd = birthday.getText().trim();
            String pa = permanentAddress.getText().trim();
            String oa = otherAddresses.getText().trim();

            if (SwingUtil.isBlank(fn) || SwingUtil.isBlank(un) || SwingUtil.isBlank(pw) || SwingUtil.isBlank(pa)) {
                SwingUtil.error(this, "Full name, username, password, and permanent address are required");
                return;
            }
            if (UserDAO.usernameExists(un)) {
                SwingUtil.error(this, "Username already exists");
                return;
            }

            boolean ok = UserDAO.signup(un, pw, fn, bd, pa, oa);
            if (!ok) {
                SwingUtil.error(this, "Signup failed. Please check inputs and try again.");
                return;
            }

            Customer c = UserDAO.login(un, pw);
            if (c == null) {
                SwingUtil.error(this, "Account created but login failed");
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

        back.addActionListener(e -> nav.show(AppNavigator.RESTAURANTS));
    }

    private static JTextField field() {
        JTextField t = new JTextField();
        t.setPreferredSize(UiSizes.FIELD);
        return t;
    }

    private static JPasswordField passField() {
        JPasswordField p = new JPasswordField();
        p.setPreferredSize(UiSizes.FIELD);
        return p;
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
