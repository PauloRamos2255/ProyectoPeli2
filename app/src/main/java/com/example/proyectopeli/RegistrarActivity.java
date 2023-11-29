package com.example.proyectopeli;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

    private class Registrar extends AsyncTask<Void, Void, Boolean> {

        private Usuario user = new Usuario();
        String correo;
        String clave;

        String htmlCorreo ;


        protected void onPreExecute() {
            // Obtener referencias a las vistas
            EditText txtNombre = findViewById(R.id.txtNombre);
            EditText txtApellido = findViewById(R.id.txtApellido);
            EditText txtNumero = findViewById(R.id.txtNumero);
            EditText txtCorreo = findViewById(R.id.txtCorreo);

            // Obtener los valores de las vistas
            String nombre = txtNombre.getText().toString();
            String apellido = txtApellido.getText().toString();
            String numero = txtNumero.getText().toString();
             correo = txtCorreo.getText().toString();
             clave = Recurso.generarClave();

            // Establecer los valores en el objeto 'user'
            user.setNombre(nombre);
            user.setApellido(apellido);
            user.setNumero(numero);
            user.setCorreo(correo);
            user.setClave(Recurso.sha256(clave));
        }



        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                String correoCodificado = URLEncoder.encode(correo, "UTF-8");
                String claveCodificada = URLEncoder.encode(clave, "UTF-8");

                htmlCorreo  = "<h3>Su cuenta fue creada correctamente</h3></br><p>Su contraseña para acceder es : !clave!</p>";
                String Mensajecorreo = htmlCorreo.replace("!clave!", clave);


                return bll.Registar(user , Mensajecorreo );
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
                Toast.makeText(RegistrarActivity.this, "Error al registrar sus datos de " + user.getNombre(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}