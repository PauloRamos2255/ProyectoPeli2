package com.example.proyectopeli.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.Navigation;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
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
    String nombre;
    String apellido;
    String numero;
    TextView cuenta;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);

        SharedPreferences preferences = getActivity().getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);
        String correo= preferences.getString("USUARIO", "a@a.com");
        View root = binding.getRoot();
        cuenta = root.findViewById(R.id.txtCuenta);
        LinearLayout liner = root.findViewById(R.id.linertrans);
        LinearLayout poli = root.findViewById(R.id.linerpoli);
        LinearLayout cerrar = root.findViewById(R.id.cerrarS);
        consultarpersonal(correo);

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

    public void consultarpersonal( String correo){

        try {
            Statement stm= conexionBD().createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Usuarios WHERE correo = '" + correo + "'");
            if(rs.next()){
                nombre = rs.getString(2);
                apellido = rs.getString(3);
                numero = rs.getString(10);
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

}