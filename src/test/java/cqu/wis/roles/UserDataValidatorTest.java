package cqu.wis.roles;

import cqu.wis.data.UserData.UserDetails;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserDataValidatorTest {

    private final UserDataValidator udv = new UserDataValidator();

    @Test
    void generateSHA1ReturnsCorrectHash() {
        String input = "testPassword";
        String expected = "82f8809f42d911d1bd5199021d69d15ea91d1fad";
        String actual = udv.generateSHA1(input);
        assertEquals(expected, actual);
    }

    @Test
    void checkForFieldsPresentValid() {
        ValidationResponse result = udv.checkForFieldsPresent("username", "password");
        assertTrue(result.result());
    }

    @Test
    void checkForFieldsPresentUsernameEmpty() {
        ValidationResponse result = udv.checkForFieldsPresent("", "password");
        assertFalse(result.result());
        assertEquals("Username is required", result.message());
    }

    @Test
    void checkForFieldsPresentPasswordEmpty() {
        ValidationResponse result = udv.checkForFieldsPresent("username", "");
        assertFalse(result.result());
        assertEquals("Password is required", result.message());
    }

    @Test
    void checkNewDetailsFieldsPresentInvalid() {
        ValidationResponse result = udv.checkForFieldsPresent("", "oldpass", "newpass");
        assertFalse(result.result());
        assertEquals("Username is required", result.message());
    }

    @Test
    void checkCurrentDetailsUsernamePatternInvalid() {
        UserDetails ud = new UserDetails("username", "irrelevant");
        ValidationResponse result = udv.checkCurrentDetails(ud, "user123", "oldpass");
        assertFalse(result.result());
        assertEquals("Username must contain only letters", result.message());
    }

    @Test
    void checkCurrentDetailsUsernameNotFound() {
        ValidationResponse result = udv.checkCurrentDetails(null, "username", "oldpass");
        assertFalse(result.result());
        assertEquals("Username not found", result.message());
    }

    @Test
    void checkCurrentDetailsDefaultPasswordValid() {
        UserDetails ud = new UserDetails("username", "password");
        ValidationResponse result = udv.checkCurrentDetails(ud, "username", "password");
        assertTrue(result.result());
    }

    @Test
    void checkCurrentDetailsDefaultPasswordInvalid() {
        UserDetails ud = new UserDetails("username", "password");
        ValidationResponse result = udv.checkCurrentDetails(ud, "username", "wrongpass");
        assertFalse(result.result());
        assertEquals("Invalid password", result.message());
    }

    @Test
    void checkCurrentDetailsEncryptedPasswordValid() {
        String oldPassword = "oldpass";
        String encrypted = udv.generateSHA1(oldPassword);
        UserDetails ud = new UserDetails("username", encrypted);
        ValidationResponse result = udv.checkCurrentDetails(ud, "username", oldPassword);
        assertTrue(result.result());
    }

    @Test
    void checkCurrentDetailsEncryptedPasswordInvalid() {
        String encrypted = udv.generateSHA1("different");
        UserDetails ud = new UserDetails("username", encrypted);
        ValidationResponse result = udv.checkCurrentDetails(ud, "username", "oldpass");
        assertFalse(result.result());
        assertEquals("Invalid password", result.message());
    }

    @Test
    void checkNewDetailsUsernamePatternInvalid() {
        UserDetails ud = new UserDetails("username", "irrelevant");
        ValidationResponse result = udv.checkNewDetails(ud, "user123", "oldpass", "Newpass1!");
        assertFalse(result.result());
        assertEquals("Username must contain only letters", result.message());
    }

    @Test
    void checkNewDetailsNewPasswordTooShort() {
        UserDetails ud = new UserDetails("username", "irrelevant");
        ValidationResponse result = udv.checkNewDetails(ud, "username", "oldpass", "short");
        assertFalse(result.result());
        assertEquals("New password must be at least 8 characters long.", result.message());
    }

    @Test
    void checkNewDetailsNewPasswordPatternInvalid() {
        UserDetails ud = new UserDetails("username", "irrelevant");
        ValidationResponse result = udv.checkNewDetails(ud, "username", "oldpass", "alllowercase");
        assertFalse(result.result());
        assertEquals("New password must contain at least 8 characters, including 1 uppercase, 1 lowercase, 1 number and 1 special character.", result.message());
    }

    @Test
    void checkNewDetailsNewPasswordSameAsOld() {
        UserDetails ud = new UserDetails("username", "irrelevant");
        String password = "SamePassword1!";
        ValidationResponse result = udv.checkNewDetails(ud, "username", password, password);
        assertFalse(result.result());
        assertEquals("New password must be different from current password", result.message());
    }

    @Test
    void checkNewDetailsDefaultPasswordValid() {
        UserDetails ud = new UserDetails("username", "password");
        ValidationResponse result = udv.checkNewDetails(ud, "username", "password", "Newpass1!");
        assertTrue(result.result());
    }

    @Test
    void checkNewDetailsDefaultPasswordInvalid() {
        UserDetails ud = new UserDetails("username", "password");
        ValidationResponse result = udv.checkNewDetails(ud, "username", "wrongpass", "Newpass1!");
        assertFalse(result.result());
        assertEquals("Invalid Old password", result.message());
    }

    @Test
    void checkNewDetailsEncryptedOldPasswordInvalid() {
        String encrypted = udv.generateSHA1("someOtherPassword");
        UserDetails ud = new UserDetails("username", encrypted);
        ValidationResponse result = udv.checkNewDetails(ud, "username", "oldpass", "Newpass1!");
        assertFalse(result.result());
        assertEquals("Invalid old password", result.message());
    }

    @Test
    void checkNewDetailsEncryptedOldPasswordValid() {
        String oldPassword = "oldpass";
        String encrypted = udv.generateSHA1(oldPassword);
        UserDetails ud = new UserDetails("username", encrypted);
        ValidationResponse result = udv.checkNewDetails(ud, "username", oldPassword, "Newpass1!");
        assertTrue(result.result());
    }
}
