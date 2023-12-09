package com.example.proyectopeli;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

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

import com.example.proyectopeli.Entidad.Usuario;
import com.example.proyectopeli.Recurso.Recurso;
import com.example.proyectopeli.ui.notifications.NotificationsFragment;
import com.google.gson.Gson;


import java.sql.Connection;


public class LoginActivity extends AppCompatActivity {

    EditText txtusurio , txtclave;
    TextView lblregistrar;
    Button btnIngresar;
    Connection db;
    private Context context;


    private ProgressDialog progressDialog;

    UsuarioBLL bll = new UsuarioBLL();
    Usuario usuario = new Usuario();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        txtusurio =  findViewById(R.id.txtusuario);
        txtclave =  findViewById(R.id.txtclave);
        lblregistrar =  findViewById(R.id.lblregistrar);
        btnIngresar =  findViewById(R.id.btningresar);



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
            usuario.setCorreo( txtusurio.getText().toString());
            usuario.setClave(Recurso.sha256(txtclave.getText().toString()));
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Boolean login = bll.Login(usuario.getCorreo() , usuario.getClave());
            return  login;
        }


        protected void onPostExecute(Boolean exitoso) {
            if(!Recurso.Conexion(getApplicationContext())){
                 SinConexxion();
            }
            else if (exitoso) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("usuario", usuario);

                NotificationsFragment tuFragment = new NotificationsFragment();
                tuFragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_main, tuFragment)
                        .commit();
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




}