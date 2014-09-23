
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author A00807688
 */
public class Users extends Database {
    public enum UserType {
        ADMIN, USER, GUEST;
    }
    
    public boolean addUser(String user, String pass, UserType type) {
        return execute("INSERT INTO users VALUES('" + user + "','" + pass + "'," + type.ordinal() + ");");
    }
    
    public boolean deleteUser(String user) {
        return execute("DELETE FROM users WHERE user='" + user + "';");
    }
    
    public boolean changePassword(String user, String newPass) {
        return execute("UPDATE users SET pass='" + newPass + "' WHERE user='" + user + "';");
    }
    
    public boolean changeUserType(String user, UserType type) {
        return execute("UPDATE users SET type=" + type.ordinal() + " WHERE user='" + user + "';");
    }
    
    public String getPassword(String username) {
        ResultSet rs = executeQuery("SELECT pass FROM users WHERE user='" + username + "';");
        
        if (rs != null) {
            try {
                rs.next();
                return rs.getString("pass");
            } catch (SQLException ex) {
                Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return null;
    }
    
    public UserType getUserType(String username) {
        ResultSet rs = executeQuery("SELECT type FROM users WHERE user='" + username + "';");
        
        if (rs != null) {
            try {
                rs.next();
                int type = rs.getInt("type");
                return UserType.values()[type];
            } catch (SQLException ex) {
                Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return null;
    }

    @Override
    boolean dropTable() {
        return execute("DROP TABLE users;");
    }

    @Override
    protected void createTable() {
        execute("CREATE TABLE IF NOT EXISTS users (user string, pass string, type integer);");
    }
}
