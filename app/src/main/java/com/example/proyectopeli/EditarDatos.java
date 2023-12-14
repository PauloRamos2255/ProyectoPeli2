package com.example.proyectopeli;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectopeli.BLL.UsuarioBLL;
import com.example.proyectopeli.Entidad.Usuario;
import com.example.proyectopeli.Recurso.Recurso;
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
    Button editar;
    int id;
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
        editar = root.findViewById(R.id.btneditar);
        txtcorreo.setText(correo);
        consultarpersonal(correo) ;

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editar();
            }
        });



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


}