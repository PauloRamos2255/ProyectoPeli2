package com.example.proyectopeli.BLL;

import android.net.Uri;

import com.example.proyectopeli.Conecction.ConectionBD;
import com.example.proyectopeli.Entidad.Correo;
import com.example.proyectopeli.Recurso.Recurso;
import com.example.proyectopeli.Entidad.Usuario;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Types;


public class UsuarioBLL {


    Boolean exitoso;
    public Boolean Registar(Usuario user , String htmlCorreo ) {
        try {
            String htmlEnviado = null;
            Connection conexion = ConectionBD.conectar();
            if (conexion != null) {
                String consulta = "INSERT INTO Usuarios (Nombres, Apellidos, Numero, Correo, Contraseña)VALUES(?,?,?,?,?) ";


                try (PreparedStatement pstmt = conexion.prepareStatement(consulta)) {
                    pstmt.setString(1,user.getNombre());
                    pstmt.setString(2, user.getApellido() );
                    pstmt.setString(3,user.getNumero());
                    pstmt.setString(4,user.getCorreo());
                    pstmt.setString(5, Recurso.sha256(user.getClave()));

                    int filasAfectadas = pstmt.executeUpdate();

                    if(filasAfectadas != 0){
                        if(htmlCorreo != null){
                            try {
                                    exitoso = Recurso.enviarCorreo(user.getCorreo() , "Creacion de Cuenta" , htmlCorreo);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    return exitoso;
                }catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                return false;
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }


    public  Boolean Login (String usuario , String contrasena){
        ResultSet resultado;
        try {
            Connection conexion = ConectionBD.conectar();
            if (conexion != null) {
                String consulta = "SELECT * FROM Usuarios  WHERE Correo = ? AND Contraseña = ?";

                try (PreparedStatement pstmt = conexion.prepareStatement(consulta)) {
                    pstmt.setString(1, usuario);
                    pstmt.setString(2, contrasena);

                      resultado = pstmt.executeQuery();

                    return resultado.next();
                }

            } else {
                return false;
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }


    public String Recuperar(String correo, String mensaje) {
        ResultSet resultado;

        try {
            Connection conexion = ConectionBD.conectar();

            if (conexion != null) {
                String consulta = "exec sp_Recuperar ?, ?, ?";
                String clave = Recurso.generarClave();

                try (CallableStatement cstmt = conexion.prepareCall(consulta)) {
                    // Configurar parámetros de entrada
                    cstmt.setString(1, mensaje);
                    cstmt.setString(2, correo);
                    cstmt.setString(3, clave);

                    // Configurar parámetro de salida para el mensaje
                    cstmt.registerOutParameter(1, Types.VARCHAR);

                    // Ejecutar la consulta
                    cstmt.execute();

                    // Obtener el mensaje de salida
                    mensaje = cstmt.getString(1);
                }
            }
        } catch (Exception ex) {
            mensaje = ex.getMessage();
        }

        return mensaje;
    }


}
