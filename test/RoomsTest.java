/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.ArrayList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author a00813191
 */
public class RoomsTest {
    
    public RoomsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of addRoom method, of class Rooms.
     */
    @Test
    public void testAddRoom() {
        System.out.println("addRoom");
        String name = "";
        Rooms instance = new Rooms();
        boolean expResult = false;
        boolean result = instance.addRoom(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteRoom method, of class Rooms.
     */
    @Test
    public void testDeleteRoom() {
        System.out.println("deleteRoom");
        String name = "";
        Rooms instance = new Rooms();
        boolean expResult = false;
        boolean result = instance.deleteRoom(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRooms method, of class Rooms.
     */
    @Test
    public void testGetRooms() {
        System.out.println("getRooms");
        Rooms instance = new Rooms();
        ArrayList<String> expResult = null;
        ArrayList<String> result = instance.getRooms();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createTable method, of class Rooms.
     */
    @Test
    public void testCreateTable() {
        System.out.println("createTable");
        Rooms instance = new Rooms();
        instance.createTable();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of dropTable method, of class Rooms.
     */
    @Test
    public void testDropTable() {
        System.out.println("dropTable");
        Rooms instance = new Rooms();
        boolean expResult = false;
        boolean result = instance.dropTable();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
