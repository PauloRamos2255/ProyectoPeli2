package com.example.proyectopeli;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.proyectopeli.Conecction.ConectionBD;
import com.example.proyectopeli.Recurso.Recurso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity {

    EditText txtusurio , txtclave;
    TextView lblregistrar;
    Button btnIngresar;
    Connection db;
    String usuario;
    String contrasena;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        txtusurio = (EditText) findViewById(R.id.txtusuario);
        txtclave = (EditText) findViewById(R.id.txtclave);
        lblregistrar = (TextView) findViewById(R.id.lblregistrar);
        btnIngresar = (Button) findViewById(R.id.btningresar);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ConnectToDatabaseTask().execute();
            }
        });

        lblregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrarActivity.class);
                startActivity(intent);
            }
        });

    }

    private class ConnectToDatabaseTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            usuario = txtusurio.getText().toString();
            contrasena = Recurso.sha256(txtclave.getText().toString());
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Connection conexion = ConectionBD.conectar();
                if (conexion != null) {
                    String consulta = "SELECT * FROM Usuarios  WHERE Correo = ? AND Contraseña = ?";

                    try (PreparedStatement pstmt = conexion.prepareStatement(consulta)) {
                        pstmt.setString(1, usuario);
                        pstmt.setString(2, contrasena);

                        ResultSet resultado = pstmt.executeQuery();

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

        @Override
        protected void onPostExecute(Boolean exitoso) {
            if (exitoso) {
                Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, "Error al conectar o credenciales incorrectas", Toast.LENGTH_SHORT).show();
            }
        }
    }

}