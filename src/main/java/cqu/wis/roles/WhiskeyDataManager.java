package cqu.wis.roles;

import cqu.wis.data.WhiskeyData;
import cqu.wis.data.WhiskeyData.WhiskeyDetails;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Manages retrieval and navigation of whiskey data records.
 * 
 * This class serves as a controller for accessing, storing, and navigating through a list of {@link WhiskeyDetails}
 * retrieved from a {@link WhiskeyData} data source. It supports fetching all records, filtering by region or age range
 * and cyclic navigation through the result set.
 *
 * It also provides methods to connect/disconnect from the data source and is used by the user interface
 * to handle display logic and data access seamlessly.
 * 
 * @author Ayush Bhandari S12157470
 */
public class WhiskeyDataManager {

    private final WhiskeyData wd;
    private List<WhiskeyDetails> records = new ArrayList<>();
    private int numberOfRecords = 0;
    private int currentIndex = -1;
    private WhiskeyDetails currentRecord;

    /**
     * Constructs a new {@code WhiskeyDataManager} using the given {@link WhiskeyData} source.
     * 
     * @param wd the whiskey data source used to fetch records
     */
    public WhiskeyDataManager(WhiskeyData wd) {
        this.wd = wd;
    }

    /**
     * Retrieves all malt records from the data source.
     * 
     * @return the number of records retrieved
     * @throws SQLException if an error occurs while querying the data source
     */
    public int findAllMalts() throws SQLException {
        records = wd.getAllMalts();
        currentIndex = records.isEmpty() ? -1 : 0;
        numberOfRecords = records.size();
        return numberOfRecords;
    }

    /**
     * Retrieves all malt records from a specific region.
     * 
     * @param r the region to filter malts by
     * @return the number of records retrieved
     * @throws SQLException if an error occurs while querying the data source
     */
    public int findMaltsFromRegion(String r) throws SQLException {
        records = wd.getMaltsFromRegion(r);
        currentIndex = records.isEmpty() ? -1 : 0;
        numberOfRecords = records.size();
        return numberOfRecords;
    }

    /**
     * Retrieves all malt records within the specified age range.
     * 
     * @param r1 the lower bound of the age range (inclusive)
     * @param r2 the upper bound of the age range (inclusive)
     * @return the number of records retrieved
     * @throws SQLException if an error occurs while querying the data source
     */
    public int findMaltsInAgeRange(int r1, int r2) throws SQLException {
        records = wd.getMaltsInAgeRange(r1, r2);
        currentIndex = records.isEmpty() ? -1 : 0;
        numberOfRecords = records.size();
        return numberOfRecords;
    }

    /**
     * Retrieves the first whiskey record from the current dataset.
     * 
     * @return the first {@link WhiskeyDetails} record or {@code null} if the dataset is empty
     */
    public WhiskeyDetails first() {
        if (records.isEmpty()) return null;
        currentIndex = 0;
        currentRecord = records.get(currentIndex);
        return currentRecord;
    }

    /**
     * Moves to and retrieves the next record in the dataset.
     * Loops back to the start when the end is reached.
     * 
     * @return the next {@link WhiskeyDetails} record or {@code null} if the dataset is empty
     */
    public WhiskeyDetails next() {
        if (records.isEmpty()) return null;
        currentIndex = (currentIndex + 1) % records.size();
        currentRecord = records.get(currentIndex);
        return currentRecord;
    }

    /**
     * Moves to and retrieves the previous record in the dataset.
     * Loops back to the end when the start is reached.
     * 
     * @return the previous {@link WhiskeyDetails} record or {@code null} if the dataset is empty
     */
    public WhiskeyDetails previous() {
        if (records.isEmpty()) return null;
        currentIndex = (currentIndex - 1 + records.size()) % records.size();
        currentRecord = records.get(currentIndex);
        return currentRecord;
    }

    /**
     * Establishes a connection to the underlying data source.
     * 
     * @throws SQLException if a database connection error occurs
     */
    public void connect() throws SQLException {
        wd.connect();
    }

    /**
     * Disconnects from the underlying data source.
     * 
     * @throws SQLException if a disconnection error occurs
     */
    public void disconnect() throws SQLException {
        wd.disconnect();
    }

    /**
     * Sets the whiskey records manually, used for testing purposes.
     * This replaces the current dataset with the provided array.
     * 
     * @param details an array of {@link WhiskeyDetails} to be used as the dataset
     */
    public void setDetails(WhiskeyDetails[] details) {
        List list = Arrays.asList(details);
        records = new ArrayList<>(list);           
        numberOfRecords = records.size();
        currentIndex = (numberOfRecords == 0) ? -1 : 0;
        currentRecord = (numberOfRecords == 0) ? null : records.get(0);
    }
}
