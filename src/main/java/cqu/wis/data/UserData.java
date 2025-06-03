package cqu.wis.data;

import java.sql.*;

/**
 * Provides data access functionality for user authentication and password management,
 * interacting with a MySQL database that stores usernames and passwords.
 *
 * This class manages database connections, retrieves user details and updates passwords.
 * 
 * Note: Connection parameters such as database URL, user and password must be updated appropriately
 * for your local setup.
 * 
 * @author Ayush Bhandari S12157470
 */
public class UserData {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/USERS"; // Update as needed
    private static final String DB_USER = "root"; // Update as needed
    private static final String DB_PASSWORD = "pass"; // Update as needed

    /**
     * Record representing a user’s details.
     *
     * @param user the username
     * @param password the user's password
     */
    public record UserDetails(String user, String password) {
        /**
         * Determines if the user's password is the default password {@code "password"}.
         *
         * @return {@code true} if the password is {@code "password"}, {@code false} otherwise
         */
        public boolean hasDefaultPassword() {
            return "password".equals(password);
        }
    }

    /** Active database connection instance. */
    private Connection connection;

    /**
     * Default constructor for {@code UserData}.
     */
    public void UserData() {
        // No explicit initialization needed.
    }

    /**
     * Establishes a connection to the user database.
     *
     * @throws SQLException if a database access error occurs
     */
    public void connect() throws SQLException {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            throw new SQLException("Failed to connect to user database: " + e.getMessage());
        }
    }

    /**
     * Closes the database connection if it is open.
     * Logs any errors that occur during closure.
     */
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }

    /**
     * Retrieves the user details for the specified username.
     *
     * @param username the username to search for
     * @return a {@link UserDetails} object if found; {@code null} otherwise
     * @throws SQLException if a database access error occurs
     */
    public UserDetails getUser(String username) throws SQLException {
        String sql = "SELECT USERNAME, PASSWORD FROM PASSWORDS WHERE USERNAME = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new UserDetails(
                        rs.getString("USERNAME"),
                        rs.getString("PASSWORD")
                    );
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error finding user: " + e.getMessage());
        }

        return null;
    }

    /**
     * Updates the password for the specified username.
     *
     * @param username the username whose password will be updated
     * @param newPassword the new password to set
     * @return {@code 1} if the update was successful (at least one row affected), {@code 0} otherwise
     * @throws SQLException if a database access error occurs
     */
    public int updatePassword(String username, String newPassword) throws SQLException {
        String sql = "UPDATE PASSWORDS SET PASSWORD = ? WHERE USERNAME = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, username);

            int rowsAffected = stmt.executeUpdate();
            return (rowsAffected > 0) ? 1 : 0;
        } catch (SQLException e) {
            throw new SQLException("Error updating password: " + e.getMessage());
        }
    }
}
