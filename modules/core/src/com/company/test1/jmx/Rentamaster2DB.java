package com.company.test1.jmx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Rentamaster2DB {

    static Connection conn;
    static Connection extDocs;

    public static  Connection getConnection(){
        if (conn!=null){
            return conn;
        }
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://localhost/rentamaster2?user=root&password=dtisat&serverTimezone=UTC");
        } catch (Exception ex) {
            int y = 2;
        }
        return conn;
    }

    public static  Connection getConnectionExtDocs(){
        if (conn!=null){
            return conn;
        }
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            conn = DriverManager.getConnection("jdbc:mysql://localhost/rentamasterdocs?user=root&password=dtisat&serverTimezone=UTC");

        } catch (Exception ex) {
            int y = 2;
        }
        return conn;
    }

    public static  ResultSet getResultSet(String sql, Connection conn) throws Exception{
        Statement s = conn.createStatement();
        ResultSet r = s.executeQuery(sql);
        return r;
    }

    public static ResultSet getResultSet(String sql) throws Exception{
        return getResultSet(sql, getConnection());
    }

    public static Integer getNumberOfRecords(String sql, Connection conn) throws Exception{
        ResultSet r = getResultSet(sql, conn);
        int n = 0;
        while(r.next()){
            n++;
        }
        r.close();
        return n;
    }

    public static Integer getNumberOfRecords(String sql) throws Exception{
        ResultSet r = getResultSet(sql, getConnection());
        int n = 0;
        while(r.next()){
            n++;
        }
        r.close();
        return n;
    }

}
