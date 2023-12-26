package com.example.proyectopeli.Conecction;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConectionBD {
    public static Connection conectar() {
        Connection cnn =null;
        try {
            StrictMode.ThreadPolicy politica= new  StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(politica);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            cnn= DriverManager.getConnection("jdbc:jtds:sqlserver://TADIAdmin.mssql.somee.com;user=PauloRamos_SQLLogin_1;password=8zmhlf3lxk;databaseName=TADIAdmin");

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return cnn;
    }
}
