package cqu.wis.roles;

import cqu.wis.data.WhiskeyData;
import cqu.wis.data.WhiskeyData.WhiskeyDetails;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 *
 * @author Ayush Bhandari S12157470
 */
public class WhiskeyDataManager {
    private final WhiskeyData wd;
    private List<WhiskeyDetails> records = new ArrayList<>();
    private int numberOfRecords = 0;
    private int currentIndex = -1;
    private WhiskeyDetails currentRecord;
    
    public WhiskeyDataManager(WhiskeyData wd) {
        this.wd = wd;
    }
    
    public int findAllMalts() throws SQLException {
        records = wd.getAllMalts();
        currentIndex = records.isEmpty() ? -1 : 0;
        numberOfRecords = records.size();
        return numberOfRecords;
    }
    
    public int findMaltsFromRegion(String r) throws SQLException {
        records = wd.getMaltsFromRegion(r);
        currentIndex = records.isEmpty() ? -1 : 0;
        numberOfRecords = records.size();
        return numberOfRecords;
    }
    
    public int findMaltsInAgeRange(int r1, int r2) throws SQLException {
        records = wd.getMaltsInAgeRange(r1, r2);
        currentIndex = records.isEmpty() ? -1 : 0;
        numberOfRecords = records.size();
        return numberOfRecords;
    }
    
    public WhiskeyDetails first() {
        if (records.isEmpty()) return null;
        currentIndex = 0;
        currentRecord = records.get(currentIndex);
        return currentRecord;
    }
    
    public WhiskeyDetails next() {
        if (records.isEmpty()) return null;
        currentIndex = (currentIndex + 1) % records.size();
        currentRecord = records.get(currentIndex);
        return currentRecord;
    }
    
    public WhiskeyDetails previous() {
        if (records.isEmpty()) return null;
        currentIndex = (currentIndex - 1 + records.size()) % records.size();
        currentRecord = records.get(currentIndex);
        return currentRecord;
    }
    
    public void connect() throws SQLException {
        wd.connect();
    }
    
    public void disconnect() throws SQLException {
        wd.disconnect();
    }
    
    // For testing
    public void setDetails(WhiskeyDetails[] details) {
        List list = Arrays.asList(details);
        records = new ArrayList<>(list);           
        numberOfRecords = records.size();
        currentIndex   = ( numberOfRecords == 0 ) ? -1 : 0;
        currentRecord = ( numberOfRecords == 0 ) ? null : records.get( 0 );
    }
}
