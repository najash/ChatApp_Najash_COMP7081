import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 * The server that can be run both as a console application or a GUI
 */
public class Server {
	// a unique ID for each connection
	private static int uniqueId;
        private HashMap<String, HashMap<String, ClientThread>> rooms;
        
	// if I am in a GUI
	private ServerGUI sg;
	// to display time
	private SimpleDateFormat sdf;
	// the port number to listen for connection
	private int port;
	// the boolean that will be turned of to stop the server
	private boolean keepGoing;
	
        Users users = new Users();

        CommandLine cmdLine = new CommandLine(this);
	/*
	 *  server constructor that receive the port to listen to for connection as parameter
	 *  in console
	 */
	public Server(int port) {
		this(port, null);
	}
	
	public Server(int port, ServerGUI sg) {
		// GUI or not
		this.sg = sg;
		// the port
		this.port = port;
		// to display hh:mm:ss
		sdf = new SimpleDateFormat("HH:mm:ss");
		// ArrayList for the Client list
                rooms = new HashMap<String, HashMap<String, ClientThread>>();
                Rooms rms = new Rooms();
                
                for (String room: rms.getRooms()) {
                   rooms.put(room, new HashMap<String, ClientThread>());
                }
	}
	
	public void start() {
		keepGoing = true;
		/* create socket server and wait for connection requests */
		try 
		{
			// the socket used by the server
			ServerSocket serverSocket = new ServerSocket(port);

			// infinite loop to wait for connections
			while(keepGoing) 
			{
				// format message saying we are waiting
				display("Server waiting for Clients on port " + port + ".");
				
				Socket socket = serverSocket.accept();  	// accept connection
				// if I was asked to stop
				if(!keepGoing)
                                    break;
				
                                ClientThread t = new ClientThread(socket);  // make a thread of it
                                String pass = users.getPassword(t.username);
                                if (pass == null) { //new user
                                    t.writeMsg("Invalid username or password\n");
                                    t.close();
                                } else if(t.password.equals(pass)) {
                                    t.start();
                                    broadcastUserList();
                                } else { //password wrong
                                    t.writeMsg("Invalid username or password\n");
                                    t.close();
                                }
			}
			// I was asked to stop
			try {   
				serverSocket.close();
                                for (int i = 0; i < rooms.size(); i++) {
                                    HashMap<String, ClientThread> clients = (HashMap<String, ClientThread>)rooms.values().toArray()[i];
                                    for(int j = 0; j < clients.size(); ++j) {
                                            ClientThread tc = (ClientThread)clients.values().toArray()[j];
                                            try {
                                            tc.sInput.close();
                                                tc.sOutput.close();
                                                tc.socket.close();
                                                }
                                                catch(IOException ioE) {
                                                        // not much I can do
                                                }
                                    }
                                }
			}
			catch(Exception e) {
				display("Exception closing the server and clients: " + e);
			}
		}
		// something went bad
		catch (IOException e) {
                        String msg = sdf.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
			display(msg);
		}
	}		
    /*
     * For the GUI to stop the server
     */
	protected void stop() {
		keepGoing = false;
		// connect to myself as Client to exit statement 
		// Socket socket = serverSocket.accept();
		try {
			new Socket("localhost", port);
		}
		catch(Exception e) {
			// nothing I can really do
		}
	}
	/*
	 * Display an event (not a message) to the console or the GUI
	 */
	private void display(String msg) {
		String time = sdf.format(new Date()) + " " + msg;
		if(sg == null)
			System.out.println(time);
		else
			sg.appendEvent(time + "\n");
	}
        
        private String getUserList()
        {
            String str = "";
            for (int i = 0; i < rooms.size(); i++) {
                    HashMap<String, ClientThread> clients = (HashMap<String, ClientThread>)rooms.values().toArray()[i];
                    Iterator<String> it = clients.keySet().iterator();
                    
                    while(it.hasNext())
                    {
                        str += (String)it.next() + "\n";
                    }
            }
            return str;
        }
        
