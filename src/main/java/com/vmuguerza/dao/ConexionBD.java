package com.vmuguerza.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    //private static String url = "jdbc:mysql://localhost:3306/pageturner";
    private static final String URL = "jdbc:hsqldb:file:data/pageturner;shutdown=true";
    private static final String USUARIO = "SA";
    private static final String PASS = "";

    private ConexionBD(){

    }

    public static Connection getConexion() throws SQLException{
        return DriverManager.getConnection(URL, USUARIO, PASS);
    }
}
