import javax.swing.*;
import java.awt.*;

public final class OrderHistoryCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof OrderHistoryRow r) {
            String html = "<html><b>Order #" + r.getOrderId() + "</b>  " + r.shortDate() +
                    "<br/>" + escape(r.getRestaurant()) +
                    " — ₱" + String.format("%.2f", r.getTotal()) +
                    " <span style='color:gray'>(ETA " + r.getEtaMinutes() + "m, " + escape(r.getStatus()) + ")</span></html>";
            lbl.setText(html);
            lbl.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        }
        return lbl;
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
