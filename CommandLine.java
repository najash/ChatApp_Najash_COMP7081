/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author A00813191
 */
public class CommandLine {
    Users users = new Users();
    Rooms rooms = new Rooms();
    Server server;
    
    public CommandLine(Server server)
    {
        this.server = server;
    }
    
    public boolean parseCMD(String cmd, Server.ClientThread thread)
    {
        //splits cmd into a string array (parsedCMD) with each entry delimited by whitespace
        String[] parsedCMD = cmd.split("\\s+");
        
        //if returns false then command is invalid, returns true if command completed
        if(parsedCMD.length <= 1)
        {
            //invalid command, This suggests the command was as follows /oneword, /deleteNekros
            return false;
        }
                    
        //gets the command without the slash for presentation sake
        String userCommand = parsedCMD[0].substring(1);
        switch(userCommand.toLowerCase())
        {
            case "add":
            {
                if (parsedCMD.length == 4 && 
                        users.addUser(parsedCMD[1], parsedCMD[2], Users.UserType.valueOf(parsedCMD[3].toUpperCase()))) {
                    thread.writeMsg("User " + parsedCMD[1] + " was created with " + parsedCMD[3] + " privileges\n");
                    return true;
                } else {
                    thread.writeMsg("Usage: /add [username] [password] [type]\n");
                }
                break;
            }
            case "delete":
            {
                if (parsedCMD.length == 2 && 
                        users.deleteUser(parsedCMD[1])) {
                    thread.writeMsg("User " + parsedCMD[1] + " was deleted\n");
                    return true;
                } else {
                    thread.writeMsg("Usage: /delete [username]\n");
                }
                break;
            }
            case "edittype":
            {
                if (parsedCMD.length == 3 && 
                        users.changeUserType(parsedCMD[1], Users.UserType.valueOf(parsedCMD[2].toUpperCase()))) {
                    thread.writeMsg("User " + parsedCMD[1] + " privilege was changed to " + parsedCMD[2] + "\n");
                    Server.ClientThread t = server.getClientThread(parsedCMD[1]);
                    
                    if (t != null) {
                        t.writeMsg("<~>" + parsedCMD[2].substring(0, 1));
                        t.writeMsg("Your user type has been changed to " + parsedCMD[2].toUpperCase() + "\n");
                    }
                    
                    return true;
                } else {
                    thread.writeMsg("Usage: /edittype [username] [type]\n");
                }
                break;
            }

            case "editpassword":
            {
                if (parsedCMD.length == 3 && 
                        users.changePassword(parsedCMD[1], parsedCMD[2])) {
                    thread.writeMsg("User " + parsedCMD[1] + " password was changed\n");
                    return true;
                } else {
                    thread.writeMsg("Usage: /editpassword [username] [password]\n");
                }
                break;
            }
            /**
             * An administer create a room
             */
            case "addroom":
            {
                if (parsedCMD.length == 2 && rooms.addRoom(parsedCMD[1])) {
                    thread.writeMsg("Chat room " + parsedCMD[1] + " was created\n");
                    server.broadcastRooms();
                    return true;
                } else {
                    thread.writeMsg("Usage: /addroom [room name]\n");
                }
                break;
            }
            /**
             * An administer can anonymously make an Announcement
             */
            case "shout":
            {
                if (parsedCMD.length == 2) {
                    thread.writeMsg("Announcement: " + parsedCMD[1] +"\n");
                    server.broadcastRooms();
                    return true;
                } else {
                    thread.writeMsg("Usage: /shout [message]\n");
                }
                break;
            }
            /**
             * An administer can clear the chat !
             */
            case "clear":
            {
                if (parsedCMD.length == 2) {
                    thread.writeMsg("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                    thread.writeMsg("\n Chat was cleared, by admin !\n");
                    thread.writeMsg("\n\n\n\n\n\n\n\n\n");
                    return true;
                } else {
                    thread.writeMsg("Usage: /clear now \n");
                }
                break;
            }
            case "rmroom":
            {
                if (parsedCMD.length == 2 && rooms.deleteRoom(parsedCMD[1])) {
                    thread.writeMsg("Chat room " + parsedCMD[1] + " was removed\n");
                    server.broadcastRooms();
                    return true;
                } else {
                    thread.writeMsg("Usage: /rmroom [room name]\n");
                }
                break;
            }
        }
        
        return false;                 
    }
}
