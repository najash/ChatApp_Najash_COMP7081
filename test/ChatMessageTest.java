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
public class ChatMessageTest {
    
    public ChatMessageTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of getType method, of class ChatMessage.
     */
    @Test
    public void testGetType() {
        System.out.println("ChatMessageTest - testGetType #1 - getType()");
        ChatMessage instance = new ChatMessage(0, "Test");
        int expResult = 0;
        assertFalse(expResult+1 == instance.getType());
        assertTrue(expResult == instance.getType());
        // TODO review the generated test code and remove the default call to fail.
        //fail("GetType() failed to return the expected type");
    }

    /**
     * Test of getMessage method, of class ChatMessage.
     */
    @Test
    public void testGetMessage() {
        System.out.println("ChatMessageTest - testGetMessage #1 - getMessage()");
        ChatMessage test = new ChatMessage(0, "Hello World");
        String expResult = "Hello World";
        assertTrue(test.getMessage().equals(expResult));
        // TODO review the generated test code and remove the default call to fail.
        //fail("GetMessage() failed to returned " + test.getMessage() + " by returning " + expResult);
    }
    
}
