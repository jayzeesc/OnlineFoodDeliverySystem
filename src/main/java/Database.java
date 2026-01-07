import java.sql.Connection;
import java.sql.DriverManager;

public final class Database {
    private static final String URL = "jdbc:sqlite:ofds.db";
    private Database() {}
    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
