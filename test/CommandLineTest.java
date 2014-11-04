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
public class CommandLineTest {
    
    public CommandLineTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of parseCMD method, of class CommandLine.
     */
    @Test
    public void testParseCMD() {
        System.out.println("parseCMD");
        String cmd = "";
        Server.ClientThread thread = null;
        CommandLine instance = null;
        boolean expResult = false;
        boolean result = instance.parseCMD(cmd, thread);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
