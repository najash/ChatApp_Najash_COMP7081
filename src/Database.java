
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author A00807688
 */
public abstract class Database {
    private Connection connection;
    
    public Database() {
        try {
            // load the sqlite-JDBC driver using the current class loader
            Class.forName("org.sqlite.JDBC");
            init();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Connect to the database
     * @return connection object
     */
    protected Connection connect() {
        try {
            return DriverManager.getConnection("jdbc:sqlite:db.db");
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
        
    /**
     * Create a table if not exist
     */
    abstract protected void createTable();
    
    /**
     * Delete a table
     * @return true if deleted successfully
     */
    abstract boolean dropTable();
    
    private void init() {
        createTable();
    }
    
    protected boolean execute(String sql) {
        Connection conn = connect();
        
        if (conn != null) {
            try {
                Statement statement = conn.createStatement();
                statement.execute(sql);
                conn.close();
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return false;
    }
    
    protected void close() {
        if (connection != null)
            try {
                connection.close();
        } catch (SQLException ex) {
        }
    }
    
    protected ResultSet executeQuery(String sql) {
        connection = connect();
        
        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                statement.execute(sql);
                return statement.executeQuery(sql);
            } catch (SQLException ex) {
                Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return null;
    }
}
