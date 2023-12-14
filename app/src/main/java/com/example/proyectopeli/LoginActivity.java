package com.example.proyectopeli;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class LoginActivity extends AppCompatActivity {

    private EditText txtusurio, txtclave;
    private TextView lblregistrar;
    private Button btnIngresar;

    private UsuarioBLL bll = new UsuarioBLL();
    private Usuario usuario = new Usuario();

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Future<Boolean> loginFuture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        txtusurio = findViewById(R.id.txtusuario);
        txtclave = findViewById(R.id.txtclave);
        lblregistrar = findViewById(R.id.lblregistrar);
        btnIngresar = findViewById(R.id.btningresar);

        LayoutInflater inflater = this.getLayoutInflater();

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onIngresarClick();
            }
        });

        lblregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegistrarClick();
            }
        });

        if (!Recurso.Conexion(getApplicationContext())) {
            SinConexion();
        }
    }

    private void onIngresarClick() {
        usuario.setCorreo(txtusurio.getText().toString());
        usuario.setClave(Recurso.sha256(txtclave.getText().toString()));

        loginFuture = executorService.submit(() -> bll.Login(usuario.getCorreo(), usuario.getClave()));

        // Puedes mostrar un indicador de carga aquí si es necesario

        // Espera el resultado en segundo plano
        executorService.execute(() -> {
            try {
                boolean exitoso = loginFuture.get();

                runOnUiThread(() -> {
                    // Puedes ocultar el indicador de carga aquí si es necesario

                    if (!Recurso.Conexion(getApplicationContext())) {
                        SinConexion();
                    } else if (exitoso) {
                        Intent intent = new Intent(LoginActivity.this, MenuPeli.class);
                        SharedPreferences preferences = LoginActivity.this.getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("USUARIO", usuario.getCorreo());
                        editor.apply();
                        startActivity(intent);
                    } else {
                        mostrarCredencialesIncorrectas();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void onRegistrarClick() {
        Intent intent = new Intent(LoginActivity.this, RegistrarActivity.class);
        startActivity(intent);
    }

    private void SinConexion() {
        LayoutInflater inflater = getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Alerta de Conexión")
                .setView(inflater.inflate(R.layout.fragment_vali, null))
                .setPositiveButton("Aceptar", (dialogInterface, i) -> dialogInterface.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void mostrarCredencialesIncorrectas() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Credenciales Incorrecta")
                .setMessage("Las credenciales proporcionadas son incorrectas. Por favor, inténtalo de nuevo.")
                .setNegativeButton("Aceptar", (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdownNow(); // Detener la ejecución de tareas en segundo plano al destruir la actividad
    }





}