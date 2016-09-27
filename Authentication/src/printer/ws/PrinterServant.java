/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package printer.ws;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
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
        System.out.println("User:"+user+"\tPrint "+filename);
    }

    @Override
    public void queue(String token) throws RemoteException {
        if (token == null) {
            System.out.println("Unathenticated user - Cannot proceed");
            return;
        }
        String user = getUser(token);
        System.out.println("User:"+user+"\tQueue");
        
    }

    @Override
    public void topQueue(int job,String token) throws RemoteException {
        if (token == null) {
            System.out.println("Unathenticated user - Cannot proceed");
            return;
        }
        String user = getUser(token);
        System.out.println("User:"+user+"\tTop Queue");    
    }

    @Override
    public void start(String token) throws RemoteException {
        if (token == null) {
            System.out.println("Unathenticated user - Cannot proceed");
            return;
        }
        String user = getUser(token);
        System.out.println("User:"+user+"\tStart");    }

    @Override
    public void stop(String token) throws RemoteException {
        if (token == null) {
            System.out.println("Unathenticated user - Cannot proceed");
            return;
        }
        String user = getUser(token);
        System.out.println("User:"+user+"\tStop");
    }

    @Override
    public void restart(String token) throws RemoteException {
        if (token == null) {
            System.out.println("Unathenticated user - Cannot proceed");
            return;
        }
        String user = getUser(token);
        System.out.println("User:"+user+"\tRestart");
    }

    @Override
    public void status(String token) throws RemoteException {
        if (token == null) {
            System.out.println("Unathenticated user - Cannot proceed");
            return;
        }
        String user = getUser(token);
        System.out.println("User:"+user+"\tStatus");
    }

    @Override
    public void readConfig(String parameter,String token) throws RemoteException {
        if (token == null) {
            System.out.println("Unathenticated user - Cannot proceed");
            return;
        }
        String user = getUser(token);
        System.out.println("User:"+user+"\tRead Configuration");
    }

    @Override
    public void setConfig(String parameter, String value,String token) throws RemoteException {
        if (token == null) {
            System.out.println("Unathenticated user - Cannot proceed");
            return;
        }
        String user = getUser(token);
        System.out.println("User:"+user+"\tSet Configuration");
    }
    
    private String getUser(String token){
        return token.substring(0, token.indexOf("|"));
    }
    
    
}
