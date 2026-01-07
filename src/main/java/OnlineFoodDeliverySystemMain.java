import javax.swing.SwingUtilities;

public class OnlineFoodDeliverySystemMain {
    public static void main(String[] args) {
        Schema.init();
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
