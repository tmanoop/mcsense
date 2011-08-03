package com.mcsense.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class Register {

    private static String dbURL = "jdbc:derby://localhost:1527/McSenseDB;create=true;user=app;password=app";
    private static String tableName = "APP.PEOPLE";
    // jdbc Connection
    private static Connection conn = null;
    private static Statement stmt = null;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		createConnection();
        insertPerson(775, "Peter", "Jackson", "N");
        selectPersons();
        shutdown();

	}
	
	private static void selectSysTables(){
		try
        {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select * from sys.systables");
            ResultSetMetaData rsmd = results.getMetaData();
            int numberCols = rsmd.getColumnCount();
            for (int i=1; i<=numberCols; i++)
            {
                //print Column Names
                System.out.print(rsmd.getColumnLabel(i)+"\t\t");  
            }

            System.out.println("\n-------------------------------------------------");

            while(results.next())
            {
                String id = results.getString(1);
                String TName = results.getString(2);
                String TType = results.getString(3);
                System.out.println(id + "\t\t" + TName + "\t\t" + TType);
            }
            results.close();
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
	}
	
	private static void createConnection()
    {
        try
        {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            //Get a connection
            conn = DriverManager.getConnection(dbURL); 
        }
        catch (Exception except)
        {
            except.printStackTrace();
        }
    }
	
	private static void insertPerson(int bankAccountId, String personLName, String personFName, String addrsDetails)
    {
        try
        {
            stmt = conn.createStatement();
            stmt.execute("INSERT INTO " + tableName + " (BANK_ACCOUNT_ID, PERSON_LNAME, PERSON_FNAME) VALUES ( " + bankAccountId + ",'" + personLName + "','" + personFName +"')");
            stmt.close();
            
            stmt = conn.createStatement();
            stmt.execute("INSERT INTO " + " (BANK_ACCOUNT_ID, PERSON_LNAME, PERSON_FNAME) VALUES ( " + bankAccountId + ",'" + personLName + "','" + personFName +"')");
            stmt.close();
            
            stmt = conn.createStatement();
            stmt.execute("INSERT INTO " + " (BANK_ACCOUNT_ID, PERSON_LNAME, PERSON_FNAME) VALUES ( " + bankAccountId + ",'" + personLName + "','" + personFName +"')");
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
	
	 private static void selectPersons()
	    {
	        try
	        {
	            stmt = conn.createStatement();
	            ResultSet results = stmt.executeQuery("select * from " + tableName);
	            ResultSetMetaData rsmd = results.getMetaData();
	            int numberCols = rsmd.getColumnCount();
	            for (int i=1; i<=numberCols; i++)
	            {
	                //print Column Names
	                System.out.print(rsmd.getColumnLabel(i)+"\t\t");  
	            }

	            System.out.println("\n-------------------------------------------------");

	            while(results.next())
	            {
	                int id = results.getInt(1);
	                int bankAccountId = results.getInt(2);
	                String LName = results.getString(3);
	                String FName = results.getString(4);
	                System.out.println(id + "\t\t" + bankAccountId + "\t\t" + LName + "\t\t" + FName);
	            }
	            results.close();
	            stmt.close();
	        }
	        catch (SQLException sqlExcept)
	        {
	            sqlExcept.printStackTrace();
	        }
	    }
	    
	    private static void shutdown()
	    {
	        try
	        {
	            if (stmt != null)
	            {
	                stmt.close();
	            }
	            if (conn != null)
	            {
	                DriverManager.getConnection(dbURL + ";shutdown=true");
	                conn.close();
	            }           
	        }
	        catch (SQLException sqlExcept)
	        {
	            
	        }

	    }
}
