package cqu.wis.roles;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Ayush Bhandari S12157470
 */
public class WhiskeyDataValidatorTest {
    private final WhiskeyDataValidator wdv = new WhiskeyDataValidator();

    @Test
    void checkAgeRangeWithBothEmpty() {
        WhiskeyDataValidator.RangeValidationResponse response = wdv.checkAgeRange("", "");
        assertFalse(response.result());
        assertNull(response.r());
        assertEquals("At least one age bound must be provided", response.message());
    }

    @Test
    void checkAgeRangeWithValidRange() {
        WhiskeyDataValidator.RangeValidationResponse response = wdv.checkAgeRange("10", "20");
        assertTrue(response.result());
        assertNotNull(response.r());
        assertEquals(10, response.r().lower());
        assertEquals(20, response.r().upper());
        assertEquals("", response.message());
    }

    @Test
    void checkAgeRangeWithSingleValue() {
        // Test lower range only
        WhiskeyDataValidator.RangeValidationResponse lowerOnly = wdv.checkAgeRange("15", "");
        assertTrue(lowerOnly.result());
        assertEquals(15, lowerOnly.r().lower());
        assertEquals(100, lowerOnly.r().upper());

        // Test upper upper only
        WhiskeyDataValidator.RangeValidationResponse upperOnly = wdv.checkAgeRange("", "25");
        assertTrue(upperOnly.result());
        assertEquals(0, upperOnly.r().lower());
        assertEquals(25, upperOnly.r().upper());
    }

    @Test
    void checkAgeRangeWithNegativeValues() {
        WhiskeyDataValidator.RangeValidationResponse response = wdv.checkAgeRange("-5", "10");
        assertFalse(response.result());
        assertEquals("Age bounds cannot be negative", response.message());
    }

    @Test
    void checkAgeRangeWithUpperLessThanLower() {
        WhiskeyDataValidator.RangeValidationResponse response = wdv.checkAgeRange("20", "10");
        assertFalse(response.result());
        assertEquals("Upper bound cannot be less than lower bound", response.message());
    }

    @Test
    void checkAgeRangeWithNonNumeric() {
        WhiskeyDataValidator.RangeValidationResponse response = wdv.checkAgeRange("ten", "20");
        assertFalse(response.result());
        assertEquals("Age bounds must be valid numbers", response.message());
    }

    @Test
    void checkAgeRangeWithEqualRange() {
        WhiskeyDataValidator.RangeValidationResponse response = wdv.checkAgeRange("15", "15");
        assertTrue(response.result());
        assertEquals(15, response.r().lower());
        assertEquals(15, response.r().upper());
    }

    @Test
    void checkRegionWithValidRegion() {
        ValidationResponse response = wdv.checkRegion("Highland");
        assertTrue(response.result());
        assertEquals("", response.message());
    }

    @Test
    void checkRegionWithEmptyValue() {
        ValidationResponse response = wdv.checkRegion("");
        assertFalse(response.result());
        assertEquals("Region cannot be empty", response.message());
    }

    @Test
    void checkRegionWithNullValue() {
        ValidationResponse response = wdv.checkRegion(null);
        assertFalse(response.result());
        assertEquals("Region cannot be empty", response.message());
    }

    @Test
    void checkRegionWithNumbers() {
        ValidationResponse response = wdv.checkRegion("Region123");
        assertFalse(response.result());
        assertEquals("Region must contain only letters", response.message());
    }

    @Test
    void checkRegionWithSpecialCharacters() {
        ValidationResponse response = wdv.checkRegion("Islay!");
        assertFalse(response.result());
        assertEquals("Region must contain only letters", response.message());
    }

    @Test
    void checkRegionWithMixedCharacters() {
        ValidationResponse response = wdv.checkRegion("SpeySide");
        assertTrue(response.result());
        assertEquals("", response.message());
    }
}
