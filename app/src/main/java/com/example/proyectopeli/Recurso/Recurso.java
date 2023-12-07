package com.example.proyectopeli.Recurso;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.proyectopeli.Entidad.Usuario;

import java.nio.charset.StandardCharsets;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import java.util.Properties;
import java.util.UUID;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class Recurso {



    public static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convierte el hash a una representación hexadecimal
            StringBuilder builder = new StringBuilder();
            for (byte b : hashBytes) {
                builder.append(String.format("%02x", b));
            }

            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String generarClave() {
        String clave = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
        return clave;
    }




    public static boolean enviarCorreo(String correoDestino, String asunto, String mensaje) {
        try {
             Usuario user = new Usuario();
            String smtpHost = "smtp.gmail.com";
            String smtpPort = "587";
            final String username = "anuelitoramos@gmail.com";
            final String password = "xblcxgcvgubforax";
            String fromEmail = "anuelitoramos@gmail.com";
            String fromName = "Paulo Ramos";

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", smtpHost);
            props.put("mail.smtp.port", smtpPort);

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail, fromName));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correoDestino));
            message.setSubject(asunto);
            message.setContent("<p>Este es un párrafo de ejemplo. Puedes agregar tu propio texto aquí. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer nec odio. Praesent libero. Sed cursus ante dapibus diam.</p>", "text/html");


            Transport.send(message);


            return true;
        } catch (Exception e) {
            e.printStackTrace(); // Maneja la excepción de manera adecuada para tu aplicación
            return false;
        }
    }




    public static boolean Conexion(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }





}
