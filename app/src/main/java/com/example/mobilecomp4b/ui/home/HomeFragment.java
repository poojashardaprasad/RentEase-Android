package com.example.mobilecomp4b.ui.home;

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
import com.example.mobilecomp4b.databinding.FragmentHomeBinding;
import com.example.mobilecomp4b.ui.adapter.PropertyAdapter;
import com.example.mobilecomp4b.ui.details.PropertyDetailsActivity;
import com.example.mobilecomp4b.ui.main.MainActivity;
import com.example.mobilecomp4b.viewmodel.HomeViewModel;

public class HomeFragment extends Fragment implements PropertyAdapter.OnPropertyClickListener {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private PropertyAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        adapter = new PropertyAdapter(this);
        binding.rvFeaturedProperties.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvFeaturedProperties.setAdapter(adapter);

        viewModel.getFeaturedProperties().observe(getViewLifecycleOwner(), properties -> {
            if (properties != null) {
                adapter.submitList(properties);
            }
        });

        binding.btnFindRental.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToBrowse("Both");
            }
        });

        binding.tvViewAllRentals.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToBrowse("Both");
            }
        });

        binding.btnCategoryAll.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToBrowse("Both");
            }
        });

        binding.btnCategoryStudents.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToBrowse("Students");
            }
        });

        binding.btnCategoryFamilies.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToBrowse("Families");
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
        viewModel.toggleFavorite(property.getId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
