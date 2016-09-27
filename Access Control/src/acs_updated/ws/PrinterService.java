/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acs_updated.ws;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Dbnk
 */
public interface PrinterService extends Remote {
    public void print(String filename, String printer,String token) throws RemoteException;
    public void queue(String token) throws RemoteException; // lists the print queue on the user's display in lines of the form <job number> <filename>
    public void topQueue(int job,String token) throws RemoteException; // moves job to the top of the queue
    public void start(String token) throws RemoteException; // starts the print server
    public void stop(String token) throws RemoteException; // stops the print server
    public void restart(String token)throws RemoteException; // stops the print server, clears the print queue and starts the print server again
    public void status(String token) throws RemoteException; // prints status of printer on the user's display
    public void readConfig(String parameter,String token) throws RemoteException; // prints the value of the parameter on the user's display
    public void setConfig(String parameter, String value,String token) throws RemoteException; // sets the parameter to value
}
