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
    
    public CommandLine()
    {
        
    }
    public CommandLine(String cmd)
    {
        //splits cmd into a string array (parsedCMD) with each entry delimited by whitespace
        parsedCMD = cmd.split("\\s+");
    }
    
    public boolean parseCMD()
    {
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
                users.addUser(parsedCMD[1], parsedCMD[2], Users.UserType.valueOf(parsedCMD[3]));
                return true;
            }
            case "delete":
            {
                users.deleteUser(parsedCMD[1]);
                return true;
            }
            case "edittype":
            {
                users.changeUserType(parsedCMD[1], Users.UserType.valueOf(parsedCMD[2]));
                return true;
            }
            case "editpassword":
            {
                users.changePassword(parsedCMD[1], parsedCMD[2]);
                return true;
            }
        }
        
        return false;                 
    }
}
