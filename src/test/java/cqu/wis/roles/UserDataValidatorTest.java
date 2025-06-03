package cqu.wis.roles;

import cqu.wis.data.UserData.UserDetails;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link UserDataValidator} class.
 *
 * This test class verifies the functionality of the {@code UserDataValidator} methods, ensuring proper validation
 * of user data, including field presence, username and password patterns and secure password hashing.
 *
 * Each test ensures that edge cases, errors, and normal scenarios are handled correctly by the validator.
 *
 * @author Ayush Bhandari S12157470
 */
public class UserDataValidatorTest {

    private final UserDataValidator udv = new UserDataValidator();

    /**
     * Tests that the {@code generateSHA1} method returns the correct SHA-1 hash for a given input string.
     */
    @Test
    void generateSHA1ReturnsCorrectHash() {
        String input = "testPassword";
        String expected = "82f8809f42d911d1bd5199021d69d15ea91d1fad";
        String actual = udv.generateSHA1(input);
        assertEquals(expected, actual);
    }

    /**
     * Tests that the field presence validation passes when both username and password are provided.
     */
    @Test
    void checkForFieldsPresentValid() {
        ValidationResponse result = udv.checkForFieldsPresent("username", "password");
        assertTrue(result.result());
    }

    /**
     * Tests that the field presence validation fails when the username is empty.
     */
    @Test
    void checkForFieldsPresentUsernameEmpty() {
        ValidationResponse result = udv.checkForFieldsPresent("", "password");
        assertFalse(result.result());
        assertEquals("Username is required", result.message());
    }

    /**
     * Tests that the field presence validation fails when the password is empty.
     */
    @Test
    void checkForFieldsPresentPasswordEmpty() {
        ValidationResponse result = udv.checkForFieldsPresent("username", "");
        assertFalse(result.result());
        assertEquals("Password is required", result.message());
    }

    /**
     * Tests that the field presence validation for new details fails when the username is missing.
     */
    @Test
    void checkNewDetailsFieldsPresentInvalid() {
        ValidationResponse result = udv.checkForFieldsPresent("", "oldpass", "newpass");
        assertFalse(result.result());
        assertEquals("Username is required", result.message());
    }

    /**
     * Tests that the username pattern validation fails when the username contains numbers.
     */
    @Test
    void checkCurrentDetailsUsernamePatternInvalid() {
        UserDetails ud = new UserDetails("username", "irrelevant");
        ValidationResponse result = udv.checkCurrentDetails(ud, "user123", "oldpass");
        assertFalse(result.result());
        assertEquals("Username must contain only letters", result.message());
    }

    /**
     * Tests that the validation fails when the username is not found (null user details).
     */
    @Test
    void checkCurrentDetailsUsernameNotFound() {
        ValidationResponse result = udv.checkCurrentDetails(null, "username", "oldpass");
        assertFalse(result.result());
        assertEquals("Username not found", result.message());
    }

    /**
     * Tests that the validation passes when using the default password.
     */
    @Test
    void checkCurrentDetailsDefaultPasswordValid() {
        UserDetails ud = new UserDetails("username", "password");
        ValidationResponse result = udv.checkCurrentDetails(ud, "username", "password");
        assertTrue(result.result());
    }

    /**
     * Tests that the validation fails when the provided password does not match the default password.
     */
    @Test
    void checkCurrentDetailsDefaultPasswordInvalid() {
        UserDetails ud = new UserDetails("username", "password");
        ValidationResponse result = udv.checkCurrentDetails(ud, "username", "wrongpass");
        assertFalse(result.result());
        assertEquals("Invalid password", result.message());
    }

    /**
     * Tests that validation passes when the encrypted password matches the provided plain text password.
     */
    @Test
    void checkCurrentDetailsEncryptedPasswordValid() {
        String oldPassword = "oldpass";
        String encrypted = udv.generateSHA1(oldPassword);
        UserDetails ud = new UserDetails("username", encrypted);
        ValidationResponse result = udv.checkCurrentDetails(ud, "username", oldPassword);
        assertTrue(result.result());
    }

