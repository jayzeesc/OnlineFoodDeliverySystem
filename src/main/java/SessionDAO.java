import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public final class SessionDAO {
    private SessionDAO() {}

    public static void rememberUser(int userId) {
        String sql = "UPDATE app_session SET user_id=? WHERE id=1";
        try (Connection c = Database.connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (Exception ignored) {}
    }

    public static void clearRememberedUser() {
        String sql = "UPDATE app_session SET user_id=NULL WHERE id=1";
        try (Connection c = Database.connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (Exception ignored) {}
    }

    public static Integer getRememberedUserId() {
        String sql = "SELECT user_id FROM app_session WHERE id=1";
        try (Connection c = Database.connect(); PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) return null;
            int id = rs.getInt("user_id");
            return rs.wasNull() ? null : id;
        } catch (Exception e) {
            return null;
        }
    }
}
