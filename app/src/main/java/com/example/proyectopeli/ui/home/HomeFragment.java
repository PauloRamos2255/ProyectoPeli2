package com.example.proyectopeli.ui.home;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectopeli.Entidad.Movie;
import com.example.proyectopeli.R;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;
import com.example.proyectopeli.Adapter.MovieAdapter;


import java.util.ArrayList;





public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView recycler;
    private RecyclerView RecyclerViewTendencia;
    private RecyclerView RecyclerEstrenos;
    private RecyclerView RecyclerReciente;
    private MovieAdapter movieAdapter;
    private MovieAdapter popularAdapter;
    private MovieAdapter tendenciaAdapter;
    private MovieAdapter recienteAdapter;
    private MovieAdapter estrenosAdapter;

    private HomeViewModel homeViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewHorizontal);
        recycler = view.findViewById(R.id.recyclerView);
        RecyclerViewTendencia = view.findViewById(R.id.tendencia);
        RecyclerEstrenos = view.findViewById(R.id.estrenos);
        RecyclerReciente = view.findViewById(R.id.recientes);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        RecyclerViewTendencia.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        RecyclerEstrenos.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        RecyclerReciente.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        movieAdapter = new MovieAdapter(getActivity(),new ArrayList<>());
        popularAdapter = new MovieAdapter(getActivity(),new ArrayList<>());
        tendenciaAdapter = new MovieAdapter(getActivity(),new ArrayList<>());
        estrenosAdapter = new MovieAdapter(getActivity(), new ArrayList<>());
        recienteAdapter = new MovieAdapter(getActivity(),new ArrayList<>());

        recyclerView.setAdapter(movieAdapter);
        recycler.setAdapter(popularAdapter);
        RecyclerViewTendencia.setAdapter(tendenciaAdapter);
        RecyclerEstrenos.setAdapter(estrenosAdapter);
        RecyclerReciente.setAdapter(recienteAdapter);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        observeViewModel();

        return view;
    }
    Movie movie;

    private void observeViewModel() {
        homeViewModel.getCarteleraMovies().observe(getViewLifecycleOwner(), carteleraMovies -> {
            if (carteleraMovies != null) {
                movieAdapter.updateMovies(carteleraMovies);
            }
        });

        homeViewModel.getPopularMovies().observe(getViewLifecycleOwner(), popularMovies -> {
            if (popularMovies != null) {
                popularAdapter.updateMovies(popularMovies);
            }
        });

        homeViewModel.getTendenciaMovies().observe(getViewLifecycleOwner(), tendenciaMovies -> {
            if (tendenciaMovies != null) {
                tendenciaAdapter.updateMovies(tendenciaMovies);
            }
        });

        homeViewModel.getEstrenosMovies().observe(getViewLifecycleOwner(), estrenosMovies -> {
            if (estrenosMovies != null) {
                estrenosAdapter.updateMovies(estrenosMovies);
            }
        });

        homeViewModel.getRecienteMovies().observe(getViewLifecycleOwner(), recienteMovies -> {
            if (recienteMovies != null) {
                recienteAdapter.updateMovies(recienteMovies);
            }
        });




    }
}