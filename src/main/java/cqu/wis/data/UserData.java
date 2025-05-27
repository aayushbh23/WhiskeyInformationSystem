package cqu.wis.data;

import java.sql.*;

/**
 *
 * @author Ayush Bhandari S12157470
 */
public class UserData {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/USERS"; // Update as needed
    private static final String DB_USER = "root"; // Update as needed
    private static final String DB_PASSWORD = "pass"; // Update as needed
    
    public record UserDetails(String user, String password){
        public boolean hasDefaultPassword() {
            return "password".equals(password);
        }
    }
    
    private Connection connection;
    
    public void UserData(){}
    
    public void connect() throws SQLException{
         try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            throw new SQLException("Failed to connect to user database: " + e.getMessage());
        }
    }
    
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
    
    public UserDetails getUser(String n) throws SQLException{
        
        String sql = "SELECT USERNAME, PASSWORD FROM PASSWORDS WHERE USERNAME = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, n);
            
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
    
    public int updatePassword(String n, String p) throws SQLException{
        String sql = "UPDATE PASSWORDS SET PASSWORD = ? WHERE USERNAME = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, p);
            stmt.setString(2, n);
            
            int rowsAffected = stmt.executeUpdate();
            return (rowsAffected > 0) ? 1 : 0;
        } catch (SQLException e) {
            throw new SQLException("Error updating password: " + e.getMessage());
        }
        
    }
}
