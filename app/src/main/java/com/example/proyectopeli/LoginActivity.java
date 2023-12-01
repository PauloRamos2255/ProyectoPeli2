package com.example.proyectopeli;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.proyectopeli.BLL.UsuarioBLL;

import com.example.proyectopeli.Recurso.Recurso;


import java.sql.Connection;


public class LoginActivity extends AppCompatActivity {

    EditText txtusurio , txtclave;
    TextView lblregistrar;
    Button btnIngresar;
    TextView lblRecuperar;
    TextView lblCambiarClave;
    Connection db;
    String usuario;
    String contrasena;

    private ProgressDialog progressDialog;

    UsuarioBLL bll = new UsuarioBLL();



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
                                String correoR;
                                correoR = findViewById(R.id.txtRecuperar).toString();


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
                builder.setTitle("Cambiar clave")
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




        if (!Recurso.Conexion(getApplicationContext())) {
             SinConexxion();
        }

    }

    private void SinConexxion() {
        LayoutInflater inflater = getLayoutInflater();

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




    private class ConnectToDatabaseTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            usuario = txtusurio.getText().toString();
            contrasena = Recurso.sha256(txtclave.getText().toString());
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            ProgressDailog();
            Boolean login = bll.Login(usuario , contrasena);
            return  login;

        }


        protected void onPostExecute(Boolean exitoso) {
            if(!Recurso.Conexion(getApplicationContext())){
                 SinConexxion();
            }
            else if (exitoso) {
                Intent intent = new Intent(LoginActivity.this , MenuPeli.class);
                startActivity(intent);
            } else  {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Credenciales Incorrecta")
                        .setMessage("Las credenciales proporcionadas son incorrectas. Por favor, inténtalo de nuevo.")
                        .setNegativeButton("Aceptar" ,new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });


                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }

    }


    private void ProgressDailog(){
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Iniciando sesión...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

}