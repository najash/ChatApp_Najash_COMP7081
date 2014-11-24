package com.najash.client;


import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;

/*
 * The Client that can be run both as a console or a GUI
 */
public class Client  {

	// for I/O
	private ObjectInputStream sInput;		// to read from the socket
	private ObjectOutputStream sOutput;		// to write on the socket
	private Socket socket;

	// if I use a GUI or not
	private ClientWrapper cg;
	
	// the server, the port and the username
	private String server, username, password;
	private int port;

	/*
	 *  Constructor called by console mode
	 *  server: the server address
	 *  port: the port number
	 *  username: the username
	 */
	Client(String server, int port, String password, String username) {
		// which calls the common constructor with the GUI set to null
		this(server, port, username, password, null);
	}

	/*
	 * Constructor call when used from a GUI
	 * in console mode the ClienGUI parameter is null
	 */
	Client(String server, int port, String username, String password, ClientWrapper cg) {
                try {
                    System.getSecurityManager().checkPermission(new SocketPermission(server, "connect, resolve"));
                }
                catch (Exception ex) {
                   cg.print(ex.getMessage() + "\n");
                }
                
		this.server = server;
		this.port = port;
		this.username = username;
                this.password = password;
		// save if we are in GUI mode or not
		this.cg = cg;
	}
	
	/*
	 * To start the dialog
	 */
	public boolean start() {
		// try to connect to the server
		try {
			socket = new Socket(server, port);
		} 
		// if it failed not much I can so
		catch(Exception ec) {
			display("Error connectiong to server:" + ec);
			return false;
		}
		
		String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
		display(msg);
	
		/* Creating both Data Stream */
		try
		{
			sInput  = new ObjectInputStream(socket.getInputStream());
			sOutput = new ObjectOutputStream(socket.getOutputStream());
		}
		catch (IOException eIO) {
			display("Exception creating new Input/output Streams: " + eIO);
			return false;
		}

		// creates the Thread to listen from the server 
		new ListenFromServer().start();
		// Send our username to the server this is the only message that we
		// will send as a String. All other messages will be ChatMessage objects
		try
		{
			sOutput.writeObject(username);
                        sOutput.writeObject(password);
		}
		catch (IOException eIO) {
			display("Exception doing login : " + eIO);
			disconnect();
			return false;
		}
		// success we inform the caller that it worked
		return true;
	}

	/*
	 * To send a message to the console or the GUI
	 */
	private void display(String msg) {
		if(cg == null) {
                        cg.print("Error: " + msg +".\n");
			System.out.println(msg);      // println in console mode
                }
		else
			cg.print(msg + "\n");		// append to the ClientGUI JTextArea (or whatever)
	}
	
	/*
	 * To send a message to the server
	 */
	boolean sendMessage(ChatMessage msg) {
		try {
			sOutput.writeObject(msg);
		}
		catch(IOException e) {
			display("Exception writing to server: " + e);
                        return false;
		}
                
                return true;
	}

	/*
	 * When something goes wrong
	 * Close the Input/Output streams and disconnect not much to do in the catch clause
	 */
	private void disconnect() {
		try { 
			if(sInput != null) sInput.close();
		}
		catch(Exception e) {} // not much else I can do
		try {
			if(sOutput != null) sOutput.close();
		}
		catch(Exception e) {} // not much else I can do
        try{
			if(socket != null) socket.close();
		}
		catch(Exception e) {} // not much else I can do
		
		// inform the GUI
		if(cg != null)
			cg.connectionFailed();
			
	}
	
        public void handleSystemMessage(String msg) {
            if (msg.substring(0, 4).equals("<~>a")) {
                cg.print("Logged in as Admin\n");
            } else if (msg.substring(0, 4).equals("<~>u")) {
                cg.print("Logged in as User\n");
            } else if (msg.substring(0, 7).equals("<~>list")) {
                cg.print(msg.substring(7));
            }  else if (msg.substring(0, 7).equals("<~>room")) {
                cg.print(msg.substring(7));
            }
        }

	/*
	 * a class that waits for the message from the server and append them to the JTextArea
	 * if we have a GUI or simply System.out.println() it in console mode
	 */
	class ListenFromServer extends Thread {

		public void run() {
			while(true) {
				try {
					String msg = (String) sInput.readObject();
                                        
                                        if (msg.substring(0, 3).equals("<~>")) { //check if the message is for the system
                                            handleSystemMessage(msg);
                                        } else {
                                            // if console mode print the message and add back the prompt
                                            if(cg == null) {
                                                    System.out.println(msg);
                                                    System.out.print("> ");
                                            }
                                            else {
                                                    cg.print(msg);
                                            }
                                        }
				}
				catch(IOException e) {
					display("Server has close the connection. Logged out");
					if(cg != null) 
						cg.connectionFailed();
					break;
				}
				// can't happen with a String object but need the catch anyhow
				catch(ClassNotFoundException e2) {
				}
			}
		}
	}
}

