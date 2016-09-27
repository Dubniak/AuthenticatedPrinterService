/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rbac.ws;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.Scanner;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;


/**
 *
 * @author Dbnk
 */
public class Client implements RMIClientSocketFactory {
    AppServer ser ;
    
    public static void main (String[] args)  throws NotBoundException, MalformedURLException, RemoteException, NoSuchAlgorithmException, InvalidKeySpecException, FileNotFoundException, UnsupportedEncodingException, SQLException, InstantiationException, IllegalAccessException {
        System.setProperty("javax.net.ssl.trustStore", "C://Program Files/Java/jdk1.8.0_60/jre/lib/security/cacerts");
        System.setProperty("javax.net.ssl.trustStorePassword", "password");
        
        
        PrinterService service = (PrinterService) Naming.lookup("rmi://localhost:5099/printer");
        
        System.out.println("Client runing..");
        while(true){
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter username (type 'exit' to exit) :"); //get username
            String uname = sc.nextLine();
 
            if ("exit".equalsIgnoreCase(uname))
                break;
            
            System.out.println("Enter password"); //get password
            String pass = sc.nextLine();

            String token = AppServer.authenticateUserWithToken(uname, pass);
            if (token!= null) {
                System.out.println("Authentication succeed for user: "+uname);
            } else {
                System.out.println("Wrong username or password. Authentication failed.\nTry again");
                continue;
            }
            //Display a simple menu. 
            int choice = -1;
            while (choice != 0){
                
                switch(choice){
                    case 1:
                        service.print("file1", "printer1", token);
                        break;
                    case 2:
                        service.queue(token);
                        break;
                    case 3:
                        service.readConfig("ink", token);
                        break;
                    case 4:
                        service.restart(token);
                        break;
                    case 5:
                        service.setConfig("ink", "100%", token);
                        break;
                    case 6:
                        service.start(token);
                        break;
                    case 7:
                        service.status(token);
                        break;
                    case 8:
                        service.stop(token);
                        break;
                    case 9:
                        service.topQueue(choice, token);
                        break;
                    default:
                        System.out.println("1.Print file\n2.Queue\n3.Read Configuration"
                                + "\n4.Restart Service\n5.Set Configuration"
                                + "\n6.Start Service\n7.Display status"
                                + "\n8.Stop Service\n9.Top Queue\n"
                                + "For exit press 0.");
                }
                System.out.print("Enter choice:");
                choice = sc.nextInt();
                
            }
      
        }
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        SocketFactory factory = SSLSocketFactory.getDefault();
        Socket socket = factory.createSocket(host,port);
        return socket;
    }
    
    
}