        private void broadcastUserList()
        {
            String str = getUserList();
            for (int i = 0; i < rooms.size(); i++) {
                    HashMap<String, ClientThread> clients = (HashMap<String, ClientThread>)rooms.values().toArray()[i];
                    Iterator<ClientThread> it = clients.values().iterator();
                    while(it.hasNext())
                    {
                        ClientThread temp = it.next();
                        temp.writeMsg("<~>list" + str);
                    }
            }
        }
	/*
	 *  to broadcast a message to all Clients
	 */
	private synchronized void broadcast(String message, String chatRoom) {
		// add HH:mm:ss and \n to the message
		String time = sdf.format(new Date());
		String messageLf = time + " " + message + "\n";
		// display message on console or GUI
		if(sg == null)
			System.out.print(messageLf);
		else
			sg.appendRoom(messageLf);     // append in the room window
		
		// we loop in reverse order in case we would have to remove a Client
		// because it has disconnected
                
                HashMap<String, ClientThread> clients = rooms.get(chatRoom);
                
                if (clients != null) {
                    for(int j = clients.size(); --j >= 0;) {
                            ClientThread ct = (ClientThread)clients.values().toArray()[j];
                            // try to write to the Client if it fails remove it from the list
                            if(!ct.writeMsg(messageLf)) {
                                    clients.remove(j);
                                    display("Disconnected Client " + ct.username + " removed from list.");
                            }
                    }
                }
	}

	/*
	 *  to broadcast a clear to all Clients
	 */
	public synchronized void clear() {

		// display message on console or GUI
		if(sg == null)
			System.out.print("\b\b\b\b\b");
		else
			sg.appendRoom("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");     // append in the room window

	} 
    

	// for a client who logoff using the LOGOUT message
	synchronized void remove(int id) {
		// scan the array list until we found the Id
            for (int i = 0; i < rooms.size(); i++) {
                    HashMap<String, ClientThread> clients = (HashMap<String, ClientThread>)rooms.values().toArray()[i];
                    for(int j = 0; j < clients.size(); ++j) {
                            ClientThread ct = (ClientThread)clients.values().toArray()[j];
                            // found it
                            if(ct.id == id) {
                                    clients.remove(j);
                                    return;

                            }
                    }
            }
	}
	
	/*
	 *  To run as a console application just open a console window and: 
	 * > java Server
	 * > java Server portNumber
	 * If the port number is not specified 1500 is used
	 */ 
	public static void main(String[] args) {
		// start server on port 1500 unless a PortNumber is specified 
		int portNumber = 1500;
		switch(args.length) {
			case 1:
				try {
					portNumber = Integer.parseInt(args[0]);
				}
				catch(Exception e) {
					System.out.println("Invalid port number.");
					System.out.println("Usage is: > java Server [portNumber]");
					return;
				}
			case 0:
				break;
			default:
				System.out.println("Usage is: > java Server [portNumber]");
				return;
				
		}
		// create a server object and start it
		Server server = new Server(portNumber);
		server.start();
	}
        
        

        public ClientThread getClientThread(String username) {
            ClientThread client = null;
            for (int i = 0; i < rooms.size(); i++) {
                    HashMap<String, ClientThread> clients = (HashMap<String, ClientThread>)rooms.values().toArray()[i];
                    if ((client = clients.get(username)) != null) {
                        break;                        
                    }
            }
            return client;
        } 

        void broadcastRooms() {
            String list = createRoomList();
            for (int i = 0; i < rooms.size(); i++) {
                    HashMap<String, ClientThread> clients = (HashMap<String, ClientThread>)rooms.values().toArray()[i];
                    for(int j = clients.size(); --j >= 0;) {
                            ClientThread ct = (ClientThread)clients.values().toArray()[j];
                            ct.writeMsg("<~>room" + list);
                    }
            }
        }
        
        private String createRoomList() {
            String roomList = "";
            Rooms rooms = new Rooms();
            ArrayList<String> list = rooms.getRooms();

            for (String room: list) {
                roomList += room + ",";
            }
            return roomList;
        }

