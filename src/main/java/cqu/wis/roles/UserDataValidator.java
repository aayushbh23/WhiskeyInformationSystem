package cqu.wis.roles;

import cqu.wis.data.UserData.UserDetails;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

/**
 *
 * @author Ayush Bhandari S12157470
 */
public class UserDataValidator {
    private static final int MINIMUM_PASSWORD_LENGTH = 8;
    private static final String DEFAULT_PASSWORD = "password";
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z]+$");
    public static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{8,}$"
    );

    public String generateSHA1(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(s.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 algorithm not found", e);
        }
    }
    
    public void UserDataValidator(){}
   
    public ValidationResponse checkCurrentDetails(UserDetails ud, String n, String oldp) {
        
        ValidationResponse fieldsCheck = checkForFieldsPresent(n, oldp);
        if (!ValidationResponse.isValid().result()) {
            return fieldsCheck;
        } 

        if (!USERNAME_PATTERN.matcher(n).matches()) {
            return ValidationResponse.invalid("Username must contain only letters");
        }

        if (ud == null) {
            return ValidationResponse.invalid("Username not found");
        }

        if (ud.hasDefaultPassword()) {
            return oldp.equals(DEFAULT_PASSWORD)
                ? ValidationResponse.isValid()
                : ValidationResponse.invalid("Invalid password");
        } else {
            String encryptedInput = generateSHA1(oldp);
            return encryptedInput.equals(ud.password())
                ? ValidationResponse.isValid()
                : ValidationResponse.invalid("Invalid password");
        }
        
    }
    public ValidationResponse checkNewDetails(UserDetails ud, String n, String oldp, String newp) {
        ValidationResponse fieldsCheck = checkForFieldsPresent(n, oldp, newp);
        if (!ValidationResponse.isValid().result()) {
            return fieldsCheck;
        } 

        if (!USERNAME_PATTERN.matcher(n).matches()) {
            return ValidationResponse.invalid("Username must contain only letters");
        }

        if (ud == null) {
            return ValidationResponse.invalid("Username not found");
        }
        
        if (newp.length() < MINIMUM_PASSWORD_LENGTH) {
            return ValidationResponse.invalid("New password must be at least 8 characters long.");
        }
        if (!PASSWORD_PATTERN.matcher(newp).matches()) {
            return ValidationResponse.invalid("New password must contain at least 8 characters, including 1 uppercase, 1 lowercase, 1 number and 1 special character.");
        }
        if (oldp.equals(newp)) {
            return ValidationResponse.invalid("New password must be different from current password");
        }

        if (ud.hasDefaultPassword()) {
            return oldp.equals(DEFAULT_PASSWORD)
                ? ValidationResponse.isValid()
                : ValidationResponse.invalid("Invalid Old password");
        } else {
            String encryptedInput = generateSHA1(oldp);
            if (!encryptedInput.equals(ud.password())) {
                return ValidationResponse.invalid("Invalid old password");
            }
        }
        return ValidationResponse.isValid();
        
    }
    public ValidationResponse checkForFieldsPresent(String n, String p) {
       
        if (n == null || n.isEmpty()) {
            return ValidationResponse.invalid("Username is required");
        }
        if (p == null || p.isEmpty()) {
            return ValidationResponse.invalid("Password is required");
        }
        return ValidationResponse.isValid();
       
    }
    public ValidationResponse checkForFieldsPresent(String n, String oldp, String newp) {
       
       if (n == null || n.isEmpty()) {
            return ValidationResponse.invalid("Username is required");
        }
        if (oldp == null || oldp.isEmpty()) {
            return ValidationResponse.invalid("Old Password is required");
        }
        if (newp == null || newp.isEmpty()) {
            return ValidationResponse.invalid("New Password is required");
        }
        return ValidationResponse.isValid();
       
    }
}
