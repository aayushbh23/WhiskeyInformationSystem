package cqu.wis.roles;

/**
 *
 * @author Ayush Bhandari S12157470
 */
public record ValidationResponse(boolean result, String message) {
    public static ValidationResponse isValid() {
        return new ValidationResponse(true, "");
    }
    
    public static ValidationResponse invalid(String message) {
        return new ValidationResponse(false, message);
    }
}
