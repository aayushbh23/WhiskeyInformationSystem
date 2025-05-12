package cqu.wis.roles;

import java.util.regex.Pattern;

/**
 *
 * @author Ayush Bhandari S12157470
 */
public class WhiskeyDataValidator {
    
    public record Range(int lower, int upper) {}
    public record RangeValidationResponse(boolean result, Range r, String message) {}
    
    private static final Pattern REGION_PATTERN = Pattern.compile("^[a-zA-Z]+$");
    
    public WhiskeyDataValidator() {}
    
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