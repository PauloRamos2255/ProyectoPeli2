package com.example.proyectopeli;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import com.example.proyectopeli.Conecction.ConectionBD;
import com.example.proyectopeli.Recurso.Recurso;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity {

    EditText txtusurio , txtclave;
    TextView lblregistrar;
    Button btnIngresar;
    TextView lblRecuperar;
    TextView lblCambiarClave;
    Connection db;
    String usuario;
    String contrasena;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        txtusurio =  findViewById(R.id.txtusuario);
        txtclave =  findViewById(R.id.txtclave);
        lblregistrar =  findViewById(R.id.lblregistrar);
        btnIngresar =  findViewById(R.id.btningresar);
        lblRecuperar = findViewById(R.id.lblRecuperar);
        lblCambiarClave = findViewById(R.id.lblCambiarClave);

        LayoutInflater inflater = this.getLayoutInflater();


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

        lblRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Recuperar Cuenta")
                        .setView(inflater.inflate(R.layout.fragment_recuperar_cuenta, null))  // Configura la vista del Fragment en el AlertDialog
                        .setPositiveButton("Recuperar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setNegativeButton("Cerrar" ,new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });


                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        lblCambiarClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Cambia tu clave!")
                        .setView(inflater.inflate(R.layout.fragment_cambiar_clave, null))  // Configura la vista del Fragment en el AlertDialog
                        .setPositiveButton("Cambiar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setNegativeButton("Cerrar" ,new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });


                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });




        if (!isNetworkAvailable(getApplicationContext())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setTitle("Alerta de Conexión")
                    .setView(inflater.inflate(R.layout.fragment_vali, null))  // Configura la vista del Fragment en el AlertDialog
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }


    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
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
                Intent intent = new Intent(LoginActivity.this , MenuPeli.class);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, "Error al conectar o credenciales incorrectas", Toast.LENGTH_SHORT).show();
            }
        }
    }

}