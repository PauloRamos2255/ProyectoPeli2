package com.example.proyectopeli.ui.notifications;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.navigation.Navigation;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.proyectopeli.BLL.UsuarioBLL;
import com.example.proyectopeli.CamaraActivity;
import com.example.proyectopeli.Conecction.ConectionBD;
import com.example.proyectopeli.Entidad.Usuario;
import com.example.proyectopeli.LoginActivity;
import com.example.proyectopeli.R;
import com.example.proyectopeli.databinding.FragmentNotificationsBinding;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;


public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private NotificationsViewModel notificationsViewModel;

    UsuarioBLL bll = new UsuarioBLL();
    Usuario usuario = new Usuario();
    String nombre , apellido , numero, URLImagen;
    TextView cuenta;
    ImageView imagenfoto , imggalery ;
    Dialog dialog;
    int iidUsuairo ;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);

        SharedPreferences preferences = getActivity().getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);
         iidUsuairo = preferences.getInt("USUARIO", 2);
        View root = binding.getRoot();
        cuenta = root.findViewById(R.id.txtCuenta);
        imggalery = root.findViewById(R.id.imgboton);
        imagenfoto = root.findViewById(R.id.imageView);
        imagenfoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LinearLayout liner = root.findViewById(R.id.linertrans);
        LinearLayout poli = root.findViewById(R.id.linerpoli);
        LinearLayout cerrar = root.findViewById(R.id.cerrarS);
        consultarpersonal(iidUsuairo);

        VerImagen(URLImagen);

        imggalery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 showDialog();
            }
        });

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getActivity(), LoginActivity.class);
                guardarSesionInactiva();
                startActivity(intent);
            }
        });


        poli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPolitica();
            }
        });


        liner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFragment();
            }
        });

        return root;
    }

    void selectFragment() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);

        navController.navigate(R.id.navigation_editar);

    }

    void selectPolitica(){
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);

        // Reemplaza R.id.action_notificationsFragment_to_editarDatosFragment con el ID correcto de la acción
        navController.navigate(R.id.navigation_politica);
    }


    public Connection conexionBD(){
        Connection cnn =null;
        try {
            StrictMode.ThreadPolicy politica= new  StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(politica);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            cnn= DriverManager.getConnection("jdbc:jtds:sqlserver://TADIAdmin.mssql.somee.com;user=PauloRamos_SQLLogin_1;password=8zmhlf3lxk;databaseName=TADIAdmin");

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return cnn;
    }

    public void consultarpersonal( int idUsuario){

        try {
            Statement stm= conexionBD().createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Usuarios WHERE id = " + idUsuario );
            if(rs.next()){
                nombre = rs.getString(2);
                apellido = rs.getString(3);
                numero = rs.getString(10);
                URLImagen = rs.getString(11);
                cuenta.setText( nombre + " " + apellido);
            }
        }catch (Exception e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarSesionInactiva() {
        SharedPreferences preferences = getActivity().getSharedPreferences("sesion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("sesionActiva", false);
        editor.apply();
    }


    private void VerImagen( String UrlImagen){
        if(UrlImagen != ""){
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.defaultplaceholder)
                    .error(R.drawable.img);
            Glide.with(getActivity())
                    .load(UrlImagen)
                    .apply(requestOptions)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .error(R.drawable.img)
                    .into(imagenfoto);
        }
    }

    private void showDialog() {
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.options_perfil);
        LinearLayout camaralayout = dialog.findViewById(R.id.options);
        LinearLayout galeryLayout = dialog.findViewById(R.id.options2);
        camaralayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getActivity() , CamaraActivity.class);
               startActivity(intent);
            }
        });

        galeryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intent);
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialoAnimatios;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }


    private final ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        // Crear una referencia única en Firebase Storage
                        String nombreArchivo = UUID.randomUUID().toString() + ".jpg";

                        // Especificar la ubicación donde deseas almacenar el archivo (por ejemplo, "images/")
                        StorageReference imagenRef = storage.getReference().child("images/" + nombreArchivo);

                        // Subir la imagen a Firebase Storage
                        UploadTask uploadTask = imagenRef.putFile(uri);

                        // Manejar el resultado del proceso de carga
                        uploadTask.addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // La imagen se ha subido exitosamente, ahora puedes obtener la URL de descarga
                                imagenRef.getDownloadUrl().addOnCompleteListener(downloadTask -> {
                                    if (downloadTask.isSuccessful()) {
                                        String url = downloadTask.getResult().toString();
                                        RequestOptions requestOptions = new RequestOptions()
                                                .placeholder(R.drawable.defaultplaceholder)
                                                .error(R.drawable.image);
                                        Glide.with(getActivity())
                                                .load(url)
                                                .apply(requestOptions)
                                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                                .error(R.drawable.image)
                                                .into(imagenfoto);

                                        GuardarImagen( iidUsuairo ,url);

                                    } else {
                                        Log.e("Error", "Error al obtener la URL de descarga", downloadTask.getException());
                                    }
                                });
                            } else {
                                Log.e("Error", "Error al subir la imagen", task.getException());
                            }
                        });

                    }
                }
            });

    private boolean GuardarImagen(int ID, String url) {
        try {
            Connection conexion = ConectionBD.conectar();
            if (conexion != null) {
                String consulta = "UPDATE Usuarios SET Imagen=? WHERE id=?";

                try (PreparedStatement pstmt = conexion.prepareStatement(consulta)) {
                    pstmt.setString(1, url);
                    pstmt.setInt(2, ID); // Cambiado de 'id' a 'ID'

                    int filasAfectadas = pstmt.executeUpdate();

                    return filasAfectadas != 0;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }


}