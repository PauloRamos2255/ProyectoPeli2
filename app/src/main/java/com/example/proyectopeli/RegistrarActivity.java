package com.example.proyectopeli;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectopeli.BLL.UsuarioBLL;
import com.example.proyectopeli.Entidad.Usuario;
import com.example.proyectopeli.Recurso.Recurso;

import java.net.URLEncoder;

public class RegistrarActivity extends AppCompatActivity {


   public  UsuarioBLL bll = new UsuarioBLL();
   Button registrar;
   TextView txtregresar;
   String  nombre ,apellido,numero, correo;
   TextView  txtnombre , txtapellido, txtnumero, txtcorreo;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar);

        txtregresar = (TextView) findViewById(R.id.txtRegresar);
        registrar = (Button) findViewById(R.id.btnRegistar);
        //Caja de Texto
        txtnombre = findViewById(R.id.txtNombre);
        txtapellido = findViewById(R.id.txtApellido);
        txtnumero = findViewById(R.id.txtNumero);
        txtcorreo = findViewById(R.id.txtCorreo);

        //Texto de las cajas
        nombre = txtnombre.toString().trim();
        apellido = txtapellido.toString().trim();
        numero = txtnumero.toString();
        correo = txtcorreo.toString();


        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Recurso.Conexion(getApplicationContext())) {
                    SinConexxion();
                }
                else if (nombre.toString().isEmpty()||apellido.toString().isEmpty()||numero.toString().isEmpty()||correo.toString().isEmpty()){
                    Valiciones("LLene todos los campos");
                }
                else {
                    new Registrar().execute();
                }

                Log.d("CampoNombre", "Contenido: " + nombre);



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

    private class Registrar extends AsyncTask<Void, Void, Boolean> {

        private Usuario user = new Usuario();

        String htmlCorreo ;


        protected void onPreExecute() {
            user.setNombre(findViewById(R.id.txtNombre).toString() );
            user.setApellido(findViewById(R.id.txtApellido).toString());
            user.setNumero(findViewById(R.id.txtNumero).toString());
            user.setCorreo(findViewById(R.id.txtCorreo).toString());
            user.setClave(Recurso.sha256(Recurso.generarClave()));
        }



        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Boolean exitoso = false;
                String correoCodificado = URLEncoder.encode(user.getCorreo(), "UTF-8");
                String claveCodificada = URLEncoder.encode(user.getClave(), "UTF-8");


                    htmlCorreo  = "<h3>Su cuenta fue creada correctamente</h3></br><p>Su contraseña para acceder es : !clave!</p>";
                    String Mensajecorreo = htmlCorreo.replace("!clave!", user.getClave());

                    exitoso = bll.Registar(user , Mensajecorreo );


                return  exitoso;

            } catch (Exception e) {
                Log.e("AsyncTaskError", "Error en doInBackground()", e);
                return false;
            }
        }


        @Override
        protected void onPostExecute(Boolean exitoso) {
            if (exitoso) {
                Intent intent = new Intent(RegistrarActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                Valiciones("Error al registrar sus datos de " + user.getNombre());
            }
        }
    }

    private void Valiciones(  String mensaje) {
        LayoutInflater inflater = getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrarActivity.this);
        builder.setTitle("Error :")
                .setMessage(mensaje)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void SinConexxion() {
        LayoutInflater inflater = getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrarActivity.this);
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