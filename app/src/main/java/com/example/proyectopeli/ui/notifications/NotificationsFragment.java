package com.example.proyectopeli.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.Navigation;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.proyectopeli.BLL.UsuarioBLL;
import com.example.proyectopeli.Entidad.Usuario;
import com.example.proyectopeli.LoginActivity;
import com.example.proyectopeli.R;
import com.example.proyectopeli.databinding.FragmentNotificationsBinding;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private NotificationsViewModel notificationsViewModel;

    UsuarioBLL bll = new UsuarioBLL();
    Usuario usuario = new Usuario();
    String nombre , apellido , numero, URLImagen;
    TextView cuenta;
    ImageView imagenfoto ;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);

        SharedPreferences preferences = getActivity().getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);
        int iidUsuairo = preferences.getInt("USUARIO", 2);
        View root = binding.getRoot();
        cuenta = root.findViewById(R.id.txtCuenta);
        imagenfoto = root.findViewById(R.id.imageView);
        imagenfoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LinearLayout liner = root.findViewById(R.id.linertrans);
        LinearLayout poli = root.findViewById(R.id.linerpoli);
        LinearLayout cerrar = root.findViewById(R.id.cerrarS);
        consultarpersonal(iidUsuairo);

        VerImagen(URLImagen);
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

}