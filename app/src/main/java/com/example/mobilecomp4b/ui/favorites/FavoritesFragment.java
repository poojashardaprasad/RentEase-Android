package com.example.mobilecomp4b.ui.favorites;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mobilecomp4b.data.local.entity.PropertyEntity;
import com.example.mobilecomp4b.databinding.FragmentFavoritesBinding;
import com.example.mobilecomp4b.ui.adapter.PropertyAdapter;
import com.example.mobilecomp4b.ui.details.PropertyDetailsActivity;
import com.example.mobilecomp4b.viewmodel.FavoritesViewModel;

public class FavoritesFragment extends Fragment implements PropertyAdapter.OnPropertyClickListener {

    private FragmentFavoritesBinding binding;
    private FavoritesViewModel viewModel;
    private PropertyAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);

        adapter = new PropertyAdapter(this);
        binding.rvSavedProperties.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvSavedProperties.setAdapter(adapter);

        viewModel.getFavoriteProperties().observe(getViewLifecycleOwner(), properties -> {
            if (properties == null || properties.isEmpty()) {
                binding.rvSavedProperties.setVisibility(View.GONE);
                binding.layoutEmptyFavorites.setVisibility(View.VISIBLE);
            } else {
                binding.rvSavedProperties.setVisibility(View.VISIBLE);
                binding.layoutEmptyFavorites.setVisibility(View.GONE);
                adapter.submitList(properties);
            }
        });
    }

    @Override
    public void onPropertyClick(PropertyEntity property) {
        Intent intent = new Intent(requireContext(), PropertyDetailsActivity.class);
        intent.putExtra(PropertyDetailsActivity.EXTRA_PROPERTY_ID, property.getId());
        startActivity(intent);
    }

    @Override
    public void onFavoriteToggle(PropertyEntity property) {
        viewModel.removeFavorite(property.getId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
