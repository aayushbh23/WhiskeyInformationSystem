package cqu.wis.roles;

import cqu.wis.data.UserData;
import cqu.wis.data.UserData.UserDetails;
/**
 *
 * @author Ayush Bhandari S12157470
 */
public class UserDataManager {
    private final UserData ud;
    
    public UserDataManager(UserData ud) {
        this.ud = ud;
    }
    
    public UserDetails findUser(String name) {
        return null;
    }
    
    public int updatePassword(String user, String password) {
        return 0;
    }
    
    public void disconnect() {
        ud.disconnect();
    }
    
}
