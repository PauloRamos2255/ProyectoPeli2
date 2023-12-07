package com.example.proyectopeli.ui.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectopeli.R;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;
import com.example.proyectopeli.Adapter.MovieAdapter;


import java.util.ArrayList;





public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView recycler;
    private MovieAdapter movieAdapter;
    private MovieAdapter popularAdapter;  // Nuevo adaptador para la lista popular
    private HomeViewModel homeViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewHorizontal);
        recycler = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        movieAdapter = new MovieAdapter(getActivity(),new ArrayList<>());
        popularAdapter = new MovieAdapter(getActivity(),new ArrayList<>());

        recyclerView.setAdapter(movieAdapter);
        recycler.setAdapter(popularAdapter);  // Utiliza el adaptador correcto

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        observeViewModel();

        return view;
    }

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


    }
}