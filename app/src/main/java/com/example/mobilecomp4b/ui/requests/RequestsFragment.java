package com.example.mobilecomp4b.ui.requests;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mobilecomp4b.databinding.FragmentRequestsBinding;
import com.example.mobilecomp4b.ui.adapter.InquiryAdapter;
import com.example.mobilecomp4b.ui.adapter.VisitRequestAdapter;
import com.example.mobilecomp4b.ui.details.PropertyDetailsActivity;
import com.example.mobilecomp4b.viewmodel.InquiryViewModel;
import com.example.mobilecomp4b.viewmodel.VisitRequestViewModel;
import com.google.android.material.tabs.TabLayout;

public class RequestsFragment extends Fragment {

    private FragmentRequestsBinding binding;
    private VisitRequestViewModel visitViewModel;
    private InquiryViewModel inquiryViewModel;
    private VisitRequestAdapter visitAdapter;
    private InquiryAdapter inquiryAdapter;
    private int currentTab = 0; // 0: Visit Requests, 1: Inquiries

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRequestsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        visitViewModel = new ViewModelProvider(this).get(VisitRequestViewModel.class);
        inquiryViewModel = new ViewModelProvider(this).get(InquiryViewModel.class);

        binding.rvRequestsList.setLayoutManager(new LinearLayoutManager(requireContext()));

        setupAdapters();
        setupTabs();
        observeData();
    }

    private void setupAdapters() {
        visitAdapter = new VisitRequestAdapter(new VisitRequestAdapter.OnVisitRequestListener() {
            @Override
            public void onViewProperty(int propertyId) {
                openPropertyDetails(propertyId);
            }

            @Override
            public void onCancelRequest(int requestId) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Cancel Visit Request")
                        .setMessage("Are you sure you want to cancel this visit request?")
                        .setPositiveButton("Yes, Cancel", (dialog, which) -> visitViewModel.cancelVisitRequest(requestId))
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        inquiryAdapter = new InquiryAdapter(propertyId -> openPropertyDetails(propertyId));
    }

    private void setupTabs() {
        binding.tabLayoutRequests.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTab = tab.getPosition();
                updateListForTab();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void observeData() {
        visitViewModel.getAllVisitRequests().observe(getViewLifecycleOwner(), requests -> {
            if (currentTab == 0) {
                if (requests == null || requests.isEmpty()) {
                    binding.rvRequestsList.setVisibility(View.GONE);
                    binding.layoutEmptyRequests.setVisibility(View.VISIBLE);
                } else {
                    binding.rvRequestsList.setVisibility(View.VISIBLE);
                    binding.layoutEmptyRequests.setVisibility(View.GONE);
                    binding.rvRequestsList.setAdapter(visitAdapter);
                    visitAdapter.submitList(requests);
                }
            }
        });

        inquiryViewModel.getAllInquiries().observe(getViewLifecycleOwner(), inquiries -> {
            if (currentTab == 1) {
                if (inquiries == null || inquiries.isEmpty()) {
                    binding.rvRequestsList.setVisibility(View.GONE);
                    binding.layoutEmptyRequests.setVisibility(View.VISIBLE);
                } else {
                    binding.rvRequestsList.setVisibility(View.VISIBLE);
                    binding.layoutEmptyRequests.setVisibility(View.GONE);
                    binding.rvRequestsList.setAdapter(inquiryAdapter);
                    inquiryAdapter.submitList(inquiries);
                }
            }
        });
    }

    private void updateListForTab() {
        if (currentTab == 0) {
            binding.rvRequestsList.setAdapter(visitAdapter);
            visitViewModel.getAllVisitRequests().observe(getViewLifecycleOwner(), requests -> {
                if (requests == null || requests.isEmpty()) {
                    binding.rvRequestsList.setVisibility(View.GONE);
                    binding.layoutEmptyRequests.setVisibility(View.VISIBLE);
                } else {
                    binding.rvRequestsList.setVisibility(View.VISIBLE);
                    binding.layoutEmptyRequests.setVisibility(View.GONE);
                    visitAdapter.submitList(requests);
                }
            });
        } else {
            binding.rvRequestsList.setAdapter(inquiryAdapter);
            inquiryViewModel.getAllInquiries().observe(getViewLifecycleOwner(), inquiries -> {
                if (inquiries == null || inquiries.isEmpty()) {
                    binding.rvRequestsList.setVisibility(View.GONE);
                    binding.layoutEmptyRequests.setVisibility(View.VISIBLE);
                } else {
                    binding.rvRequestsList.setVisibility(View.VISIBLE);
                    binding.layoutEmptyRequests.setVisibility(View.GONE);
                    inquiryAdapter.submitList(inquiries);
                }
            });
        }
    }

    private void openPropertyDetails(int propertyId) {
        Intent intent = new Intent(requireContext(), PropertyDetailsActivity.class);
        intent.putExtra(PropertyDetailsActivity.EXTRA_PROPERTY_ID, propertyId);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
