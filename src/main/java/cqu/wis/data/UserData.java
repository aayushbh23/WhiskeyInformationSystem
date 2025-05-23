package cqu.wis.data;

import java.sql.*;

/**
 *
 * @author Ayush Bhandari S12157470
 */
public class UserData {
    
    public record UserDetails(String user, String Password){}
    
    private Connection connection;
    
    public void UserData(){}
    
    public void connect() {
        
    }
    
    public void disconnect() {
        
    }
    
    public UserDetails findUser(String username) {
        
        return null;
        
    }
    
    public int updatePassword(String n, String p) {
        
        return 0;
        
    }
}
