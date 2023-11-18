package com.example.proyectopeli;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RegistrarActivity extends AppCompatActivity {

    TextView txtregresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar);

        txtregresar = (TextView) findViewById(R.id.txtRegresar);

        txtregresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrarActivity.this , LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}