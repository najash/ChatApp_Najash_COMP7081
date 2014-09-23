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
                //add database code
                return true;
            }
            case "delete":
            {
                
                return true;
            }
            case "edit":
            {
                
                return true;
            }
        }
        
        return false;                 
    }
}
