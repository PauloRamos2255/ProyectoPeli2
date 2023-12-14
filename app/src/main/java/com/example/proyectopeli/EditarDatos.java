package com.example.proyectopeli;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectopeli.databinding.FragmentEditarDatosBinding;
import com.example.proyectopeli.databinding.FragmentNotificationsBinding;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class EditarDatos extends Fragment {

    TextView txtnombre ;
    TextView txtapellido;
    TextView txtcorreo;
    TextView txtnumero;
    private FragmentEditarDatosBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentEditarDatosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        SharedPreferences preferences = getActivity().getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);
        String correo= preferences.getString("USUARIO", "a@a.com");

        txtnombre = root.findViewById(R.id.txtNombre);
        txtapellido = root.findViewById(R.id.txtApellido);
        txtcorreo = root.findViewById(R.id.txtCorreo);
        txtnumero = root.findViewById(R.id.txtTelefono);
        txtcorreo.setText(correo);
        consultarpersonal(correo) ;

        return root;
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
                txtnombre.setText(rs.getString(2));
                txtapellido.setText(rs.getString(3));
                txtnumero.setText(rs.getString(10));
            }
        }catch (Exception e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}