	/** One instance of this thread will run for each client */
	class ClientThread extends Thread {
		// the socket where to listen/talk
		Socket socket;
		ObjectInputStream sInput;
		ObjectOutputStream sOutput;
		// my unique id (easier for deconnection)
		int id;
		// the Username of the Client
		String username;
                String password;
                Users.UserType type;
		// the only type of message a will receive
		ChatMessage cm;
		// the date I connect
		String date;
                
                String chatRoom;

		// Constructore
		ClientThread(Socket socket) {
			// a unique id
			id = ++uniqueId;
			this.socket = socket;
			/* Creating both Data Stream */
			System.out.println("Thread trying to create Object Input/Output Streams");
			try
			{
				// create output first
				sOutput = new ObjectOutputStream(socket.getOutputStream());
				sInput  = new ObjectInputStream(socket.getInputStream());
				// read the username
				username = (String) sInput.readObject();
                                password = (String) sInput.readObject();
			}
			catch (IOException e) {
				display("Exception creating new Input/output Streams: " + e);
				return;
			}
			// have to catch ClassNotFoundException
			// but I read a String, I am sure it will work
			catch (ClassNotFoundException e) {
			}
                        date = new Date().toString() + "\n";
		}
                
                public void sendRoomList() {
                    String roomList = createRoomList();
                    writeMsg("<~>room" + roomList);
                }
                
		// what will run forever
		public void run() {
			display(username + " just connected.");
			// to loop until LOGOUT
                        type = users.getUserType(username);
                        writeMsg("<~>" + type.name().toLowerCase().substring(0, 1));
                        sendRoomList();
			boolean keepGoing = true;
			while(keepGoing) {
				// read a String (which is an object)
				try {
					cm = (ChatMessage) sInput.readObject();
				}
				catch (IOException e) {
					display(username + " Exception reading Streams: " + e);
					break;				
				}
				catch(ClassNotFoundException e2) {
					break;
				}
				// the messaage part of the ChatMessage
				String message = cm.getMessage();

				// Switch on the type of message receive
				switch(cm.getType()) {

				case ChatMessage.MESSAGE:
                                    if(message.startsWith("/")) {
                                        //separate function to avoid messy code
                                        if(type == Users.UserType.ADMIN && !cmdLine.parseCMD(message, this)) {
                                            writeMsg("Invalid Command\n");
                                        }
                                    } else {
                                        broadcast(username + ": " + message, chatRoom);
                                    }
                                    break;
				case ChatMessage.LOGOUT:
					display(username + " disconnected with a LOGOUT message.");
					keepGoing = false;
					break;
				case ChatMessage.ROOMINFO:
                                        HashMap<String, ClientThread> hl = rooms.get(chatRoom);
                                    
                                        if (hl != null) { //remove from previous chat room
                                            hl.remove(username);
                                        }
                                        
					chatRoom = cm.getMessage();
                                        hl = rooms.get(chatRoom);
                                        
                                        if (hl != null) { //moving to new chatroom
                                             hl.put(username, this);
                                        }
                                        
					break;
				}
			}
			// remove myself from the arrayList containing the list of the
			// connected Clients
			remove(id);
			close();
		}
		
		// try to close everything
		private void close() {
                        HashMap<String, ClientThread> clients = rooms.get(chatRoom);
                
                        if (clients != null) {
                            clients.remove(username);
                        }
                        
			// try to close the connection
			try {
				if(sOutput != null) sOutput.close();
			}
			catch(Exception e) {}
			try {
				if(sInput != null) sInput.close();
			}
			catch(Exception e) {};
			try {
				if(socket != null) socket.close();
			}
			catch (Exception e) {}
		}

		/*
		 * Write a String to the Client output stream
		 */
		public boolean writeMsg(String msg) {
			// if Client is still connected send the message to it
			if(!socket.isConnected()) {
				close();
				return false;
			}
			// write the message to the stream
			try {
				sOutput.writeObject(msg);
			}
			// if an error occurs, do not abort just inform the user
			catch(IOException e) {
				display("Error sending message to " + username);
				display(e.toString());
			}
			return true;
		}
	}
}


