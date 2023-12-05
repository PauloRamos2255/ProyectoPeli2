package com.example.proyectopeli.ui.dashboard;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.proyectopeli.Adapter.MovieAdapter;
import com.example.proyectopeli.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectopeli.databinding.FragmentDashboardBinding;
import com.example.proyectopeli.ui.home.HomeViewModel;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private DashboardViewModel homeViewModel;
    private EditText searchEditText;
    private String buscar;
    private RecyclerView recycler;
    private MovieAdapter Adapter;
    private Handler searchHandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        recycler = rootView.findViewById(R.id.recyclerbuscar);
        searchEditText = rootView.findViewById(R.id.txtbuscar);
        buscar = rootView.findViewById(R.id.txtbuscar).toString();
        int columna = 2;
        recycler.setLayoutManager(new GridLayoutManager(requireContext() , columna));
        Adapter = new MovieAdapter(new ArrayList<>());
        recycler.setAdapter(Adapter);

        homeViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        observeViewModel();

        // Configurar un listener para el cambio en el texto del EditText
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                // No es necesario en este ejemplo
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Iniciar la búsqueda después de un retraso de 1 segundo
                searchHandler.removeCallbacksAndMessages(null);
                searchHandler.postDelayed(() -> {
                    homeViewModel.searchMovies("8300bb0075ff37f5c25ab05fdeb18503", charSequence.toString());
                }, 1000); // 1000 milisegundos = 1 segundo
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No es necesario en este ejemplo
            }
        });
        return rootView;
    }

    private void observeViewModel() {
        homeViewModel.getSearchResults().observe(getViewLifecycleOwner(), carteleraMovies -> {
            if (carteleraMovies != null) {
                Adapter.updateMovies(carteleraMovies);
            }
        });
    }
}