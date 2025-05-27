package cqu.wis.roles;

import cqu.wis.data.UserData;
import cqu.wis.data.UserData.UserDetails;
import java.sql.SQLException;
/**
 *
 * @author Ayush Bhandari S12157470
 */
public class UserDataManager {
    private final UserData ud;
    
    public UserDataManager(UserData ud) {
        this.ud = ud;
    }
    
    public UserDetails findUser(String name) throws SQLException{
        return ud.getUser(name);
    }
    
    public int updatePassword(String user, String password) throws SQLException{
        return ud.updatePassword(user, password);
    }
    
    public void disconnect() throws SQLException{
        ud.disconnect();
    }
    
}
