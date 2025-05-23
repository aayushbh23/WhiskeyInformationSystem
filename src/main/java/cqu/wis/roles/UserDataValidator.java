package cqu.wis.roles;

import cqu.wis.data.UserData.UserDetails;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Ayush Bhandari S12157470
 */
public class UserDataValidator {
    private static final int MINIMUM_PASSWORD_LENGTH = 8;

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
    
    public void UserDataValidator(){
        
    }
   
    public ValidationResponse checkCurrentDetails(UserDetails ud, String n, String oldp) {
        
        return null;
        
    }
    public ValidationResponse checkNewDetails(UserDetails ud, String n, String oldp, String newp) {
        
        return null;
        
    }
    public ValidationResponse checkForFieldsPresent(String n, String p) {
       
        return null;
       
    }
    public ValidationResponse checkForFieldsPresent(String n, String oldp, String newp) {
       
        return null;
       
    }
}
