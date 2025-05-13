package cqu.wis.roles;

import cqu.wis.data.WhiskeyData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link WhiskeyDataManager} class.
 * 
 * This test class verifies the behavior of the {@code WhiskeyDataManager} with different data sets
 * and scenarios, including handling empty, single, and multiple whiskey records.
 * 
 * It ensures that navigation through records using {@code next()}, {@code previous()}, and {@code first()}
 * works correctly and consistently under various conditions.
 * 
 * @author Ayush Bhandari S12157470
 */
public class WhiskeyDataManagerTest {

    /**
     * Tests that calling {@code next()} on an empty record list returns {@code null}.
     */
    @Test
    public void nextWithNoRecordsTest() {
        WhiskeyDataManager wdm = new WhiskeyDataManager(null);
        WhiskeyData.WhiskeyDetails[] details = {};
        wdm.setDetails(details);
        assertEquals(null, wdm.next());
    }

    /**
     * Tests that calling {@code previous()} on an empty record list returns {@code null}.
     */
    @Test
    public void previousWithNoRecordsTest() {
        WhiskeyDataManager wdm = new WhiskeyDataManager(null);
        WhiskeyData.WhiskeyDetails[] details = {};
        wdm.setDetails(details);
        assertEquals(null, wdm.previous());
    }

    /**
     * Tests that repeatedly calling {@code next()} with a single record always returns the same record.
     */
    @Test
    public void nextWithSingleRecordTest() {
        WhiskeyDataManager wdm = new WhiskeyDataManager(null);
        WhiskeyData.WhiskeyDetails singleRecord = new WhiskeyData.WhiskeyDetails("Test", 10, "Region", 100);
        wdm.setDetails(new WhiskeyData.WhiskeyDetails[] { singleRecord });
        assertEquals(singleRecord, wdm.next());
        assertEquals(singleRecord, wdm.next());
        assertEquals(singleRecord, wdm.next());
    }

    /**
     * Tests that repeatedly calling {@code previous()} with a single record always returns the same record.
     */
    @Test
    public void previousWithSingleRecordTest() {
        WhiskeyDataManager wdm = new WhiskeyDataManager(null);
        WhiskeyData.WhiskeyDetails singleRecord = new WhiskeyData.WhiskeyDetails("Test", 10, "Region", 100);
        wdm.setDetails(new WhiskeyData.WhiskeyDetails[] { singleRecord });
        assertEquals(singleRecord, wdm.previous());
        assertEquals(singleRecord, wdm.previous());
        assertEquals(singleRecord, wdm.previous());
    }

    /**
     * Tests {@code next()} navigation with two records, ensuring it loops through them correctly.
     */
    @Test
    public void nextWithTwoRecordsTest() {
        WhiskeyDataManager wdm = new WhiskeyDataManager(null);
        WhiskeyData.WhiskeyDetails record1 = new WhiskeyData.WhiskeyDetails("Test1", 10, "Region1", 100);
        WhiskeyData.WhiskeyDetails record2 = new WhiskeyData.WhiskeyDetails("Test2", 10, "Region2", 100);
        wdm.setDetails(new WhiskeyData.WhiskeyDetails[] { record1, record2 });

        assertEquals(record2, wdm.next());
        assertEquals(record1, wdm.next());
        assertEquals(record2, wdm.next());
        assertEquals(record1, wdm.next());
        assertEquals(record2, wdm.next());
    }

    /**
     * Tests {@code previous()} navigation with two records, ensuring it loops through them correctly.
     */
    @Test
    public void previousWithTwoRecordsTest() {
        WhiskeyDataManager wdm = new WhiskeyDataManager(null);
        WhiskeyData.WhiskeyDetails record1 = new WhiskeyData.WhiskeyDetails("Test1", 10, "Region1", 100);
        WhiskeyData.WhiskeyDetails record2 = new WhiskeyData.WhiskeyDetails("Test2", 10, "Region2", 100);
        wdm.setDetails(new WhiskeyData.WhiskeyDetails[] { record1, record2 });

        assertEquals(record2, wdm.previous());
        assertEquals(record1, wdm.previous());
        assertEquals(record2, wdm.previous());
        assertEquals(record1, wdm.previous());
        assertEquals(record2, wdm.previous());
    }

    /**
     * Tests cyclic navigation forward and backward through a list of multiple records using
     * {@code next()} and {@code previous()} methods.
     */
    @Test
    public void navigationWithMultipleRecordsTest() {
        WhiskeyDataManager wdm = new WhiskeyDataManager(null);
        WhiskeyData.WhiskeyDetails record1 = new WhiskeyData.WhiskeyDetails("Test1", 10, "Region1", 100);
        WhiskeyData.WhiskeyDetails record2 = new WhiskeyData.WhiskeyDetails("Test2", 10, "Region2", 100);
        WhiskeyData.WhiskeyDetails record3 = new WhiskeyData.WhiskeyDetails("Test3", 10, "Region3", 100);
        WhiskeyData.WhiskeyDetails record4 = new WhiskeyData.WhiskeyDetails("Test4", 10, "Region4", 100);
        WhiskeyData.WhiskeyDetails record5 = new WhiskeyData.WhiskeyDetails("Test5", 10, "Region5", 100);

        wdm.setDetails(new WhiskeyData.WhiskeyDetails[] { record1, record2, record3, record4, record5 });

        // Forward navigation
        assertEquals(record2, wdm.next());
        assertEquals(record3, wdm.next());
        assertEquals(record4, wdm.next());
        assertEquals(record5, wdm.next());
        assertEquals(record1, wdm.next());
        assertEquals(record2, wdm.next());

        // Backward navigation
        assertEquals(record1, wdm.previous());
        assertEquals(record5, wdm.previous());
        assertEquals(record4, wdm.previous());
        assertEquals(record3, wdm.previous());
        assertEquals(record2, wdm.previous());
        assertEquals(record1, wdm.previous());
    }

    /**
     * Tests that calling {@code first()} on an empty record list returns {@code null}.
     */
    @Test
    public void firstWithEmptyListTest() {
        WhiskeyDataManager wdm = new WhiskeyDataManager(null);
        wdm.setDetails(new WhiskeyData.WhiskeyDetails[] {});
        assertEquals(null, wdm.first());
    }

    /**
     * Tests that {@code first()} returns the first record when the list is not empty.
     */
    @Test
    public void firstWithNonEmptyListTest() {
        WhiskeyDataManager wdm = new WhiskeyDataManager(null);
        WhiskeyData.WhiskeyDetails nonEmptyRecord = new WhiskeyData.WhiskeyDetails("Test", 10, "Region", 100);
        wdm.setDetails(new WhiskeyData.WhiskeyDetails[] { nonEmptyRecord });
        assertEquals(nonEmptyRecord, wdm.first());
    }
}
