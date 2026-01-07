import javax.swing.JOptionPane;
import javax.swing.JTextField;

public final class SwingUtil {
    private SwingUtil() {}

    public static String val(JTextField t) {
        return t == null ? "" : t.getText().trim();
    }

    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static void error(java.awt.Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void info(java.awt.Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
