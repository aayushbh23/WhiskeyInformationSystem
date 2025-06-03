package cqu.wis.roles;

import cqu.wis.data.UserData;
import cqu.wis.data.UserData.UserDetails;
import java.sql.SQLException;

/**
 * Provides a higher-level interface for managing user authentication data.
 *
 * This class acts as a controller that uses the {@link UserData} data access object
 * to perform operations related to user records, such as:
 * Finding a user by username
 * Updating a user's password
 * Disconnecting from the user database
 *  
 * @author Ayush Bhandari S12157470
 */
public class UserDataManager {

    private final UserData ud;

    /**
     * Constructs a new {@code UserDataManager} using the given {@link UserData} data source.
     *
     * @param ud the user data source used to retrieve and update user information
     */
    public UserDataManager(UserData ud) {
        this.ud = ud;
    }

    /**
     * Finds a user record by username.
     *
     * @param name the username to search for
     * @return a {@link UserDetails} object if the user exists; {@code null} otherwise
     * @throws SQLException if a database access error occurs
     */
    public UserDetails findUser(String name) throws SQLException {
        return ud.getUser(name);
    }

    /**
     * Updates the password for a given username.
     *
     * @param user the username whose password is to be updated
     * @param password the new password to set
     * @return {@code 1} if the update was successful (at least one row affected), {@code 0} otherwise
     * @throws SQLException if a database access error occurs
     */
    public int updatePassword(String user, String password) throws SQLException {
        return ud.updatePassword(user, password);
    }

    /**
     * Disconnects from the underlying user database.
     *
     * @throws SQLException if a database disconnection error occurs
     */
    public void disconnect() throws SQLException {
        ud.disconnect();
    }
}
