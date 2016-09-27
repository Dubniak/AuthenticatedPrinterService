/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rbac.ws;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dbnk
 */
public class DBInstance {
    private Connection myConnection;
    private Properties properties;
    private static final String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/testdatabase";
    private static final String DATABASE_SCHEMA = "jdbc:mysql://localhost:3306/";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1122";
    private static final String MAX_POOL = "250"; // set your own limit
    
    public DBInstance(){
        
    }
    
     private Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            properties.setProperty("user", USERNAME);
            properties.setProperty("password", PASSWORD);
           // properties.setProperty("MaxPooledStatements", MAX_POOL);
        }
        return properties;
    }
    
      public void init() throws InstantiationException, IllegalAccessException{
        try{
            Class.forName(DATABASE_DRIVER);
            myConnection = DriverManager.getConnection(DATABASE_URL,getProperties());
            
        }
        catch(ClassNotFoundException | SQLException e){
            System.out.println("Failed to connect to db instance");
            e.printStackTrace();
        }
    }
      
      /*Executes given sql statement *cmd* , returns true on success, false otherwise*/
      public boolean executeCommand(String cmd) throws SQLException{
         
        if (myConnection == null){
            System.out.println("executeCommand: connection is closed");
                            return false;

        }
        PreparedStatement state = myConnection.prepareStatement(cmd);
        
        return state.execute();
    }
     
      //Constracts sql statement, in order to defend agains SQL injections 
       public ResultSet executetSelect(String cmd,String username) throws SQLException{
           if (myConnection == null){
               System.out.println("executeSelect: connection is closed");
               return null;
           }
           PreparedStatement st = myConnection.prepareStatement(cmd);
           st.setString(1, username);
           return st.executeQuery();
           
       }
       
       public void closeConnection(){
        if (this.myConnection != null){
            try{
                this.myConnection.close();
                this.myConnection = null;
            }
            catch(Exception e){}
        }
    }
       
       public void createTable(){
           /*
           CREATE TABLE AgentDetail ( 
                idNo INT(64) NOT NULL AUTO_INCREMENT, 
                initials VARCHAR(2),
                agentDate DATE,  
                agentCount INT(64),PRIMARY KEY (`idNo`));
           
           */
           String sql = "CREATE TABLE IF NOT EXISTS testdatabase.table22 (id INT(64) NOT NULL AUTO_INCREMENT,"
                   +                                        "username VARCHAR(20),"
                   +                                        "salt VARCHAR(100),"
                   +                                        "password VARCHAR(100),"
                   +                                        "token VARCHAR(100),"
                   +                                        "PRIMARY KEY (`id`));";
        try {
            executeCommand(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBInstance.class.getName()).log(Level.SEVERE, null, ex);
        }
           
       }
       
       public void dropTable(){
           String sql ="DROP TABLE IF EXISTS testdatabase.table22;";
        try {
            executeCommand(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBInstance.class.getName()).log(Level.SEVERE, null, ex);
        }
           
       }
}
