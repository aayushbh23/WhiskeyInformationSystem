package cqu.wis.roles;

import cqu.wis.data.WhiskeyData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Ayush Bhandari S12157470
 */
public class WhiskeyDataManagerTest {
    @Test
    public void nextWithNoRecordsTest(){
        WhiskeyDataManager wdm = new WhiskeyDataManager(null);
        WhiskeyData.WhiskeyDetails[] details = {};
        wdm.setDetails(details);
        assertEquals(null,wdm.next());
    }
    
    @Test
    public void previousWithNoRecordsTest() {
        WhiskeyDataManager wdm = new WhiskeyDataManager(null);
        WhiskeyData.WhiskeyDetails[] details = {};
        wdm.setDetails(details);
        assertEquals(null,wdm.previous());
    }
    
    @Test
    public void nextWithSingleRecordTest() {
        WhiskeyDataManager wdm = new WhiskeyDataManager(null);
        WhiskeyData.WhiskeyDetails singleRecord = new WhiskeyData.WhiskeyDetails("Test", 10, "Region", 100);
        wdm.setDetails(new WhiskeyData.WhiskeyDetails[] { singleRecord });       
        // Returns same value again and again.
        assertEquals(singleRecord, wdm.next());
        assertEquals(singleRecord, wdm.next()); 
        assertEquals(singleRecord, wdm.next());

    }
    
    @Test
    public void previousWithSingleRecordTest() {
        WhiskeyDataManager wdm = new WhiskeyDataManager(null);
        WhiskeyData.WhiskeyDetails singleRecord = new WhiskeyData.WhiskeyDetails("Test", 10, "Region", 100);
        wdm.setDetails(new WhiskeyData.WhiskeyDetails[] { singleRecord });       
        // Returns same value again and again.
        assertEquals(singleRecord, wdm.previous());
        assertEquals(singleRecord, wdm.previous()); 
        assertEquals(singleRecord, wdm.previous());
    }
    
    @Test
    public void nextWithTwoRecordsTest() {
        WhiskeyDataManager wdm = new WhiskeyDataManager(null);
        WhiskeyData.WhiskeyDetails record1 = new WhiskeyData.WhiskeyDetails("Test1", 10, "Region1", 100);
        WhiskeyData.WhiskeyDetails record2 = new WhiskeyData.WhiskeyDetails("Test2", 10, "Region2", 100);
        wdm.setDetails(new WhiskeyData.WhiskeyDetails[] { record1,record2 });       
        
        // Returns record1 after record2
        assertEquals(record2, wdm.next());
        assertEquals(record1, wdm.next()); 
        assertEquals(record2, wdm.next());
        assertEquals(record1, wdm.next()); 
        assertEquals(record2, wdm.next());
    }

   
    @Test
    public void previousWithTwoRecordsTest() {
        WhiskeyDataManager wdm = new WhiskeyDataManager(null);
        WhiskeyData.WhiskeyDetails record1 = new WhiskeyData.WhiskeyDetails("Test1", 10, "Region1", 100);
        WhiskeyData.WhiskeyDetails record2 = new WhiskeyData.WhiskeyDetails("Test2", 10, "Region2", 100);
        wdm.setDetails(new WhiskeyData.WhiskeyDetails[] { record1,record2 });       
        
        // Returns record2 after record1
        assertEquals(record2, wdm.previous());
        assertEquals(record1, wdm.previous()); 
        assertEquals(record2, wdm.previous());
        assertEquals(record1, wdm.previous()); 
        assertEquals(record2, wdm.previous());
    }
    
    @Test
    public void navigationWithMultipleRecordsTest() {
        WhiskeyDataManager wdm = new WhiskeyDataManager(null);
        WhiskeyData.WhiskeyDetails record1 = new WhiskeyData.WhiskeyDetails("Test1", 10, "Region1", 100);
        WhiskeyData.WhiskeyDetails record2 = new WhiskeyData.WhiskeyDetails("Test2", 10, "Region2", 100);
        WhiskeyData.WhiskeyDetails record3 = new WhiskeyData.WhiskeyDetails("Test3", 10, "Region3", 100);
        WhiskeyData.WhiskeyDetails record4 = new WhiskeyData.WhiskeyDetails("Test4", 10, "Region4", 100);
        WhiskeyData.WhiskeyDetails record5 = new WhiskeyData.WhiskeyDetails("Test5", 10, "Region5", 100);

        wdm.setDetails(new WhiskeyData.WhiskeyDetails[] { record1,record2,record3,record4,record5 });       
        
        // Returns next records
        assertEquals(record2, wdm.next());
        assertEquals(record3, wdm.next()); 
        assertEquals(record4, wdm.next());
        assertEquals(record5, wdm.next()); 
        assertEquals(record1, wdm.next());
        assertEquals(record2, wdm.next());

        // Returns previous records
        assertEquals(record1, wdm.previous());
        assertEquals(record5, wdm.previous()); 
        assertEquals(record4, wdm.previous());
        assertEquals(record3, wdm.previous()); 
        assertEquals(record2, wdm.previous());
        assertEquals(record1, wdm.previous());
    }
    @Test
    public void firstWithEmptyListTest() {
        WhiskeyDataManager wdm = new WhiskeyDataManager(null);
        wdm.setDetails(new WhiskeyData.WhiskeyDetails[] {});
        assertEquals(null,wdm.first());
    }

    @Test
    public void firstWithNonEmptyListTest() {
        WhiskeyDataManager wdm = new WhiskeyDataManager(null);
        WhiskeyData.WhiskeyDetails nonEmptyRecord = new WhiskeyData.WhiskeyDetails("Test", 10, "Region", 100);
        wdm.setDetails(new WhiskeyData.WhiskeyDetails[] { nonEmptyRecord });
        assertEquals(nonEmptyRecord, wdm.first());
    }
}