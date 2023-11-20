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
            String correo = txtCorreo.getText().toString();
            String clave = Recurso.generarClave();

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
                return bll.Registar(user);
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