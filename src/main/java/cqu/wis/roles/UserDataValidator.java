package cqu.wis.roles;

import cqu.wis.data.UserData.UserDetails;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

/**
 * Provides validation logic for user-related input, such as usernames and passwords.
 * 
 * This class is responsible for:
 * Validating user credentials during login and password changes
 * Enforcing password complexity rules
 * Encrypting plain text passwords using SHA-1
 * Standardizing validation responses with error messages when applicable
 * It is used to ensure security and data integrity throughout the application.
 * 
 * @author Ayush Bhandari S12157470
 */
public class UserDataValidator {

    /** Minimum length for valid passwords. */
    private static final int MINIMUM_PASSWORD_LENGTH = 8;

    /** Default password value (used for initial accounts or resets). */
    private static final String DEFAULT_PASSWORD = "password";

    /** Pattern for validating usernames (letters only). */
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z]+$");

    /** Pattern for enforcing password complexity. */
    public static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{8,}$"
    );

    /**
     * Default constructor for {@code UserDataValidator}.
     */
    public void UserDataValidator() {}

    /**
     * Generates a SHA-1 hash of the given input string.
     *
     * @param s the string to hash
     * @return the SHA-1 hash of the input as a hexadecimal string
     * @throws RuntimeException if the SHA-1 algorithm is not available
     */
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

    /**
     * Validates the current user details during login or password change.
     * Checks for presence of required fields (username, password).
     * Validates username pattern.
     * Verifies the old password (either default or encrypted).
     *
     * @param ud the stored user details
     * @param n the username provided by the user
     * @param oldp the current password provided by the user
     * @return a {@link ValidationResponse} indicating whether validation succeeded
     */
    public ValidationResponse checkCurrentDetails(UserDetails ud, String n, String oldp) {
        ValidationResponse fieldsCheck = checkForFieldsPresent(n, oldp);
        if (!fieldsCheck.result()) {
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

    /**
     * Validates the new user details when changing a password.
     * Checks for presence of required fields (username, old password, new password).
     * Validates username pattern.
     * Enforces new password length and complexity requirements.
     * Ensures new password is different from the old password.
     * Verifies old password correctness (default or encrypted).
     *
     * @param ud the stored user details
     * @param n the username provided by the user
     * @param oldp the current password provided by the user
     * @param newp the new password the user wants to set
     * @return a {@link ValidationResponse} indicating whether validation succeeded
     */
    public ValidationResponse checkNewDetails(UserDetails ud, String n, String oldp, String newp) {
        ValidationResponse fieldsCheck = checkForFieldsPresent(n, oldp, newp);
        if (!fieldsCheck.result()) {
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
            return ValidationResponse.invalid(
                "New password must contain at least 8 characters, including 1 uppercase, 1 lowercase, 1 number and 1 special character."
            );
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

    /**
     * Checks that both username and password fields are present (non-null and non-empty).
     *
     * @param n the username
     * @param p the password
     * @return a {@link ValidationResponse} indicating the result and error message if missing
     */
    public ValidationResponse checkForFieldsPresent(String n, String p) {
        if (n == null || n.isEmpty()) {
            return ValidationResponse.invalid("Username is required");
        }
        if (p == null || p.isEmpty()) {
            return ValidationResponse.invalid("Password is required");
        }
        return ValidationResponse.isValid();
    }

    /**
     * Checks that username, old password and new password fields are present (non-null and non-empty).
     *
     * @param n the username
     * @param oldp the old password
     * @param newp the new password
     * @return a {@link ValidationResponse} indicating the result and error message if missing
     */
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
