import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public final class ProfilePanel extends JPanel {
    private final JLabel name = new JLabel();
    private final JLabel handle = new JLabel();
    private final JTextField fullName = new JTextField();
    private final JTextField birthday = new JTextField();
    private final JTextField permanent = new JTextField();
    private final JTextField other = new JTextField();
    private final JLabel report = new JLabel();

    private final DefaultListModel<OrderHistoryRow> ordersModel = new DefaultListModel<>();
    private final JList<OrderHistoryRow> ordersList = new JList<>(ordersModel);

    private final JButton edit = new JButton("Edit Profile");
    private final JButton save = new JButton("Save");
    private final JButton cancel = new JButton("Cancel");
    private final JButton back = new JButton("Back");
    private final JButton logout = new JButton("Logout");

    private Customer snapshot;

    public ProfilePanel(AppNavigator nav) {
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JPanel top = new JPanel(new GridLayout(0, 1, 6, 6));
        name.setFont(name.getFont().deriveFont(Font.BOLD, 14f));
        top.add(name);
        top.add(handle);
        top.add(report);
        add(top, BorderLayout.NORTH);

        fullName.setPreferredSize(UiSizes.FIELD);
        birthday.setPreferredSize(UiSizes.FIELD);
        permanent.setPreferredSize(UiSizes.FIELD);
        other.setPreferredSize(UiSizes.FIELD);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 8, 8, 8);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;
        addRow(form, gc, y++, "Full Name", fullName);
        addRow(form, gc, y++, "Birthday (optional)", birthday);
        addRow(form, gc, y++, "Permanent Address", permanent);
        addRow(form, gc, y++, "Other Addresses (optional)", other);

        JPanel left = new JPanel(new BorderLayout());
        left.add(form, BorderLayout.NORTH);

        JPanel right = new JPanel(new BorderLayout(8, 8));
        JLabel oh = new JLabel("Order History (double-click to view receipt)");
        oh.setFont(oh.getFont().deriveFont(Font.BOLD, 12f));
        right.add(oh, BorderLayout.NORTH);

        ordersList.setCellRenderer(new OrderHistoryCellRenderer());
        ordersList.setFixedCellHeight(-1);
        right.add(new JScrollPane(ordersList), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        split.setResizeWeight(0.48);
        split.setDividerLocation(520);
        split.setEnabled(false);
        add(split, BorderLayout.CENTER);

        edit.setPreferredSize(UiSizes.BUTTON);
        save.setPreferredSize(UiSizes.BUTTON);
        cancel.setPreferredSize(UiSizes.BUTTON);
        back.setPreferredSize(UiSizes.BUTTON);
        logout.setPreferredSize(UiSizes.BUTTON);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.add(logout);
        actions.add(back);
        actions.add(cancel);
        actions.add(save);
        actions.add(edit);
        add(actions, BorderLayout.SOUTH);

        setEditing(false);

        edit.addActionListener(e -> {
            if (Session.getUser() == null) {
                SwingUtil.info(this, "Please login.");
                nav.show(AppNavigator.LOGIN);
                return;
            }
            snapshot = Session.getUser();
            setEditing(true);
        });

        cancel.addActionListener(e -> {
            if (snapshot != null) apply(snapshot);
            setEditing(false);
        });

        save.addActionListener(e -> {
            Customer u = Session.getUser();
            if (u == null) {
                SwingUtil.info(this, "Please login.");
                nav.show(AppNavigator.LOGIN);
                return;
            }
            String fn = fullName.getText().trim();
            String bd = birthday.getText().trim();
            String pa = permanent.getText().trim();
            String oa = other.getText().trim();

            if (SwingUtil.isBlank(fn) || SwingUtil.isBlank(pa)) {
                SwingUtil.error(this, "Full name and permanent address are required");
                return;
            }
            boolean ok = UserDAO.updateProfile(u.getId(), fn, bd, pa, oa);
            if (!ok) {
                SwingUtil.error(this, "Failed to update profile");
                return;
            }
            Session.setUser(new Customer(u.getId(), u.getUsername(), fn, u.getHandle(), bd.isEmpty() ? null : bd, pa, oa.isEmpty() ? null : oa));
            SwingUtil.info(this, "Profile updated");
            refresh();
            setEditing(false);
        });

        back.addActionListener(e -> nav.show(AppNavigator.RESTAURANTS));

        logout.addActionListener(e -> {
            Session.clearPending();
            Session.logout();
            nav.show(AppNavigator.RESTAURANTS);
        });

        ordersList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    OrderHistoryRow row = ordersList.getSelectedValue();
                    if (row == null) return;
                    String receipt = OrderDAO.receiptForOrder(row.getOrderId());
                    showReceiptDialog(receipt);
                }
            }
        });
    }

    private void showReceiptDialog(String receipt) {
        JTextArea area = new JTextArea(receipt == null ? "" : receipt);
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        area.setCaretPosition(0);
        JScrollPane sp = new JScrollPane(area);
        sp.setPreferredSize(new Dimension(700, 460));
        JOptionPane.showMessageDialog(this, sp, "Receipt", JOptionPane.INFORMATION_MESSAGE);
    }

    private void setEditing(boolean on) {
        fullName.setEditable(on);
        birthday.setEditable(on);
        permanent.setEditable(on);
        other.setEditable(on);

        edit.setVisible(!on);
        save.setVisible(on);
        cancel.setVisible(on);
    }

    public void refresh() {
        Customer u = Session.getUser();
        if (u == null) return;
        apply(u);
        double avg = OrderDAO.averageEtaForUser(u.getId());
        report.setText("Average ETA (minutes): " + String.format("%.1f", avg));
        loadOrders(u.getId());
        setEditing(false);
    }

    private void apply(Customer u) {
        name.setText(u.getFullName() + "  (" + u.getUsername() + ")");
        handle.setText("Handle: " + u.getHandle());
        fullName.setText(u.getFullName());
        birthday.setText(u.getBirthday() == null ? "" : u.getBirthday());
        permanent.setText(u.getPermanentAddress());
        other.setText(u.getOtherAddresses() == null ? "" : u.getOtherAddresses());
    }

    private void loadOrders(int userId) {
        ordersModel.clear();
        List<OrderHistoryRow> rows = OrderDAO.historyForUser(userId);
        for (OrderHistoryRow r : rows) ordersModel.addElement(r);
    }

    private static void addRow(JPanel form, GridBagConstraints gc, int row, String label, JComponent field) {
        gc.gridx = 0;
        gc.gridy = row;
        gc.weightx = 0;
        JLabel l = new JLabel(label);
        l.setPreferredSize(new Dimension(UiSizes.LABEL_COL_WIDTH, 22));
        form.add(l, gc);

        gc.gridx = 1;
        gc.gridy = row;
        gc.weightx = 1;
        form.add(field, gc);
    }
}
