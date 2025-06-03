package cqu.wis.roles;

/**
 * Represents the result of a validation operation, indicating whether the input is valid
 * and providing an accompanying message in case of failure.
 * This record is commonly used across the application to standardize responses from
 * input validators such as region or age field checks.
 * 
 * @param result {@code true} if the input passed validation; {@code false} otherwise
 * @param message the error message if validation failed or an empty string if successful
 * 
 * @author Ayush Bhandari S12157470
 */
public record ValidationResponse(boolean result, String message) {

    /**
     * Creates a successful validation response.
     * 
     * @return a {@code ValidationResponse} indicating that validation succeeded
     */
    public static ValidationResponse isValid() {
        return new ValidationResponse(true, "");
    }

    /**
     * Creates a failed validation response with a specific message.
     * 
     * @param message the failure message explaining why validation did not pass
     * @return a {@code ValidationResponse} indicating that validation failed
     */
    public static ValidationResponse invalid(String message) {
        return new ValidationResponse(false, message);
    }
}
