import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public final class RestaurantsPanel extends JPanel {
    private final DefaultListModel<Restaurant> model = new DefaultListModel<>();
    private final JList<Restaurant> list = new JList<>(model);
    private final JTextField locationFilter = new JTextField();

    public RestaurantsPanel(AppNavigator nav) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JPanel filter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JLabel locLbl = new JLabel("Location:");
        locationFilter.setPreferredSize(new Dimension(260, 28));

        JButton apply = new JButton("Apply");
        JButton clear = new JButton("Clear");
        apply.setPreferredSize(UiSizes.BUTTON);
        clear.setPreferredSize(UiSizes.BUTTON);

        filter.add(locLbl);
        filter.add(locationFilter);
        filter.add(apply);
        filter.add(clear);

        add(filter, BorderLayout.NORTH);

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFixedCellHeight(28);
        add(new JScrollPane(list), BorderLayout.CENTER);

        JButton openMenu = new JButton("Browse Menu");
        openMenu.setPreferredSize(UiSizes.BUTTON);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottom.add(openMenu);
        add(bottom, BorderLayout.SOUTH);

        Runnable openSelected = () -> {
            Restaurant r = list.getSelectedValue();
            if (r == null) {
                SwingUtil.error(this, "Select a restaurant");
                return;
            }
            if (MenuPanelRef.get() != null) MenuPanelRef.get().loadForRestaurant(r);
            nav.show(AppNavigator.MENU);
        };

        apply.addActionListener(e -> {
            String loc = SwingUtil.val(locationFilter);
            if (SwingUtil.isBlank(loc)) {
                reloadAll();
                return;
            }
            RestaurantLookup lookup = new RestaurantLookup();
            List<Restaurant> found = lookup.findByLocation(loc);
            model.clear();
            if (found != null) for (Restaurant r : found) model.addElement(r);
        });

        clear.addActionListener(e -> {
            locationFilter.setText("");
            reloadAll();
        });

        openMenu.addActionListener(e -> openSelected.run());

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    openSelected.run();
                }
            }
        });

        reloadAll();
    }

    public void reloadAll() {
        model.clear();
        for (Restaurant r : RestaurantDAO.all()) model.addElement(r);
    }
}
