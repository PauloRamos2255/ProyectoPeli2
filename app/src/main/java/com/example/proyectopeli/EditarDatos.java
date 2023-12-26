package com.example.proyectopeli;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.proyectopeli.BLL.UsuarioBLL;
import com.example.proyectopeli.Conecction.ConectionBD;
import com.example.proyectopeli.Entidad.Usuario;
import com.example.proyectopeli.Recurso.Recurso;
import com.example.proyectopeli.databinding.FragmentEditarDatosBinding;
import com.example.proyectopeli.databinding.FragmentNotificationsBinding;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;


public class EditarDatos extends Fragment {

    TextView txtnombre ;
    TextView txtapellido;
    TextView txtcorreo;
    TextView txtnumero;
    Button editar , perfilfoto;
    ImageView ftfoto;
    String UrlImagten = "";
    Dialog dialog;
    int id , idUsuario;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;
    Usuario usuarios = new Usuario();

    private FragmentEditarDatosBinding binding;
    private static final int PICK_IMAGE_REQUEST = 1;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentEditarDatosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        SharedPreferences preferences = getActivity().getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);
         idUsuario= preferences.getInt("USUARIO", 2);

        txtnombre = root.findViewById(R.id.txtNombre);
        txtapellido = root.findViewById(R.id.txtApellido);
        txtcorreo = root.findViewById(R.id.txtCorreo);
        txtnumero = root.findViewById(R.id.txtTelefono);
        editar = root.findViewById(R.id.btneditar);
        perfilfoto = root.findViewById(R.id.btnPerfil);
        ftfoto = root.findViewById(R.id.foto);
        ftfoto.setScaleType(ImageView.ScaleType.CENTER_CROP);


        consultarpersonal(idUsuario) ;
        VerImagen(UrlImagten);



        perfilfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editar();
            }
        });



        return root;
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
                Toast.makeText( getContext(), "Abristes tu camara", Toast.LENGTH_SHORT).show();
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
                                                .into(ftfoto);

                                        GuardarImagen( idUsuario ,url);

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


    public void consultarpersonal( int idUsuario){

        try {
            Statement stm= ConectionBD.conectar().createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Usuarios WHERE id = " + idUsuario );
            if(rs.next()){
                txtnombre.setText(rs.getString(2));
                txtapellido.setText(rs.getString(3));
                txtnumero.setText(rs.getString(10));
                txtcorreo.setText(rs.getString(5));
                UrlImagten = rs.getString(11);
                id = rs.getInt(1);

            }
        }catch (Exception e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void Editar(){

        if(txtnombre.getText().toString().equals("")){
            Valiciones("Colocar su nombre");
        } else if(txtapellido.getText().toString().equals("")){
            Valiciones("Colocar su apellido");
        } else if (txtnumero.getText().toString().equals("")) {
            Valiciones("Colocar su numero");
        } else if (txtcorreo.getText().toString().equals("")) {
            Valiciones("Colocar su correo");
        }else{
            Usuario usuario = new Usuario();
            UsuarioBLL bll = new UsuarioBLL();
            usuario.setNombre(txtnombre.getText().toString());
            usuario.setApellido(txtapellido.getText().toString());
            usuario.setNumero(txtnumero.getText().toString());
            usuario.setCorreo(txtcorreo.getText().toString());
            usuario.setId(id);
            Boolean exitoso = bll.editarUsuario(usuario);
            if (exitoso){
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.navigation_notifications);
            }
            else{
                Valiciones("Hubo problema la Editar sus datos");
            }


        }

    }

    private void Valiciones(  String mensaje) {
        LayoutInflater inflater = getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                    .into(ftfoto);
        }
    }




}