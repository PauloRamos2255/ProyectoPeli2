package com.example.proyectopeli.BLL;

import android.net.Uri;

import com.example.proyectopeli.Conecction.ConectionBD;
import com.example.proyectopeli.Recurso.Recurso;
import com.example.proyectopeli.Entidad.Usuario;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
                    pstmt.setString(5, user.getClave());

                    int filasAfectadas = pstmt.executeUpdate();

                    if(filasAfectadas != 0){
                        if(htmlCorreo != null){
                            try {
                                String urlString = htmlCorreo;
                                URL url = new URL(urlString);
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                                int responseCode = connection.getResponseCode();

                                if (responseCode == HttpURLConnection.HTTP_OK) {
                                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                                        StringBuilder content = new StringBuilder();
                                        String line;

                                        while ((line = reader.readLine()) != null) {
                                            content.append(line);
                                        }

                                        htmlEnviado = content.toString();
                                    }
                                }
                                if(htmlEnviado != null){
                                    Recurso.enviarCorreo(user.getCorreo() , "Creacion de Cuenta" , htmlEnviado);
                                }

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

}
