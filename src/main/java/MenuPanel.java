import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public final class MenuPanel extends JPanel {
    private Restaurant currentRestaurant;

    private final DefaultListModel<MenuItem> menuModel = new DefaultListModel<>();
    private final JList<MenuItem> menuList = new JList<>(menuModel);

    private final DefaultListModel<String> cartModel = new DefaultListModel<>();
    private final JList<String> cartList = new JList<>(cartModel);

    private final List<CartItem> cart = new ArrayList<>();
    private final SpinnerNumberModel qtyModel = new SpinnerNumberModel(1, 1, 20, 1);
    private final SpinnerNumberModel urgencyModel = new SpinnerNumberModel(5, 1, 10, 1);

    private final JLabel header = new JLabel("Menu");
    private final JLabel totals = new JLabel();

    public MenuPanel(AppNavigator nav) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JPanel top = new JPanel(new BorderLayout(10, 10));
        JButton back = new JButton("Back");
        back.setPreferredSize(UiSizes.BUTTON);
        top.add(back, BorderLayout.WEST);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 14f));
        top.add(header, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        JPanel left = new JPanel(new BorderLayout(10, 10));
        menuList.setFixedCellHeight(26);
        left.add(new JScrollPane(menuList), BorderLayout.CENTER);

        JPanel addBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JSpinner qty = new JSpinner(qtyModel);
        qty.setPreferredSize(new Dimension(70, 28));
        JButton addToCart = new JButton("Add");
        addToCart.setPreferredSize(UiSizes.BUTTON);
        addBar.add(new JLabel("Qty"));
        addBar.add(qty);
        addBar.add(addToCart);
        left.add(addBar, BorderLayout.SOUTH);

        JPanel right = new JPanel(new BorderLayout(10, 10));
        cartList.setFixedCellHeight(26);
        right.add(new JScrollPane(cartList), BorderLayout.CENTER);

        JPanel rightBottom = new JPanel();
        rightBottom.setLayout(new BoxLayout(rightBottom, BoxLayout.Y_AXIS));

        JPanel urgRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        urgRow.add(new JLabel("Urgency (1-10)"));
        JSpinner urgency = new JSpinner(urgencyModel);
        urgency.setPreferredSize(new Dimension(70, 28));
        urgRow.add(urgency);

        JPanel feeRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        feeRow.add(new JLabel("Delivery Fee: ₱35.00"));

        JPanel totalRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        totalRow.add(totals);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton clear = new JButton("Clear");
        JButton checkout = new JButton("Checkout");
        clear.setPreferredSize(UiSizes.BUTTON);
        checkout.setPreferredSize(UiSizes.BUTTON);
        actions.add(clear);
        actions.add(checkout);

        rightBottom.add(urgRow);
        rightBottom.add(feeRow);
        rightBottom.add(totalRow);
        rightBottom.add(Box.createVerticalStrut(6));
        rightBottom.add(actions);

        right.add(rightBottom, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        split.setResizeWeight(0.55);
        split.setDividerLocation(520);
        split.setEnabled(false);
        add(split, BorderLayout.CENTER);

        addToCart.addActionListener(e -> {
            MenuItem m = menuList.getSelectedValue();
            if (m == null) {
                SwingUtil.error(this, "Select a menu item");
                return;
            }
            int q = qtyModel.getNumber().intValue();
            addOrInc(m.getName(), m.getPrice(), q);
            renderCart();
        });

        clear.addActionListener(e -> {
            cart.clear();
            renderCart();
        });

        checkout.addActionListener(e -> {
            if (currentRestaurant == null) {
                SwingUtil.error(this, "No restaurant selected");
                nav.show(AppNavigator.RESTAURANTS);
                return;
            }
            if (cart.isEmpty()) {
                SwingUtil.error(this, "Cart is empty");
                return;
            }
            int urg = urgencyModel.getNumber().intValue();
            if (Session.getUser() == null) {
                Session.setPending(new PendingCheckout(currentRestaurant, new ArrayList<>(cart), urg));
                SwingUtil.info(this, "Please create an account to complete your order.");
                nav.show(AppNavigator.SIGNUP);
                return;
            }
            placeOrder(nav, currentRestaurant, cart, urg);
        });

        back.addActionListener(e -> nav.show(AppNavigator.RESTAURANTS));
    }

    public void loadForRestaurant(Restaurant r) {
        currentRestaurant = r;
        header.setText("Menu - " + r.getName() + " (" + r.getLocation() + ")");
        menuModel.clear();
        for (MenuItem m : MenuItemDAO.forRestaurant(r.getId())) menuModel.addElement(m);
        cart.clear();
        renderCart();
    }

    public void loadFromPending(PendingCheckout p) {
        if (p == null) return;
        loadForRestaurant(p.getRestaurant());
        cart.clear();
        cart.addAll(p.getCart());
        urgencyModel.setValue(p.getUrgency());
        renderCart();
    }

    private void addOrInc(String name, double price, int qty) {
        for (CartItem c : cart) {
            if (c.getName().equalsIgnoreCase(name) && Math.abs(c.getUnitPrice() - price) < 0.0001) {
                c.inc(qty);
                return;
            }
        }
        cart.add(new CartItem(name, price, qty));
    }

    private void renderCart() {
        cartModel.clear();
        double subtotal = 0;
        for (CartItem c : cart) {
            double line = c.lineTotal();
            subtotal += line;
            cartModel.addElement(c.getQty() + " x " + c.getName() + " = ₱" + String.format("%.2f", line));
        }
        totals.setText("Subtotal: ₱" + String.format("%.2f", subtotal) + "   Total: ₱" + String.format("%.2f", subtotal + OrderDAO.DELIVERY_FEE));
    }

    private void placeOrder(AppNavigator nav, Restaurant r, List<CartItem> items, int urgency) {
        Customer u = Session.getUser();
        int eta = EtaEstimator.estimateMinutes(u.getPermanentAddress(), r.getLocation(), urgency);
        int orderId = OrderDAO.createOrder(u.getId(), r.getId(), eta, urgency, items);
        if (orderId <= 0) {
            SwingUtil.error(this, "Failed to place order");
            return;
        }
        String receipt = ReceiptBuilder.build(u, r, orderId, eta, items, OrderDAO.DELIVERY_FEE);
        ReceiptPanelRef.get().setReceipt(receipt);
        Session.clearPending();
        nav.show(AppNavigator.RECEIPT);
    }
}
