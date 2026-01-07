import javax.swing.SwingUtilities;

public class OnlineFoodDeliverySystemMain {
    public static void main(String[] args) {
        Schema.init();

        Integer uid = SessionDAO.getRememberedUserId();
        if (uid != null) {
            Customer u = UserDAO.getById(uid);
            if (u != null) Session.setUser(u);
        }
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
