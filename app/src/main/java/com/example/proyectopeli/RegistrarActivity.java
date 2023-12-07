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
import com.example.proyectopeli.Entidad.Correo;
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
        txtnombre = findViewById(R.id.txtNombre);;
        nombre = txtnombre.getText().toString();
        txtapellido = findViewById(R.id.txtApellido);
        apellido = txtapellido.getText().toString();
        txtnumero = findViewById(R.id.txtNumero);
        numero = txtnumero.getText().toString();
        txtcorreo = findViewById(R.id.txtCorreo);
        correo = txtcorreo.getText().toString();



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

    private class Registrar extends AsyncTask<Void, Void, Boolean> {

        private Usuario user = new Usuario();

        String htmlCorreo ;


        protected void onPreExecute() {
            user.setNombre(nombre);
            user.setApellido(apellido);
            user.setNumero(numero);
            user.setCorreo(correo);
            user.setClave(Recurso.generarClave());
        }



        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                String HTML = "<!DOCTYPE html>\n" +
                        "\n" +
                        "<html>\n" +
                        "<head>\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width\" />\n" +
                        "    <title>EnviarClave</title>\n" +
                        "</head>\n" +
                        "<body style=\"background-color:#EDF6FF\">\n" +
                        "    <br />\n" +
                        "    <br />\n" +
                        "    <div style=\"width:400px;border-radius:5px; margin:auto;background-color:#fff;box-shadow:0px 0px 10px  #DEDEDE;padding:20px\">\n" +
                        "        <table style=\"width:100%\">\n" +
                        "            <tr>\n" +
                        "                <td align=\"center\" colspan=\"2\">\n" +
                        "                    <h2 style=\"color:#004DAF\">Bienvenido</h2>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td align=\"left\" colspan=\"2\">\n" +
                        "                    <p>Se creó exitosamente tu usuario. Los detalles de tu cuenta son:</p>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td><h4 style=\"color:#004DAF;margin:2px\">Correo:</h4></td>\n" +
                        "                <td>"+ user.getCorreo() + "</td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td><h4 style=\"color:#004DAF;margin:2px\">Contraseña:</h4></td>\n" +
                        "                <td>"+ user.getClave() +"</td>\n" +
                        "            </tr>\n" +
                        "        </table>\n" +
                        "        <div style=\"background-color:#FFE1CE;padding:15px;margin-top:15px;margin-bottom:15px\">\n" +
                        "            <p style=\"margin:0px;color: #F45E00;\">Le recomendamos cambiar la contraseña una vez inicie sesión.</p>\n" +
                        "        </div>\n" +
                        "        <table>\n" +
                        "            <tr>\n" +
                        "                <td>Para iniciar sesión ingrese a la siguiente URL:</td>\n" +
                        "            </tr>\n" +
                        "        </table>\n" +
                        "        <a href=\"@ViewData[\"Url\"]\">Iniciar Sesión</a>\n" +
                        "    </div>\n" +
                        "    <br />\n" +
                        "    <br />\n" +
                        "</body>\n" +
                        "</html>";
                return  bll.Registar(user , HTML);
            } catch (Exception e) {
                Log.e("AsyncTaskError", "Error en doInBackground()", e);
                return false;
            }
        }


        @Override
        protected void onPostExecute(Boolean exitoso) {
            Correo correo1 = new Correo();
            if (exitoso) {
                Intent intent = new Intent(RegistrarActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                Valiciones("Error al registrar sus datos " + correo1.toJson() );
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