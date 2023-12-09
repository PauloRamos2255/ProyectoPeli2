package com.example.proyectopeli.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.Navigation;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.example.proyectopeli.BLL.UsuarioBLL;
import com.example.proyectopeli.EditarDatos;
import com.example.proyectopeli.Entidad.Movie;
import com.example.proyectopeli.Entidad.Usuario;
import com.example.proyectopeli.R;
import com.example.proyectopeli.databinding.FragmentNotificationsBinding;
import com.google.gson.Gson;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private NotificationsViewModel notificationsViewModel;

    UsuarioBLL bll = new UsuarioBLL();

    TextView cuenta;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        cuenta = root.findViewById(R.id.txtCuenta);
        LinearLayout liner = root.findViewById(R.id.linertrans);
        LinearLayout poli = root.findViewById(R.id.linerpoli);
        Toolbar toolbar = root.findViewById(R.id.toolbar);

        Bundle bundle = getArguments();
        if (bundle != null) {
            Usuario usuario = (Usuario) bundle.getSerializable("usuario");
            cuenta.setText(usuario.getCorreo());
        }

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

        // Reemplaza R.id.action_notificationsFragment_to_editarDatosFragment con el ID correcto de la acción
        navController.navigate(R.id.navigation_editar);
    }

    void selectPolitica(){
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);

        // Reemplaza R.id.action_notificationsFragment_to_editarDatosFragment con el ID correcto de la acción
        navController.navigate(R.id.navigation_politica);
    }


}