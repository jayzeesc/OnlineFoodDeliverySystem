import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Locale;

public final class UserDAO {
    private UserDAO() {}

    public static boolean usernameExists(String username) {
        String sql = "SELECT 1 FROM users WHERE username=?";
        try (Connection c = Database.connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            return true;
        }
    }

    public static boolean handleExists(String handle) {
        String sql = "SELECT 1 FROM users WHERE handle=?";
        try (Connection c = Database.connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, handle);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            return true;
        }
    }

    public static String handleFromUsername(String username) {
        String u = username == null ? "" : username.trim();
        if (u.startsWith("@")) u = u.substring(1);
        u = normalize(u);
        if (u.isEmpty()) u = "user";
        String base = "@" + u;
        if (!handleExists(base)) return base;
        int i = 1;
        while (handleExists(base + i)) i++;
        return base + i;
    }

    private static String normalize(String s) {
        String x = s.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "");
        if (x.length() > 22) x = x.substring(0, 22);
        return x;
    }

    public static boolean signup(String username, String password, String fullName, String birthday, String permanentAddress, String otherAddresses) {
        if (isBlank(username) || isBlank(password) || isBlank(fullName) || isBlank(permanentAddress)) return false;
        String un = username.trim();
        if (usernameExists(un)) return false;

        String handle = handleFromUsername(un);

        String sql = "INSERT INTO users(username,password,full_name,handle,birthday,permanent_address,other_addresses) VALUES(?,?,?,?,?,?,?)";
        try (Connection c = Database.connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, un);
            ps.setString(2, password);
            ps.setString(3, fullName.trim());
            ps.setString(4, handle);
            ps.setString(5, isBlank(birthday) ? null : birthday.trim());
            ps.setString(6, permanentAddress.trim());
            ps.setString(7, isBlank(otherAddresses) ? null : otherAddresses.trim());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Customer login(String usernameOrHandle, String password) {
        String in = usernameOrHandle == null ? "" : usernameOrHandle.trim();
        String handle = in.startsWith("@") ? in : ("@" + normalize(in));
        String sql = "SELECT id, username, full_name, handle, birthday, permanent_address, other_addresses " +
                "FROM users WHERE (username=? OR handle=?) AND password=?";
        try (Connection c = Database.connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, in.startsWith("@") ? in.substring(1) : in);
            ps.setString(2, handle);
            ps.setString(3, password == null ? "" : password);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new Customer(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("full_name"),
                        rs.getString("handle"),
                        rs.getString("birthday"),
                        rs.getString("permanent_address"),
                        rs.getString("other_addresses")
                );
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean updateProfile(int userId, String fullName, String birthday, String permanentAddress, String otherAddresses) {
        if (userId <= 0) return false;
        if (isBlank(fullName) || isBlank(permanentAddress)) return false;

        String sql = "UPDATE users SET full_name=?, birthday=?, permanent_address=?, other_addresses=? WHERE id=?";
        try (Connection c = Database.connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, fullName.trim());
            ps.setString(2, isBlank(birthday) ? null : birthday.trim());
            ps.setString(3, permanentAddress.trim());
            ps.setString(4, isBlank(otherAddresses) ? null : otherAddresses.trim());
            ps.setInt(5, userId);
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
