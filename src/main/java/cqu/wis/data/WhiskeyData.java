package cqu.wis.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides data access functionality for whiskey-related information stored in a MySQL database.
 * 
 * This class establishes and manages connections to the database, performs SQL queries to retrieve whiskey data,
 * and maps the results to {@link WhiskeyDetails} records. It supports queries to fetch all single malts, 
 * malts from a specific region, and malts within a specific age range.
 * 
 * Note: Connection parameters such as database URL, user, and password must be updated appropriately
 * for your local setup.
 * 
 * @author Ayush Bhandari S12157470
 */
public class WhiskeyData {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/WHISKEY"; // Update as needed
    private static final String DB_USER = "root"; // Update as needed
    private static final String DB_PASSWORD = "pass"; // Update as needed

    /**
     * Represents the details of a single malt whiskey.
     * 
     * @param distillery the name of the distillery
     * @param age the age of the whiskey in years
     * @param region the region where the whiskey was produced
     * @param price the price of the whiskey in dollars
     */
    public record WhiskeyDetails(String distillery, int age, String region, int price) {
        @Override
        public String toString() {
            return String.format("%s %d year old from %s - $%d", distillery, age, region, price);
        }
    }

    /** Active database connection instance. */
    public Connection connection;

    /** SQL query to retrieve all single malt records. */
    public String getAllMaltsQuery = "SELECT DISTILLERY, AGE, REGION, PRICE FROM SINGLEMALTS";

    /** SQL query to retrieve single malts from a specific region. */
    public String getMaltsFromRegionQuery = "SELECT DISTILLERY, AGE, REGION, PRICE FROM SINGLEMALTS WHERE REGION = ?";

    /** SQL query to retrieve single malts within a given age range. */
    public String getMaltsInAgeRangeQuery = "SELECT DISTILLERY, AGE, REGION, PRICE FROM SINGLEMALTS WHERE AGE BETWEEN ? AND ?";

    /**
     * Establishes a connection to the whiskey database.
     * 
     * @throws SQLException if a database access error occurs
     */
    public void connect() throws SQLException {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            throw new SQLException("Failed to connect to whiskey database: " + e.getMessage());
        }
    }

    /**
     * Closes the connection to the whiskey database if it is open.
     *
     * @throws SQLException if a database error occurs
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
     * Retrieves all single malt whiskey records from the database.
     * 
     * @return a list of {@link WhiskeyDetails} representing all malts
     * @throws SQLException if a database access error occurs
     */
    public List<WhiskeyDetails> getAllMalts() throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(getAllMaltsQuery);
             ResultSet rs = stmt.executeQuery()) {
            return getWhiskeyDetailsFromResultSet(rs);
        } catch (SQLException e) {
            throw new SQLException("Error getting all malts: " + e.getMessage());
        }
    }

    /**
     * Retrieves single malt whiskeys from the specified region.
     * 
     * @param r the region to filter malts by
     * @return a list of {@link WhiskeyDetails} matching the region
     * @throws SQLException if a database access error occurs
     */
    public List<WhiskeyDetails> getMaltsFromRegion(String r) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(getMaltsFromRegionQuery)) {
            stmt.setString(1, r);
            ResultSet rs = stmt.executeQuery();
            return getWhiskeyDetailsFromResultSet(rs);
        } catch (SQLException e) {
            throw new SQLException("Error getting malts from region: " + e.getMessage());
        }
    }

    /**
     * Retrieves single malt whiskeys within the specified age range.
     * 
     * @param r1 the minimum age (inclusive)
     * @param r2 the maximum age (inclusive)
     * @return a list of {@link WhiskeyDetails} within the age range
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Helper method that converts a {@link ResultSet} into a list of {@link WhiskeyDetails} records.
     * 
     * @param rs the {@link ResultSet} to process
     * @return a list of {@link WhiskeyDetails} extracted from the result set
     * @throws SQLException if a database access error occurs during result processing
     */
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
