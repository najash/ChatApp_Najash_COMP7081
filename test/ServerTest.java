/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author a00813191
 */
public class ServerTest {
    
    public ServerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of start method, of class Server.
     */
    @Test
    public void testStart() {
        System.out.println("start");
        Server instance = null;
        instance.start();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of stop method, of class Server.
     */
    @Test
    public void testStop() {
        System.out.println("stop");
        Server instance = null;
        instance.stop();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clear method, of class Server.
     */
    @Test
    public void testClear() {
        System.out.println("clear");
        Server instance = null;
        instance.clear();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of remove method, of class Server.
     */
    @Test
    public void testRemove() {
        System.out.println("remove");
        int id = 0;
        Server instance = null;
        instance.remove(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class Server.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        Server.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getClientThread method, of class Server.
     */
    @Test
    public void testGetClientThread() {
        System.out.println("getClientThread");
        String username = "";
        Server instance = null;
        Server.ClientThread expResult = null;
        Server.ClientThread result = instance.getClientThread(username);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of broadcastRooms method, of class Server.
     */
    @Test
    public void testBroadcastRooms() {
        System.out.println("broadcastRooms");
        Server instance = null;
        instance.broadcastRooms();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
