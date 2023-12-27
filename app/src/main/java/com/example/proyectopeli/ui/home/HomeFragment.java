package com.example.proyectopeli.ui.home;


import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectopeli.Entidad.Movie;
import com.example.proyectopeli.R;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;
import com.example.proyectopeli.Adapter.MovieAdapter;
import com.facebook.shimmer.ShimmerFrameLayout;


import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView recycler;
    private RecyclerView RecyclerViewTendencia;
    private RecyclerView RecyclerEstrenos;
    private ShimmerFrameLayout shimmerFrameLayout ;


    private MovieAdapter movieAdapter;
    private MovieAdapter popularAdapter;
    private MovieAdapter tendenciaAdapter;

    private MovieAdapter estrenosAdapter;

    private HomeViewModel homeViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewHorizontal);
        recycler = view.findViewById(R.id.recyclerView);
        RecyclerViewTendencia = view.findViewById(R.id.tendencia);
        RecyclerEstrenos = view.findViewById(R.id.estrenos);
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout);


        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        RecyclerViewTendencia.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        RecyclerEstrenos.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        movieAdapter = new MovieAdapter(getActivity(),new ArrayList<>());
        popularAdapter = new MovieAdapter(getActivity(),new ArrayList<>());
        tendenciaAdapter = new MovieAdapter(getActivity(),new ArrayList<>());
        estrenosAdapter = new MovieAdapter(getActivity(), new ArrayList<>());

        recyclerView.setAdapter(movieAdapter);
        recycler.setAdapter(popularAdapter);
        RecyclerViewTendencia.setAdapter(tendenciaAdapter);
        RecyclerEstrenos.setAdapter(estrenosAdapter);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        observeViewModel();

        return view;
    }


    private void observeViewModel() {
        observeMovies(homeViewModel.getCarteleraMovies(), movieAdapter );
        observeMovies(homeViewModel.getPopularMovies(), popularAdapter);
        observeMovies(homeViewModel.getTendenciaMovies(), tendenciaAdapter );
        observeMovies(homeViewModel.getEstrenosMovies(), estrenosAdapter );
    }

    private void observeMovies(LiveData<List<Movie>> moviesLiveData, MovieAdapter movieAdapter) {
        moviesLiveData.observe(getViewLifecycleOwner(), movies -> {
            if (movies != null) {
                movieAdapter.updateMovies(movies);
                shimmerFrameLayout.startShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        });

        shimmerFrameLayout.startShimmerAnimation();
        new Handler().postDelayed(() -> {
            shimmerFrameLayout.stopShimmerAnimation();
            shimmerFrameLayout.setVisibility(View.GONE);
        }, 8000);
    }

}