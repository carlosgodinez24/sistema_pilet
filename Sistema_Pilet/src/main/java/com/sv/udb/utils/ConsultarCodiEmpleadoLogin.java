/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.utils;
import java.sql.*;
/**
 *
 * @author Alvin
 */
public class ConsultarCodiEmpleadoLogin {
    
    public int consultarCodigo(String acceUsua, String contUsua)
    {
        int id=0;
        try
        {
          // create our mysql database connection
          String myDriver = "org.gjt.mm.mysql.Driver";
          String myUrl = "jdbc:mysql://www.opensv.tk:3306/webservices";
          Class.forName(myDriver);
          Connection conn = DriverManager.getConnection(myUrl, "poo", "@dmin123");

          // our SQL SELECT query. 
          // if you only need a few columns, specify them by name instead of using "*"
          String query = "SELECT codi_empl FROM usuarios where acce_usua = '"+acceUsua+"' and cont_usua='"+contUsua+"'";

          // create the java statement
          Statement st = conn.createStatement();

          // execute the query, and get a java resultset
          ResultSet rs = st.executeQuery(query);

          // iterate through the java resultset
          while (rs.next())
          {
            id = rs.getInt("codi_empl");
          }
          st.close();
        }
        catch (Exception e)
        {
          System.err.println("Got an exception! ");
          System.err.println(e.getMessage());
          e.printStackTrace();
        }
        return id;
    }
    
    public int consultarCodigo(String acceUsua)
    {
        int id=0;
        try
        {
          // create our mysql database connection
          String myDriver = "org.gjt.mm.mysql.Driver";
          String myUrl = "jdbc:mysql://www.opensv.tk:3306/webservices";
          Class.forName(myDriver);
          Connection conn = DriverManager.getConnection(myUrl, "poo", "@dmin123");

          // our SQL SELECT query. 
          // if you only need a few columns, specify them by name instead of using "*"
          String query = "SELECT codi_empl FROM usuarios where acce_usua = '"+acceUsua+"'";

          // create the java statement
          Statement st = conn.createStatement();

          // execute the query, and get a java resultset
          ResultSet rs = st.executeQuery(query);

          // iterate through the java resultset
          while (rs.next())
          {
            id = rs.getInt("codi_empl");
          }
          st.close();
        }
        catch (Exception e)
        {
          System.err.println("Got an exception! ");
          System.err.println(e.getMessage());
          e.printStackTrace();
        }
        return id;
    }
    
}