    /**
     * Tests that validation fails when the encrypted password does not match the provided plain text password.
     */
    @Test
    void checkCurrentDetailsEncryptedPasswordInvalid() {
        String encrypted = udv.generateSHA1("different");
        UserDetails ud = new UserDetails("username", encrypted);
        ValidationResponse result = udv.checkCurrentDetails(ud, "username", "oldpass");
        assertFalse(result.result());
        assertEquals("Invalid password", result.message());
    }

    /**
     * Tests that the new details validation fails when the username contains numbers.
     */
    @Test
    void checkNewDetailsUsernamePatternInvalid() {
        UserDetails ud = new UserDetails("username", "irrelevant");
        ValidationResponse result = udv.checkNewDetails(ud, "user123", "oldpass", "Newpass1!");
        assertFalse(result.result());
        assertEquals("Username must contain only letters", result.message());
    }

    /**
     * Tests that the new password validation fails when the new password is too short.
     */
    @Test
    void checkNewDetailsNewPasswordTooShort() {
        UserDetails ud = new UserDetails("username", "irrelevant");
        ValidationResponse result = udv.checkNewDetails(ud, "username", "oldpass", "short");
        assertFalse(result.result());
        assertEquals("New password must be at least 8 characters long.", result.message());
    }

    /**
     * Tests that the new password validation fails when it does not meet the required pattern.
     */
    @Test
    void checkNewDetailsNewPasswordPatternInvalid() {
        UserDetails ud = new UserDetails("username", "irrelevant");
        ValidationResponse result = udv.checkNewDetails(ud, "username", "oldpass", "alllowercase");
        assertFalse(result.result());
        assertEquals("New password must contain at least 8 characters, including 1 uppercase, 1 lowercase, 1 number and 1 special character.", result.message());
    }

    /**
     * Tests that the new password validation fails when the new password is the same as the old password.
     */
    @Test
    void checkNewDetailsNewPasswordSameAsOld() {
        UserDetails ud = new UserDetails("username", "irrelevant");
        String password = "SamePassword1!";
        ValidationResponse result = udv.checkNewDetails(ud, "username", password, password);
        assertFalse(result.result());
        assertEquals("New password must be different from current password", result.message());
    }

    /**
     * Tests that validation passes when using the default password and a valid new password.
     */
    @Test
    void checkNewDetailsDefaultPasswordValid() {
        UserDetails ud = new UserDetails("username", "password");
        ValidationResponse result = udv.checkNewDetails(ud, "username", "password", "Newpass1!");
        assertTrue(result.result());
    }

    /**
     * Tests that the new details validation fails when the old password does not match the default password.
     */
    @Test
    void checkNewDetailsDefaultPasswordInvalid() {
        UserDetails ud = new UserDetails("username", "password");
        ValidationResponse result = udv.checkNewDetails(ud, "username", "wrongpass", "Newpass1!");
        assertFalse(result.result());
        assertEquals("Invalid Old password", result.message());
    }

    /**
     * Tests that the new details validation fails when the old password does not match the encrypted stored password.
     */
    @Test
    void checkNewDetailsEncryptedOldPasswordInvalid() {
        String encrypted = udv.generateSHA1("someOtherPassword");
        UserDetails ud = new UserDetails("username", encrypted);
        ValidationResponse result = udv.checkNewDetails(ud, "username", "oldpass", "Newpass1!");
        assertFalse(result.result());
        assertEquals("Invalid old password", result.message());
    }

    /**
     * Tests that validation passes when the old password (in plain text) matches the stored encrypted password and a valid new password is provided.
     */
    @Test
    void checkNewDetailsEncryptedOldPasswordValid() {
        String oldPassword = "oldpass";
        String encrypted = udv.generateSHA1(oldPassword);
        UserDetails ud = new UserDetails("username", encrypted);
        ValidationResponse result = udv.checkNewDetails(ud, "username", oldPassword, "Newpass1!");
        assertTrue(result.result());
    }
}
