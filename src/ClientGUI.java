
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


/*
 * The Client with its GUI
 */
public class ClientGUI extends JApplet implements ActionListener {
	private static final long serialVersionUID = 1L;
	// will first hold "Username:", later on "Enter message"
	private JLabel label;
	// to hold the Username and later on the messages
	private JTextField tf;
	// to hold the server address an the port number
	private JTextField tfServer, tfPort;
	// to Logout and get the list of the users
	private JButton login, logout;
        //login text fields
        private JTextField username;
        private JPasswordField password;
	// for the chat room
	private JTextArea ta, taDisplay;
	// if it is for connection
	private boolean connected;
	// the Client object
	private Client client;
	// the default port number
	private int defaultPort;
	private String defaultHost;
        
        //char rooms list
        private JComboBox<String> rooms;

	// Constructor connection receiving a socket number
	public void init() {
		// The NorthPanel with:
		JPanel northPanel = new JPanel(new GridLayout(3,1));
		// the server name anmd the port number
		JPanel serverAndPort = new JPanel(new GridLayout(1,5, 1, 3));
		// the two JTextField with default value for server address and port number
                
                JPanel eastPanel = new JPanel(new GridLayout(1,1));
                
		tfServer = new JTextField("localhost");
		tfPort = new JTextField("" + 1500);
		tfPort.setHorizontalAlignment(SwingConstants.RIGHT);

		serverAndPort.add(new JLabel("Server Address:  "));
		serverAndPort.add(tfServer);
		serverAndPort.add(new JLabel("Port Number:  "));
		serverAndPort.add(tfPort);
		serverAndPort.add(new JLabel(""));
		// adds the Server an port field to the GUI
		northPanel.add(serverAndPort);

		// the Label and the TextField
		label = new JLabel("Enter your message below", SwingConstants.CENTER);
		northPanel.add(label);
		tf = new JTextField();
		tf.setBackground(Color.WHITE);
		northPanel.add(tf);
		add(northPanel, BorderLayout.NORTH);

		// The CenterPanel which is the chat room
		ta = new JTextArea("Welcome to the Chat room\n", 25, 40);
		JPanel centerPanel = new JPanel(new GridLayout(1,1));
		centerPanel.add(new JScrollPane(ta));
		ta.setEditable(false);
		add(centerPanel, BorderLayout.CENTER);

                taDisplay = new JTextArea("", 10, 10);
                eastPanel.add(new JScrollPane(taDisplay));
                taDisplay.setEditable(false);
                add(eastPanel, BorderLayout.EAST);
                
                //login fields
                JPanel loginpanel = new JPanel();
                username = new JTextField();
                username.setPreferredSize(new Dimension(100, 20));
                loginpanel.add(new JLabel("Username: "));
                loginpanel.add(username);
                password = new JPasswordField();
                password.setPreferredSize(new Dimension(100, 20));
                loginpanel.add(new JLabel("Password: "));
                loginpanel.add(password);
                username.addActionListener(this);
                password.addActionListener(this);
                
		// the 3 buttons
		login = new JButton("Login");
		login.addActionListener(this);
		logout = new JButton("Logout");
		logout.addActionListener(this);
		logout.setEnabled(false);		// you have to login before being able to logout
	
                rooms = new JComboBox<String>();
                rooms.setPreferredSize(new Dimension(100, 20));
                rooms.addActionListener(this);
                
		JPanel loginBtnPanel = new JPanel();
		loginBtnPanel.add(login);
		loginBtnPanel.add(logout);
                loginBtnPanel.add(new JLabel("Room: "));
		loginBtnPanel.add(rooms);
		
                JPanel southPanel = new JPanel();
                southPanel.add(loginpanel, BorderLayout.NORTH);
                southPanel.add(loginBtnPanel, BorderLayout.SOUTH);
                add(southPanel, BorderLayout.SOUTH);

		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(800, 600);
		setVisible(true);
                tf.addActionListener(this);
		username.requestFocus();
               // pack();
               // setLocationRelativeTo(null);
                
	}

	// called by the Client to append text in the TextArea 
	void append(String str) {
		ta.append(str);
		ta.setCaretPosition(ta.getText().length() - 1);
	}
        
        void displayUsers(String str)
        {
            taDisplay.setText(str);
        }
	// called by the GUI is the connection failed
	// we reset our buttons, label, textfield
	void connectionFailed() {
		login.setEnabled(true);
		logout.setEnabled(false);
		// reset port number and host name as a construction time
		tfPort.setText("" + defaultPort);
		tfServer.setText(defaultHost);
		// let the user change them
		tfServer.setEditable(true);
		tfPort.setEditable(true);
		connected = false;
                password.setEnabled(true);
                username.setEnabled(true);
                username.requestFocus();
                login.setEnabled(true);
                logout.setEnabled(false);
	}
		
	/*
	* Button or JTextField clicked
	*/
        @Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		// if it is the Logout button
                
		if(o == logout) {
			client.sendMessage(new ChatMessage(ChatMessage.LOGOUT, ""));
                        password.setEnabled(true);
                        username.setEnabled(true);
                        username.requestFocus();
                        login.setEnabled(true);
                        logout.setEnabled(false);
			return;
		}
		
		if(o == rooms) {
                        client.sendMessage(new ChatMessage(ChatMessage.ROOMINFO, rooms.getSelectedItem().toString()));
                        return;
		}

		// ok it is coming from the JTextField
		if(connected) {
			// just have to send the message
			client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, tf.getText()));				
			tf.setText("");
			return;
		}
		

		if(o == login || o == password || o == username) {
			// ok it is a connection request
			String user = username.getText().trim();
                        String pass = new String(password.getPassword());
                        
                        password.setText("");
                        username.setText("");
                        
			// empty username ignore it
			if(user.length() == 0)
				return;
			// empty serverAddress ignore it
			String server = tfServer.getText().trim();
			if(server.length() == 0)
				return;
			// empty or invalid port numer, ignore it
			String portNumber = tfPort.getText().trim();
			if(portNumber.length() == 0)
				return;
                        
                        append("Connecting to server at: " + server + ":" + portNumber + "\n");
                        
			int port = 0;
			try {
				port = Integer.parseInt(portNumber);
			}
			catch(Exception en) {
                                append(en.getMessage() + "\n");
				return;   // nothing I can do if port number is not valid
			}

			// try creating a new Client with GUI
			client = new Client(server, port, user, pass, this);
			// test if we can start the Client
			if(!client.start()) 
				return;
                   
                        defaultPort = port;
                        defaultHost = server;
                        
                        password.setEnabled(false);
			username.setEnabled(false);         
			connected = true;
			
			// disable login button
			login.setEnabled(false);
			// enable the 2 buttons
			logout.setEnabled(true);
			// disable the Server and Port JTextField
			tfServer.setEditable(false);
			tfPort.setEditable(false);
			// Action listener for when the user enter a message
                        tf.requestFocus();
		}

	}

        void populateRoomList(String str) {
        StringTokenizer strToken = new StringTokenizer(str, ",");
        rooms.removeAllItems();
        while (strToken.hasMoreTokens()) {
           rooms.addItem(strToken.nextToken());
        }
        client.sendMessage(new ChatMessage(ChatMessage.ROOMINFO, rooms.getSelectedItem().toString()));
    }

}

