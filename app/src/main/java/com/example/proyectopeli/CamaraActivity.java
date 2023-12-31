package com.example.proyectopeli;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;


public class CamaraActivity extends AppCompatActivity {

    ImageButton toggleFlash , flipCamer;

    CardView capture;

    private PreviewView previewView;
    int cameraFacing = CameraSelector.LENS_FACING_BACK;
    private  final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean o) {
            if (o){
                startCamera(cameraFacing);
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara);
        previewView = findViewById(R.id.camara);
        capture = findViewById(R.id.capture);
        toggleFlash =findViewById(R.id.flashon);
        flipCamer = findViewById(R.id.flipoff);
        if(ContextCompat.checkSelfPermission(CamaraActivity.this , android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            activityResultLauncher.launch(Manifest.permission.CAMERA);
        }else {
            startCamera(cameraFacing);
        }
        flipCamer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cameraFacing == CameraSelector.LENS_FACING_BACK){
                    cameraFacing = CameraSelector.LENS_FACING_FRONT;
                }else {
                    cameraFacing = CameraSelector.LENS_FACING_BACK;
                }
                startCamera(cameraFacing);
            }
        });

    }


    public void startCamera(int cameraFacing) {
        int aspectRatio = aspectRatio(previewView.getWidth(), previewView.getHeight());
        ListenableFuture<ProcessCameraProvider> listenableFuture = ProcessCameraProvider.getInstance(this);

        listenableFuture.addListener(() -> {

        }, ContextCompat.getMainExecutor(this));

        listenableFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = listenableFuture.get();
                    Preview preview = new Preview.Builder().setTargetAspectRatio(aspectRatio).build();
                    ImageCapture imageCapture = new ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                            .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation()).build();

                    CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(cameraFacing).build();
                    cameraProvider.unbindAll();

                    Camera camera = cameraProvider.bindToLifecycle(CamaraActivity.this, cameraSelector, preview, imageCapture);
                    capture.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            takePicture(imageCapture);
                        }
                    });

                    toggleFlash.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setFlashIcon(camera);
                        }
                    });

                    preview.setSurfaceProvider(previewView.getSurfaceProvider());

                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }


    public void takePicture(ImageCapture imageCapture) {
        File file = new File(getExternalFilesDir(null), System.currentTimeMillis() + ".jpg");
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
        imageCapture.takePicture(outputFileOptions, Executors.newCachedThreadPool(), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Aquí se inicia la nueva actividad y se pasa la ruta de la imagen capturada como argumento
                        Intent intent = new Intent(CamaraActivity.this, FotoActivity.class);
                        intent.putExtra("imagePath", file.getPath());

                        // Obtener la orientación de la imagen y pasarla a la actividad
                        int rotation = getRotationFromImage(file);
                        intent.putExtra("imageRotation", rotation);

                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CamaraActivity.this, "Failed to save : " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        // Puedes decidir si reiniciar la cámara en caso de error
                        startCamera(cameraFacing);
                    }
                });
            }
        });
    }



    private void setFlashIcon(Camera camera){
        if(camera.getCameraInfo().hasFlashUnit()){
            if (camera.getCameraInfo().getTorchState().getValue() == 0){
                camera.getCameraControl().enableTorch(true);
                toggleFlash.setImageResource(R.drawable.baseline_flash_off_24);

            }else{
                camera.getCameraControl().enableTorch(false);
                toggleFlash.setImageResource(R.drawable.baseline_flash_on_24);
            }
        }else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(CamaraActivity.this, "Flash is not available currenty", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private  int aspectRatio(int width , int height ){
        double previewRatio = (double)  Math.max(width,height) / Math.min(width, height);
        if(Math.abs(previewRatio - 4.0 / 3.0) <= Math.abs(previewRatio - 16.0 /9.0)){
            return AspectRatio.RATIO_4_3;
        }
        return AspectRatio.RATIO_16_9;

    }


    private int getRotationFromImage(File imageFile) {
        try {
            ExifInterface exifInterface = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
                default:
                    return 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }


}
