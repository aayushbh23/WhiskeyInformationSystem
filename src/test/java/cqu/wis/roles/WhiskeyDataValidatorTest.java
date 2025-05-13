package cqu.wis.roles;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link WhiskeyDataValidator} class.
 * 
 * This test class verifies the functionality of the {@code WhiskeyDataValidator} methods, particularly
 * for validating age ranges and whiskey region names. It includes tests for both valid and invalid inputs
 * to ensure the validator correctly handles edge cases, errors, and normal inputs.
 * 
 * @author Ayush Bhandari S12157470
 */
public class WhiskeyDataValidatorTest {

    private final WhiskeyDataValidator wdv = new WhiskeyDataValidator();

    /**
     * Tests that both age bounds being empty returns a validation error message indicating that at least one bound must be provided.
     */
    @Test
    void checkAgeRangeWithBothEmpty() {
        WhiskeyDataValidator.RangeValidationResponse response = wdv.checkAgeRange("", "");
        assertFalse(response.result());
        assertNull(response.r());
        assertEquals("At least one age bound must be provided", response.message());
    }

    /**
     * Tests that a valid age range with both lower and upper bounds returns a successful validation.
     * 
     * @see WhiskeyDataValidator#checkAgeRange(String, String)
     */
    @Test
    void checkAgeRangeWithValidRange() {
        WhiskeyDataValidator.RangeValidationResponse response = wdv.checkAgeRange("10", "20");
        assertTrue(response.result());
        assertNotNull(response.r());
        assertEquals(10, response.r().lower());
        assertEquals(20, response.r().upper());
        assertEquals("", response.message());
    }

    /**
     * Tests the age range validation for single values (either lower or upper bound).
     * - Validates lower bound only.
     * - Validates upper bound only.
     */
    @Test
    void checkAgeRangeWithSingleValue() {
        // Test lower range only
        WhiskeyDataValidator.RangeValidationResponse lowerOnly = wdv.checkAgeRange("15", "");
        assertTrue(lowerOnly.result());
        assertEquals(15, lowerOnly.r().lower());
        assertEquals(100, lowerOnly.r().upper());

        // Test upper bound only
        WhiskeyDataValidator.RangeValidationResponse upperOnly = wdv.checkAgeRange("", "25");
        assertTrue(upperOnly.result());
        assertEquals(0, upperOnly.r().lower());
        assertEquals(25, upperOnly.r().upper());
    }

    /**
     * Tests age range validation when the lower bound is negative.
     * The validator should reject negative age bounds.
     */
    @Test
    void checkAgeRangeWithNegativeValues() {
        WhiskeyDataValidator.RangeValidationResponse response = wdv.checkAgeRange("-5", "10");
        assertFalse(response.result());
        assertEquals("Age bounds cannot be negative", response.message());
    }

    /**
     * Tests age range validation when the upper bound is less than the lower bound.
     * The validator should reject such cases with an appropriate error message.
     */
    @Test
    void checkAgeRangeWithUpperLessThanLower() {
        WhiskeyDataValidator.RangeValidationResponse response = wdv.checkAgeRange("20", "10");
        assertFalse(response.result());
        assertEquals("Upper bound cannot be less than lower bound", response.message());
    }

    /**
     * Tests age range validation when the input contains non-numeric values.
     * The validator should reject non-numeric values and return an error message.
     */
    @Test
    void checkAgeRangeWithNonNumeric() {
        WhiskeyDataValidator.RangeValidationResponse response = wdv.checkAgeRange("ten", "20");
        assertFalse(response.result());
        assertEquals("Age bounds must be valid numbers", response.message());
    }

    /**
     * Tests that when the lower and upper bounds are equal, the validator correctly accepts the input.
     */
    @Test
    void checkAgeRangeWithEqualRange() {
        WhiskeyDataValidator.RangeValidationResponse response = wdv.checkAgeRange("15", "15");
        assertTrue(response.result());
        assertEquals(15, response.r().lower());
        assertEquals(15, response.r().upper());
    }

    /**
     * Tests the region name validation with a valid region name.
     * The validator should accept valid region names without errors.
     */
    @Test
    void checkRegionWithValidRegion() {
        ValidationResponse response = wdv.checkRegion("Highland");
        assertTrue(response.result());
        assertEquals("", response.message());
    }

    /**
     * Tests the region name validation with an empty region name.
     * The validator should reject empty region names with an appropriate error message.
     */
    @Test
    void checkRegionWithEmptyValue() {
        ValidationResponse response = wdv.checkRegion("");
        assertFalse(response.result());
        assertEquals("Region cannot be empty", response.message());
    }

    /**
     * Tests the region name validation with a {@code null} value.
     * The validator should reject {@code null} region names with an appropriate error message.
     */
    @Test
    void checkRegionWithNullValue() {
        ValidationResponse response = wdv.checkRegion(null);
        assertFalse(response.result());
        assertEquals("Region cannot be empty", response.message());
    }

    /**
     * Tests the region name validation with a region name that contains numbers.
     * The validator should reject region names with numbers and return an error message.
     */
    @Test
    void checkRegionWithNumbers() {
        ValidationResponse response = wdv.checkRegion("Region123");
        assertFalse(response.result());
        assertEquals("Region must contain only letters", response.message());
    }

    /**
     * Tests the region name validation with a region name containing special characters.
     * The validator should reject region names with special characters and return an error message.
     */
    @Test
    void checkRegionWithSpecialCharacters() {
        ValidationResponse response = wdv.checkRegion("Islay!");
        assertFalse(response.result());
        assertEquals("Region must contain only letters", response.message());
    }

    /**
     * Tests the region name validation with a region name containing mixed characters (Uppercase and Lowercase).
     * The validator should accept region name containing mixed characters (Uppercase and Lowercase) without errors.
     */
    @Test
    void checkRegionWithMixedCharacters() {
        ValidationResponse response = wdv.checkRegion("SpeySide");
        assertTrue(response.result());
        assertEquals("", response.message());
    }
}
