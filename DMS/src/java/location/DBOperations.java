/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package location;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Jack Wang
 */

/*
This class includes all the operations from Java Derby database.
Therefore, opimize the code if need to create the connection and send queries.
*/

public class DBOperations {

    private static final String url = "jdbc:derby://localhost:1527/LocationProjectDB;create=true";
    String tableName = "LocationData";
    private static final String username = "dms";
    private static final String password = "dms";
    Connection conn;
    final String quote = "'";

    public void createConnection() {
        try {
            conn = DriverManager.getConnection(url, username, password);
            System.out.println(url + " connected....");

        } catch (SQLException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean checkTableExist() throws SQLException {

        boolean tableExist = false;

        //schema for new db is the login username in uppercases, in this case, DMS
        DatabaseMetaData md = conn.getMetaData();
        ResultSet rs = md.getTables(null, "DMS", tableName.toUpperCase(), null);

        //if table exists
        if (rs.next()) {
            tableExist = true;
        }

        return tableExist;

    }

    public void createTable() {

        try {

            //if table does not exist, need to create a new table
            if (!checkTableExist()) {
                Statement statement = conn.createStatement();

                String sqlCreate = "CREATE TABLE " + tableName + " "
                        +"(ID INT not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "                    
                        + "Latitude float, Longitude float, "
                        + "Title varchar(255), "
                        + "CONSTRAINT primary_key PRIMARY KEY (ID)"
                        + ")";

                statement.executeUpdate(sqlCreate);
               
                String Data = "INSERT INTO " + tableName + "(Latitude,Longitude,Title)"
                        + "VALUES "
                        + "(-36.8749538,174.7425675,'Dad'),"
                        + "(-36.9894137,174.8578987,'Mum'),"
                        + "(-36.8541523,174.7772213,'Grandfather'), "
                        + "(-36.614946,174.8144855,'Grandmother')";              
                statement.executeUpdate(Data);    
                
                System.out.println("Table created");
            }

        } catch (SQLException ex) {
            Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);

        }
    }
    
    //return resultset for sql select queries
    public ResultSet selectQuery(String sql) {

        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = conn.createStatement();
            resultSet = statement.executeQuery(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void insertIntoDatabase(double latitude, double longitude, String title) {

        String sql = "INSERT INTO " + tableName.toUpperCase() + "(ID,Latitude, Longitude, Title)" + " VALUES ("+
                latitude+", " + longitude+", "+quote + title + quote+")";
        try {
            conn.createStatement().executeUpdate(sql);
            System.out.println("Successfully added!");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void closeConnections() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
