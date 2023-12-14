package com.example.proyectopeli;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Carga extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carga);

        ProgressBar progressBar = findViewById(R.id.progressBar);
        TextView pocentaje = findViewById(R.id.txtPorcentaje);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Hacer la ProgressBar visible al inicio
                progressBar.setVisibility(View.VISIBLE);

                // Simular carga durante 2 segundos
                CountDownTimer countDownTimer = new CountDownTimer(3000, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        int progreso = (int) (100 * (3000 - millisUntilFinished) / 3000);
                        progressBar.setProgress(progreso);
                        pocentaje.setText(String.format("%d%%", progreso));
                    }

                    @Override
                    public void onFinish() {
                        // Hacer la ProgressBar invisible al finalizar
                        progressBar.setVisibility(View.INVISIBLE);

                        // Iniciar la MenuPeliActivity después de la carga
                        Intent intent = new Intent(Carga.this, MenuPeli.class);
                        startActivity(intent);

                        // Cierra la actividad de SplashScreen
                        finish();
                    }
                };

                countDownTimer.start();
            }
        }, 3000);


    }


}