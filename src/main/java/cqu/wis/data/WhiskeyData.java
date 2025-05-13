package cqu.wis.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ayush Bhandari S12157470
 */
public class WhiskeyData {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/WHISKEY";
    private static final String DB_USER = "root"; 
    private static final String DB_PASSWORD = "pass";
    
    public record WhiskeyDetails(String distillery, int age, String region, int price) {
        @Override
        public String toString() {
            return String.format("%s %d year old from %s - $%d", distillery, age, region, price);
        }
    }
    
    public Connection connection;
    
    public String getAllMaltsQuery = "SELECT DISTILLERY, AGE, REGION, PRICE FROM SINGLEMALTS";
    public String getMaltsFromRegionQuery = "SELECT DISTILLERY, AGE, REGION, PRICE FROM SINGLEMALTS WHERE REGION = ?";
    public String getMaltsInAgeRangeQuery = "SELECT DISTILLERY, AGE, REGION, PRICE FROM SINGLEMALTS WHERE AGE BETWEEN ? AND ?";
    
    public void connect() throws SQLException {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            throw new SQLException("Failed to connect to whiskey database: " + e.getMessage());
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
    
    public List<WhiskeyDetails> getAllMalts() throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(getAllMaltsQuery);
             ResultSet rs = stmt.executeQuery()) {
            return getWhiskeyDetailsFromResultSet(rs);
        } catch (SQLException e) {
            throw new SQLException("Error getting all malts: " + e.getMessage());
        }
    }
    
    public List<WhiskeyDetails> getMaltsFromRegion(String r) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(getMaltsFromRegionQuery)) {
            stmt.setString(1, r);
            ResultSet rs = stmt.executeQuery();
            return getWhiskeyDetailsFromResultSet(rs);
        } catch (SQLException e) {
            throw new SQLException("Error getting malts from region: " + e.getMessage());
        }
    }
    
    public List<WhiskeyDetails> getMaltsInAgeRange(int r1, int r2) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(getMaltsInAgeRangeQuery)) {
            stmt.setInt(1, r1);
            stmt.setInt(2, r2);
            ResultSet rs = stmt.executeQuery();
            return getWhiskeyDetailsFromResultSet(rs);
        } catch (SQLException e) {
            throw new SQLException("Error getting malts in age range: " + e.getMessage());
        }
    }
    
    private List<WhiskeyDetails> getWhiskeyDetailsFromResultSet(ResultSet rs) throws SQLException {
        List<WhiskeyDetails> whiskeys = new ArrayList<>();
        while (rs.next()) {
            whiskeys.add(new WhiskeyDetails(
                rs.getString("DISTILLERY"),
                rs.getInt("AGE"),
                rs.getString("REGION"),
                rs.getInt("PRICE")
            ));
        }
        return whiskeys;
    } 
}
