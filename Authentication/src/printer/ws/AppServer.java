/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package printer.ws;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIServerSocketFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.ServerSocket;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import sun.rmi.registry.RegistryImpl;


/**
 *
 * @author Dbnk
 */
public class AppServer implements RMIServerSocketFactory {
    
    public static DBInstance db = new DBInstance();
    
     public static void main (String[] args) throws RemoteException, InstantiationException, IllegalAccessException, SQLException, NoSuchAlgorithmException, InvalidKeySpecException, FileNotFoundException, UnsupportedEncodingException{
        Registry registry = LocateRegistry.createRegistry(0x13eb); //5099
        registry.rebind("printer", new PrinterServant());
        System.out.println("Initialize database....");
        dbInit();
        createUsers();
     }
     
     public static String authenticateUserWithToken(String username,String password) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException, InstantiationException, IllegalAccessException{
         if (validateUser(username,password))
                 return generateToken(username);
         return null;
     }
     
     public static boolean validateUser(String username, String password) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException, InstantiationException, IllegalAccessException{
         dbInit();
         String token = null;
         String sql = "SELECT password,salt FROM testdatabase.table22 where username=?;";
         ResultSet rs = db.executetSelect(sql,username);
         if (rs== null) {
             System.out.println("Invalid username or password. Authentication failed");
             return false;
         }
         while(rs.next()){
            String hash = "1000:"+rs.getString("salt")+":"+rs.getString("password");
            return PswdHash.validatePassword(password,hash);
         }
         
         return false;
     }
     
     /*
     Creates a table in mysql that stores the data in the form: 
     ------------------------------------------------------------
     | id | username |  hex(hash(salt))  | hex(hash(password))) |
     ------------------------------------------------------------
     */
     private static void createUsers() throws NoSuchAlgorithmException, InvalidKeySpecException, FileNotFoundException, UnsupportedEncodingException, SQLException{
         String[] users = {"admin","alice","bob","charlie"};
         String[] passwords = {"admin1","alice1","b0b","charl13"};
         db.dropTable();
         db.createTable();
         for (int i=0; i<users.length; i++){
             String[] pass_salt_pair = PswdHash.createHash(passwords[i]); //0:salt , 1:password
             String cmd = "INSERT INTO testdatabase.table22 (username, salt, password) VALUES ('"
             + users[i]+"','"+pass_salt_pair[0]+"','"
             + pass_salt_pair[1]+"');";
              try {
                writeDB(cmd);
             } catch (SQLException ex) {
             Logger.getLogger(AppServer.class.getName()).log(Level.SEVERE, null, ex);
             }
             
         }
     }
     
     private static boolean dbInit() throws InstantiationException, IllegalAccessException{
         db.init();
        return false;
     }
     
     private static void writeDB(String cmd) throws SQLException{
        db.executeCommand(cmd);
     }
     
     /*
     Generates a token for the authenticated user and stores it to database
     */
     private static String generateToken(String username) throws InstantiationException, IllegalAccessException, SQLException{
         dbInit();
         String cmd = null;
         String token = username + "|" +System.currentTimeMillis();
         String sql = "SELECT id FROM testdatabase.table22 where username=?;";
         ResultSet rs = db.executetSelect(sql,username);
         while(rs.next()){
            cmd = "UPDATE testdatabase.table22 SET token ='" +token +"' WHERE id="+rs.getString("id");
         }
       
        try {
            writeDB(cmd);
        } catch (SQLException ex) {
            Logger.getLogger(AppServer.class.getName()).log(Level.SEVERE, null, ex);
        }
         return token;
     }

    @Override
    public ServerSocket createServerSocket(int port) throws IOException {
        ServerSocketFactory factory = SSLServerSocketFactory.getDefault();
        ServerSocket socket = factory.createServerSocket(port);
        return socket;
        
    }
}
