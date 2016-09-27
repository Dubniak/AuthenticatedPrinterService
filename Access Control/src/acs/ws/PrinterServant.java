/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acs.ws;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Dbnk
 */
public class PrinterServant extends UnicastRemoteObject implements PrinterService {
    
    public PrinterServant() throws RemoteException{
    super();
}

    @Override
    public void print(String filename, String printer,String token) throws RemoteException {
        if (token == null) {
            System.out.println("Unathenticated user - Cannot proceed");
            return;
        }
        String user = getUser(token);
        try {
            if (getPrivileges(token,"print")){
                System.out.println("User:"+user+"\tPrint "+filename);
            }
            else {
                System.out.println("User:"+user+"Unauthorized call to method Print");
            }
        } catch (InstantiationException ex) {
            Logger.getLogger(PrinterServant.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(PrinterServant.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void queue(String token) throws RemoteException {
        if (token == null) {
            System.out.println("Unathenticated user - Cannot proceed");
            return;
        }
        String user = getUser(token);
        try {
            if (getPrivileges(token,"queue")){
                System.out.println("User:"+user+"\tQueue");
            }
            else {
                System.out.println("User:"+user+"Unauthorized call to method Queue");
            }
        } catch (InstantiationException ex) {
            Logger.getLogger(PrinterServant.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(PrinterServant.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    @Override
    public void topQueue(int job,String token) throws RemoteException {
        if (token == null) {
            System.out.println("Unathenticated user - Cannot proceed");
            return;
        }
        String user = getUser(token);
         try {
            if (getPrivileges(token,"topqueue")){
                System.out.println("User:"+user+"\tTop Queue");            
            }
            else {
                System.out.println("User:"+user+"Unauthorized call to method topQueue");
            }
        } catch (InstantiationException ex) {
            Logger.getLogger(PrinterServant.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(PrinterServant.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }

    @Override
    public void start(String token) throws RemoteException {
        if (token == null) {
            System.out.println("Unathenticated user - Cannot proceed");
            return;
        }
        String user = getUser(token);
        try {
            if (getPrivileges(token,"start")){
                 System.out.println("User:"+user+"\tStart");              
            }
            else {
                System.out.println("User:"+user+"Unauthorized call to method Start");
            }
        } catch (InstantiationException ex) {
            Logger.getLogger(PrinterServant.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(PrinterServant.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }

    @Override
    public void stop(String token) throws RemoteException {
        if (token == null) {
            System.out.println("Unathenticated user - Cannot proceed");
            return;
        }
        String user = getUser(token);
        try {
            if (getPrivileges(token,"stop")){
                 System.out.println("User:"+user+"\tStop");              
            }
            else {
                System.out.println("User:"+user+"Unauthorized call to method Stop");
            }
        } catch (InstantiationException ex) {
            Logger.getLogger(PrinterServant.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(PrinterServant.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void restart(String token) throws RemoteException {
        if (token == null) {
            System.out.println("Unathenticated user - Cannot proceed");
            return;
        }
        String user = getUser(token);
        try {
            if (getPrivileges(token,"restart")){
                 System.out.println("User:"+user+"\tRestart");              
            }
            else {
                System.out.println("User:"+user+"Unauthorized call to method Restart");
            }
        } catch (InstantiationException ex) {
            Logger.getLogger(PrinterServant.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(PrinterServant.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void status(String token) throws RemoteException {
        if (token == null) {
            System.out.println("Unathenticated user - Cannot proceed");
            return;
        }
        String user = getUser(token);
        try {
            if (getPrivileges(token,"status")){
                 System.out.println("User:"+user+"\tStatus");              
            }
            else {
                System.out.println("User:"+user+"Unauthorized call to method Status");
            }
        } catch (InstantiationException ex) {
            Logger.getLogger(PrinterServant.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(PrinterServant.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void readConfig(String parameter,String token) throws RemoteException {
        if (token == null) {
            System.out.println("Unathenticated user - Cannot proceed");
            return;
        }
        String user = getUser(token);
        try {
            if (getPrivileges(token,"readconfig")){
                 System.out.println("User:"+user+"\tRead Configuration");              
            }
            else {
                System.out.println("User:"+user+"Unauthorized call to method Read Configuration");
            }
        } catch (InstantiationException ex) {
            Logger.getLogger(PrinterServant.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(PrinterServant.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void setConfig(String parameter, String value,String token) throws RemoteException {
        if (token == null) {
            System.out.println("Unathenticated user - Cannot proceed");
            return;
        }
        String user = getUser(token);
        try {
            if (getPrivileges(token,"setconfig")){
                System.out.println("User:"+user+"\tSet Configuration");             
            }
            else {
                System.out.println("User:"+user+"Unauthorized call to method Set Configuration");
            }
        } catch (InstantiationException ex) {
            Logger.getLogger(PrinterServant.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(PrinterServant.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private String getUser(String token){
        return token.substring(0, token.indexOf("|"));
    }
    
    private boolean getPrivileges(String token,String method) throws InstantiationException, IllegalAccessException{
        //String user = token.substring(0, token.indexOf("|"));
        return AppServer.privilege(getUser(token), method);
    }
    
    
}
