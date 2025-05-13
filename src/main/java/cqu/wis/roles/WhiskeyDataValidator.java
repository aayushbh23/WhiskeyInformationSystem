package cqu.wis.roles;

import java.util.regex.Pattern;

/**
 * Provides validation logic for whiskey data input, such as age ranges and region names.
 * This class is used to validate user input fields before querying or inserting whiskey data.
 * It ensures that age bounds are logical and numeric, and that region names are alphabetic and non-empty.
 * 
 * The validation results are encapsulated in simple record types that indicate whether the validation passed,
 * provide additional context (like computed bounds), and include error messages if applicable.
 * 
 * @author Ayush Bhandari S12157470
 */
public class WhiskeyDataValidator {

    /**
     * Represents a validated age range with a lower and upper bound.
     * 
     * @param lower the lower bound of the range (inclusive)
     * @param upper the upper bound of the range (inclusive)
     */
    public record Range(int lower, int upper) {}

    /**
     * Represents the result of validating an age range.
     * 
     * @param result  whether the validation was successful
     * @param r       the validated {@link Range}, or {@code null} if validation failed
     * @param message the error message if validation failed, or empty if successful
     */
    public record RangeValidationResponse(boolean result, Range r, String message) {}

    private static final Pattern REGION_PATTERN = Pattern.compile("^[a-zA-Z]+$");

    /**
     * Default constructor for {@code WhiskeyDataValidator}.
     */
    public WhiskeyDataValidator() {}

    /**
     * Validates an age range given as two string inputs.
     * <ul>
     *   <li>If both bounds are empty or null, validation fails.</li>
     *   <li>If one bound is missing, it defaults to 0 (lower) or 100 (upper).</li>
     *   <li>Negative values and upper bounds less than lower bounds are invalid.</li>
     *   <li>Non-numeric values result in failure.</li>
     * </ul>
     *
     * @param b1 the lower age bound as a string (may be empty or null)
     * @param b2 the upper age bound as a string (may be empty or null)
     * @return a {@link RangeValidationResponse} containing the result and relevant messages
     */
    public RangeValidationResponse checkAgeRange(String b1, String b2) {
        if ((b1 == null || b1.isEmpty()) && (b2 == null || b2.isEmpty())) {
            return new RangeValidationResponse(false, null, "At least one age bound must be provided");
        }

        try {
            int lower = (b1 == null || b1.isEmpty()) ? 0 : Integer.parseInt(b1);
            int upper = (b2 == null || b2.isEmpty()) ? 100 : Integer.parseInt(b2);

            Range range = new Range(lower, upper);

            if (lower < 0 || upper < 0) {
                return new RangeValidationResponse(false, range, "Age bounds cannot be negative");
            }
            if (upper < lower) {
                return new RangeValidationResponse(false, range, "Upper bound cannot be less than lower bound");
            }

            return new RangeValidationResponse(true, range, "");
        } catch (NumberFormatException e) {
            return new RangeValidationResponse(false, null, "Age bounds must be valid numbers");
        }
    }

    /**
     * Validates a whiskey region name.
     * <ul>
     *   <li>The region must not be null or empty.</li>
     *   <li>The region must contain only letters (A-Z, a-z).</li>
     * </ul>
     *
     * @param r the region name to validate
     * @return a {@link ValidationResponse} indicating the result and error message if any
     */
    public ValidationResponse checkRegion(String r) {
        if (r == null || r.isEmpty()) {
            return ValidationResponse.invalid("Region cannot be empty");
        }
        if (!REGION_PATTERN.matcher(r).matches()) {
            return ValidationResponse.invalid("Region must contain only letters");
        }
        return ValidationResponse.isValid();
    }
}
