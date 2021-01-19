package com.company.test1.jmx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Cubatest1DB {

    static Connection conn = null;

    public static Connection getConnection(){
        try {
            if (conn.isClosed()) {
                conn = null;
            }
        }catch(Exception exc){
            conn = null;
        }
        if (conn!=null){

            return conn;
        }
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            conn = DriverManager.getConnection("jdbc:mysql://localhost/cubatest1?user=root&password=dtisat&serverTimezone=UTC");

        } catch (Exception ex) {
            int y = 2;
        }
        return conn;
    }

    public static void executeSql(Connection conn, String sql) throws Exception{
        Statement stm = conn.createStatement();
        stm.executeUpdate(sql);
        stm.close();
    }

}
