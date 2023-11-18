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

public class RegistrarActivity extends AppCompatActivity {

    TextView txtregresar;
    EditText txtNombre;
    EditText txtApellido;
    EditText txtNumero;
    EditText txtCorreo;
    String clave;
    Button registrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar);

        txtregresar = (TextView) findViewById(R.id.txtRegresar);
        registrar = (Button) findViewById(R.id.btnRegistar);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Registrar().execute();
            }
        });


        txtregresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrarActivity.this , LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private class Registrar extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected void onPreExecute() {
            txtNombre = (EditText)  findViewById(R.id.txtNombre);
            txtApellido = (EditText)  findViewById(R.id.txtApellido);
            txtNumero = (EditText)  findViewById(R.id.txtNumero);
            txtCorreo = (EditText) findViewById(R.id.txtCorreo);
            clave = Recurso.generarClave();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Connection conexion = ConectionBD.conectar();
                if (conexion != null) {
                    String consulta = "INSERT INTO Usuarios (Nombres, Apellidos, Numero, Correo, Contraseña)VALUES(?,?,?,?,?) ";

                    try (PreparedStatement pstmt = conexion.prepareStatement(consulta)) {
                        pstmt.setString(1, txtNombre.getText().toString());
                        pstmt.setString(2, txtApellido.getText().toString());
                        pstmt.setString(3,txtNumero.getText().toString());
                        pstmt.setString(4,txtCorreo.getText().toString());
                        pstmt.setString(5,Recurso.sha256(clave));

                        int filasAfectadas = pstmt.executeUpdate();
                        return filasAfectadas > 0;
                    }catch (SQLException e) {
                        e.printStackTrace();
                        return false;
                    } finally {
                        try {
                            conexion.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    return false;
                }

            } catch (Exception exception) {
                exception.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean exitoso) {
            if (exitoso) {
                Intent intent = new Intent(RegistrarActivity.this , LoginActivity.class);
                startActivity(intent);
            } else{
                Toast.makeText(RegistrarActivity.this, "Error al registrar sus datos", Toast.LENGTH_SHORT).show();
            }
        }


    }

}