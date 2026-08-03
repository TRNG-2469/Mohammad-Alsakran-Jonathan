package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    //Singleton Instance
    private static ConnectionFactory instance;

    //Variables for connecting
    private final String url;
    private final String username;
    private final String password;
    // Private constructor prevents instantiation from outside
    private ConnectionFactory() {
        this.url = System.getenv("DB_URL");
        this.username = System.getenv("DB_USER");
        this.password = System.getenv("DB_PASS");

        // Fail-fast validation: Verify environment setup
        if (this.url == null || this.username == null || this.password == null) {
            throw new IllegalStateException(
                    "Critical Error: Database environment variables (DB_URL, DB_USER, DB_PASS) are not configured."
            );
        }

        // Force-load the PostgreSQL driver class (recommended in older frameworks)
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC driver not found on classpath.", e);
        }
    }

    // Global access point for the Singleton instance
    public static synchronized ConnectionFactory getInstance() {
        if (instance == null) {
            instance = new ConnectionFactory();
        }
        return instance;
    }

    // Factory method returning a connection
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
