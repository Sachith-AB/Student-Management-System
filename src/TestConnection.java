import util.DBConnection;
import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            System.out.println("Connected to MySQL (XAMPP) successfully!");
        } else {
            System.out.println("Connection failed.");
        }
    }
}
