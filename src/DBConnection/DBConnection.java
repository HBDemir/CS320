package DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/hms";
    private static final String USER = "postgres";
    private static final String PASSWORD = "hms2025";

    private static Connection connection = null;

    // Bağlantıyı almak için statik metot
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // PostgreSQL JDBC driver'ı yükle
                Class.forName("org.postgresql.Driver");

                // Bağlantıyı kur
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Veritabanına başarıyla bağlanıldı.");
            } catch (ClassNotFoundException e) {
                System.err.println("PostgreSQL JDBC Driver bulunamadı.");
                e.printStackTrace();
            } catch (SQLException e) {
                System.err.println("Veritabanına bağlanırken hata oluştu.");
                e.printStackTrace();
            }
        }
        return connection;
    }

    // Bağlantıyı kapatmak için metot
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Veritabanı bağlantısı kapatıldı.");
            }
        } catch (SQLException e) {
            System.err.println("Bağlantı kapatılırken hata oluştu.");
            e.printStackTrace();
        }
    }
}


