package com.najash.client;

/*
 * The Client with its GUI
 */
public class ClientWrapper {
    private static final long serialVersionUID = 1L;
    
    Client client;
    String message;
            
    // called by the GUI is the connection failed
    // we reset our buttons, label, textfield
    void connectionFailed() {

    }
    
    public void init() {
        client = new Client("127.0.0.1", 1500, "admin", "pass", this);
    }
    
    public boolean test() {
        if (!client.start()) {
            System.out.println("Connection fail.");
            return false;
        }
        
        ChatMessage helloMessage = new ChatMessage(ChatMessage.MESSAGE, "Hello Chat.");
        ChatMessage logoutMessage = new ChatMessage(ChatMessage.LOGOUT, "");
        
        if (!client.sendMessage(helloMessage)) {
            System.out.println("Connection fail.");
            return false;
        }
        
        if (!client.sendMessage(logoutMessage)) {
            System.out.println("Connection fail.");
            return false;
        }
        
        return true;
    }
	
    void print(String string) {
        message = string;
        System.out.println(string);
    }
    
    public String getMessage() {
        return message;
    }
}

