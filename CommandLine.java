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
    String[] parsedCMD;
    Users users = new Users();
    
    public CommandLine(String cmd)
    {
        //splits cmd into a string array (parsedCMD) with each entry delimited by whitespace
        parsedCMD = cmd.split("\\s+");
    }
    
    public boolean parseCMD(Server.ClientThread thread)
    {
        //if returns false then command is invalid, returns true if command completed
        if(parsedCMD.length <= 1)
        {
            //invalid command, This suggests the command was as follows /oneword, /deleteNekros
            return false;
        }
                    
        //gets the command without the slash for presentation sake
        String userCommand = parsedCMD[0].substring(1);
        System.out.println("Length: " + parsedCMD.length);
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
        }
        
        return false;                 
    }
}
