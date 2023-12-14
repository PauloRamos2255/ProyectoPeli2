package com.example.proyectopeli.BLL;

import android.security.keystore.UserPresenceUnavailableException;

import com.example.proyectopeli.Conecction.ConectionBD;
import com.example.proyectopeli.Recurso.Recurso;
import com.example.proyectopeli.Entidad.Usuario;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Collection;


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


    public  Boolean Login (String usuario , String contrasena) {
        ResultSet resultado;
        Connection conexion = null;
        try {
            ConectionBD conectionBD = new ConectionBD();
            conexion = conectionBD.conectar();
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
        } finally {
            cerrarConexion(conexion);
        }
    }









    private void cerrarConexion(Connection conexion) {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




}
