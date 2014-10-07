
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author A00807688
 */
public class Rooms extends Database {

    public boolean addRoom(String name) {
        return execute("INSERT INTO rooms VALUES('" + name + "');");
    }
    
    public boolean deleteRoom(String name) {
        return execute("DELETE FROM rooms WHERE name='" + name + "';");
    }
    
    public ArrayList<String> getRooms() {
        ArrayList<String> list = new ArrayList<String>();
        
        ResultSet rs = executeQuery("SELECT * FROM rooms;");
    
        try {
            while (rs.next()) {
                list.add(rs.getString("name"));
            }
        } catch (SQLException ex) { }
        
        return list;
    }
    
    @Override
    protected void createTable() {
        execute("CREATE TABLE IF NOT EXISTS rooms (name string, PRIMARY KEY (name));");
        
        ResultSet rs = executeQuery("SELECT * FROM rooms;");
        try {
            if (!rs.next()) {
                addRoom("default");
            }
        } catch (SQLException ex) { }
        finally {
            close();
        }
    }

    @Override
    boolean dropTable() {
        return execute("DROP TABLE rooms;");
    }
    
}
