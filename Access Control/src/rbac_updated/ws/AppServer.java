/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rbac_updated.ws;

/**
 *
 * @author Dbnk
 */


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import sun.rmi.registry.RegistryImpl;



public class AppServer implements RMIServerSocketFactory {
    
    public static DBInstance db = new DBInstance();
    private static LinkedList<String>[] users_role = new LinkedList[7];
    public static int[][] role_permisions = new int[4][9];
    public static String[] role_apport = new String[4];
    
     public static void main (String[] args) throws RemoteException, InstantiationException, IllegalAccessException, SQLException, NoSuchAlgorithmException, InvalidKeySpecException, FileNotFoundException, UnsupportedEncodingException, IOException{
        Registry registry = LocateRegistry.createRegistry(6001); //5099
        registry.rebind("printer", new PrinterServant());
        System.out.println("Initialize database....");
        dbInit();
        createUsers();
        readFromFile();
        System.out.println("Initialization of server complete\n**************");

     }
     
      private static void readFromFile() throws FileNotFoundException, IOException{
        String line = null;
        int user_index = 0;
        int column=0;
        int row=0;
        System.out.println("Parsing file *role_rights*");
        FileReader file = new FileReader("role_rights");
        BufferedReader br = new BufferedReader(file);
        
        while ((line = br.readLine()) != null){
            column  = 0;
            if (line.startsWith("//")){
                continue;
            }
            //Fill role_apport
            role_apport[user_index++] = line.substring(0, line.indexOf("|")).trim();
            //Fill role_permisions
            for (int i=line.indexOf("|")+1; i<line.length(); i++){
                role_permisions[row][column++] = Character.getNumericValue(line.charAt(i));
            }
            row++;
        }
        br.close();
        file.close();
        line = null;
        System.out.println("Parsing file *role_apport_updated*");
        FileReader f = new FileReader("role_apport_updated");
        BufferedReader bb = new BufferedReader(f);
        user_index = 0;
        while ((line = bb.readLine()) != null){
            if (line.startsWith("//")){
                continue;
            }
            if (users_role[user_index] == null)
                users_role[user_index] = new LinkedList<String>();
            users_role[user_index].addLast(line.substring(0, line.indexOf("|")).trim());
            String[] names = line.substring(line.indexOf("|")+1).split(",");
            for (int i=0; i<names.length; i++){
                users_role[user_index].addLast(names[i]); //Character.getNumericValue(line.charAt(i));
            }
            user_index++;
        }
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
         String[] users = {"Alice","Ida","Cecilia","David","Erica","Fred","George","Henry"};
         String[] passwords = {"alice","ida","cecilia","david","erica","fred","george","henry"};
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
    
    public static boolean privilege(String user, String method) throws InstantiationException, IllegalAccessException{
        int index = 0; //<4
        int column = 0; //<9
        String role= new String();
        for (int i=0; i<4; i++){
            ListIterator<String> r = users_role[i].listIterator();
            while(r.hasNext()){
                if (r.next().trim().equalsIgnoreCase(user)){
                    role = users_role[i].getFirst();
                }
            }
        }
        System.out.println("User: "+user+" Role: "+ role);
        for (index=0; index<role_apport.length; index++){
            if (role_apport[index].equalsIgnoreCase(role))
                break;
        }
        column = getMethodNumber(method);
        return role_permisions[index][column] == 1;
    }
    
    private static int getMethodNumber(String method){
        switch (method) {
            case "start":
                return 0;
            case "stop":
                return 1;
            case "restart":
                return 2;
            case "status":
                return 3;
            case "readconfig":
                return 4;
            case "setconfig":
                return 5;
            case "queue":
                return 6;
            case "topqueue":
                return 7;
            case "print":
                return 8;
        }
        return -1;
    }
}


