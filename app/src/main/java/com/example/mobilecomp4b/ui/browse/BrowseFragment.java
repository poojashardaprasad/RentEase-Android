package com.example.mobilecomp4b.ui.browse;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mobilecomp4b.R;
import com.example.mobilecomp4b.data.local.entity.PropertyEntity;
import com.example.mobilecomp4b.databinding.FragmentBrowseBinding;
import com.example.mobilecomp4b.ui.adapter.PropertyAdapter;
import com.example.mobilecomp4b.ui.details.PropertyDetailsActivity;
import com.example.mobilecomp4b.viewmodel.BrowseViewModel;

public class BrowseFragment extends Fragment implements PropertyAdapter.OnPropertyClickListener {

    private static final String ARG_TENANT_CATEGORY = "arg_tenant_category";

    private FragmentBrowseBinding binding;
    private BrowseViewModel viewModel;
    private PropertyAdapter adapter;

    public static BrowseFragment newInstance(String tenantCategory) {
        BrowseFragment fragment = new BrowseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TENANT_CATEGORY, tenantCategory);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBrowseBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(BrowseViewModel.class);

        setupSpinners();
        setupRecyclerView();
        setupSearchAndFilters();

        if (getArguments() != null) {
            String initialTenant = getArguments().getString(ARG_TENANT_CATEGORY, "Both");
            if ("Students".equalsIgnoreCase(initialTenant)) {
                binding.chipTenantStudents.setChecked(true);
                viewModel.updateTenantType("Students");
            } else if ("Families".equalsIgnoreCase(initialTenant)) {
                binding.chipTenantFamilies.setChecked(true);
                viewModel.updateTenantType("Families");
            } else {
                binding.chipTenantBoth.setChecked(true);
                viewModel.updateTenantType("Both");
            }
        }

        viewModel.getFilteredProperties().observe(getViewLifecycleOwner(), properties -> {
            if (properties == null || properties.isEmpty()) {
                binding.rvBrowseProperties.setVisibility(View.GONE);
                binding.layoutEmptyState.setVisibility(View.VISIBLE);
            } else {
                binding.rvBrowseProperties.setVisibility(View.VISIBLE);
                binding.layoutEmptyState.setVisibility(View.GONE);
                adapter.submitList(properties);
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new PropertyAdapter(this);
        binding.rvBrowseProperties.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvBrowseProperties.setAdapter(adapter);
    }

    private void setupSpinners() {
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                new String[]{"All Types", "PG", "Shared Room", "Private Room", "Studio", "Flat", "Independent House"}
        );
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerPropertyType.setAdapter(typeAdapter);

        ArrayAdapter<String> furnishingAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                new String[]{"All Furnishing", "Furnished", "Semi-furnished", "Unfurnished"}
        );
        furnishingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerFurnishing.setAdapter(furnishingAdapter);

        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                new String[]{"Default Sort", "Rent: Low to High", "Rent: High to Low"}
        );
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerSortBy.setAdapter(sortAdapter);
    }

    private void setupSearchAndFilters() {
        binding.etSearchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.updateSearchQuery(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.btnFilterToggle.setOnClickListener(v -> {
            if (binding.layoutFilterPanel.getVisibility() == View.VISIBLE) {
                binding.layoutFilterPanel.setVisibility(View.GONE);
            } else {
                binding.layoutFilterPanel.setVisibility(View.VISIBLE);
            }
        });

        binding.chipGroupTenant.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.contains(R.id.chipTenantStudents)) {
                viewModel.updateTenantType("Students");
            } else if (checkedIds.contains(R.id.chipTenantFamilies)) {
                viewModel.updateTenantType("Families");
            } else {
                viewModel.updateTenantType("Both");
            }
        });

        binding.btnApplyFilters.setOnClickListener(v -> {
            String minRentStr = binding.etMinRent.getText() != null ? binding.etMinRent.getText().toString().trim() : "";
            String maxRentStr = binding.etMaxRent.getText() != null ? binding.etMaxRent.getText().toString().trim() : "";

            Long minRent = minRentStr.isEmpty() ? null : Long.parseLong(minRentStr);
            Long maxRent = maxRentStr.isEmpty() ? null : Long.parseLong(maxRentStr);

            String selectedType = binding.spinnerPropertyType.getSelectedItem().toString();
            if ("All Types".equalsIgnoreCase(selectedType)) selectedType = "All";

            String selectedFurnishing = binding.spinnerFurnishing.getSelectedItem().toString();
            if ("All Furnishing".equalsIgnoreCase(selectedFurnishing)) selectedFurnishing = "All";

            int sortPos = binding.spinnerSortBy.getSelectedItemPosition();
            String sortBy = "DEFAULT";
            if (sortPos == 1) sortBy = "RENT_ASC";
            else if (sortPos == 2) sortBy = "RENT_DESC";

            viewModel.applyFilters(minRent, maxRent, selectedType, null, selectedFurnishing, sortBy);
            binding.layoutFilterPanel.setVisibility(View.GONE);
        });

        binding.btnClearFilters.setOnClickListener(v -> {
            binding.etSearchQuery.setText("");
            binding.etMinRent.setText("");
            binding.etMaxRent.setText("");
            binding.spinnerPropertyType.setSelection(0);
            binding.spinnerFurnishing.setSelection(0);
            binding.spinnerSortBy.setSelection(0);
            binding.chipTenantBoth.setChecked(true);
            viewModel.clearFilters();
            binding.layoutFilterPanel.setVisibility(View.GONE);
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
