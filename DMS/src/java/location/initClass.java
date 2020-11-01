/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package location;

/**
 *
 * @author Jack Wang
 */
public class initClass {
     static DBOperations db = new DBOperations();
    public static void main(String[] args) {
        db.createConnection();
        db.createTable();
    }
}
