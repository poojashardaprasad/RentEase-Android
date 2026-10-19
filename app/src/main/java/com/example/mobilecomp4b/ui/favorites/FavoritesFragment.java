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

import com.example.mobilecomp4b.R;
import com.example.mobilecomp4b.data.local.entity.PropertyEntity;
import com.example.mobilecomp4b.databinding.FragmentFavoritesBinding;
import com.example.mobilecomp4b.ui.adapter.PropertyAdapter;
import com.example.mobilecomp4b.ui.details.PropertyDetailsActivity;
import com.example.mobilecomp4b.utils.CurrencyFormatter;
import com.example.mobilecomp4b.viewmodel.FavoritesViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class FavoritesFragment extends Fragment implements PropertyAdapter.OnPropertyClickListener, OnMapReadyCallback {

    private FragmentFavoritesBinding binding;
    private FavoritesViewModel viewModel;
    private PropertyAdapter adapter;
    private GoogleMap googleMap;
    private List<PropertyEntity> savedProperties;
    private boolean isMapViewActive = false;

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

        setupMapFragment();

        viewModel.getFavoriteProperties().observe(getViewLifecycleOwner(), properties -> {
            this.savedProperties = properties;
            if (properties == null || properties.isEmpty()) {
                binding.rvSavedProperties.setVisibility(View.GONE);
                binding.layoutSavedMap.setVisibility(View.GONE);
                binding.layoutEmptyFavorites.setVisibility(View.VISIBLE);
            } else {
                binding.layoutEmptyFavorites.setVisibility(View.GONE);
                if (isMapViewActive) {
                    binding.rvSavedProperties.setVisibility(View.GONE);
                    binding.layoutSavedMap.setVisibility(View.VISIBLE);
                } else {
                    binding.rvSavedProperties.setVisibility(View.VISIBLE);
                    binding.layoutSavedMap.setVisibility(View.GONE);
                }
                adapter.submitList(properties);
            }
            updateMapMarkers();
        });

        viewModel.getFavoriteIds().observe(getViewLifecycleOwner(), favoriteIds -> {
            adapter.setFavoriteIds(favoriteIds);
        });
    }

    private void setupMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapFragmentSaved);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        binding.btnSavedMapToggle.setOnClickListener(v -> {
            isMapViewActive = !isMapViewActive;
            if (isMapViewActive) {
                binding.rvSavedProperties.setVisibility(View.GONE);
                binding.layoutSavedMap.setVisibility(View.VISIBLE);
                binding.btnSavedMapToggle.setImageResource(R.drawable.ic_menu);
                updateMapMarkers();
            } else {
                binding.layoutSavedMap.setVisibility(View.GONE);
                binding.rvSavedProperties.setVisibility(
                        savedProperties != null && !savedProperties.isEmpty() ? View.VISIBLE : View.GONE
                );
                binding.btnSavedMapToggle.setImageResource(R.drawable.ic_location);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        this.googleMap = map;
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);

        this.googleMap.setOnInfoWindowClickListener(marker -> {
            Object tag = marker.getTag();
            if (tag instanceof Integer) {
                int propertyId = (Integer) tag;
                Intent intent = new Intent(requireContext(), PropertyDetailsActivity.class);
                intent.putExtra(PropertyDetailsActivity.EXTRA_PROPERTY_ID, propertyId);
                startActivity(intent);
            }
        });

        updateMapMarkers();
    }

    private void updateMapMarkers() {
        if (googleMap == null || savedProperties == null || savedProperties.isEmpty()) return;

        googleMap.clear();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        int validMarkerCount = 0;

        for (PropertyEntity property : savedProperties) {
            double lat = property.getLatitude();
            double lng = property.getLongitude();
            if (lat != 0.0 || lng != 0.0) {
                LatLng position = new LatLng(lat, lng);
                Marker marker = googleMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(property.getTitle())
                        .snippet(CurrencyFormatter.formatRent(property.getMonthlyRent()) + " • " + property.getLocality())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                );
                if (marker != null) {
                    marker.setTag(property.getId());
                }
                builder.include(position);
                validMarkerCount++;
            }
        }

        if (validMarkerCount > 0) {
            try {
                LatLngBounds bounds = builder.build();
                if (validMarkerCount == 1) {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), 14f));
                } else {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 120));
                }
            } catch (Exception ignored) {}
        }
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
