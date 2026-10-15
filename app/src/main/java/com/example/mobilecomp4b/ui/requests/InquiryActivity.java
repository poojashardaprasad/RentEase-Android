package com.example.mobilecomp4b.ui.requests;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobilecomp4b.RentEaseApplication;
import com.example.mobilecomp4b.databinding.ActivityInquiryBinding;
import com.example.mobilecomp4b.utils.CurrencyFormatter;
import com.example.mobilecomp4b.viewmodel.InquiryViewModel;

public class InquiryActivity extends AppCompatActivity {

    public static final String EXTRA_PROPERTY_ID = "extra_property_id";

    private ActivityInquiryBinding binding;
    private InquiryViewModel viewModel;
    private int propertyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInquiryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        propertyId = getIntent().getIntExtra(EXTRA_PROPERTY_ID, -1);
        if (propertyId == -1) {
            Toast.makeText(this, "Property not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this).get(InquiryViewModel.class);

        binding.btnInquiryBack.setOnClickListener(v -> finish());

        RentEaseApplication app = (RentEaseApplication) getApplication();
        app.getProfileRepository().getUserProfile().observe(this, profile -> {
            if (profile != null) {
                if (binding.etInquiryName.getText() == null || binding.etInquiryName.getText().toString().isEmpty()) {
                    binding.etInquiryName.setText(profile.getName());
                }
                if (binding.etInquiryPhone.getText() == null || binding.etInquiryPhone.getText().toString().isEmpty()) {
                    binding.etInquiryPhone.setText(profile.getPhone());
                }
                if (binding.etInquiryEmail.getText() == null || binding.etInquiryEmail.getText().toString().isEmpty()) {
                    binding.etInquiryEmail.setText(profile.getEmail());
                }
            }
        });

        app.getPropertyRepository().getPropertyById(propertyId).observe(this, property -> {
            if (property != null) {
                binding.tvInquiryPropertyTitle.setText(property.getTitle());
                binding.tvInquiryPropertyRent.setText(CurrencyFormatter.formatRent(property.getMonthlyRent()) + " · " + property.getLocality() + ", " + property.getCity());
            }
        });

        binding.btnSubmitInquiry.setOnClickListener(v -> submitInquiry());
    }

    private void submitInquiry() {
        String name = binding.etInquiryName.getText() != null ? binding.etInquiryName.getText().toString().trim() : "";
        String phone = binding.etInquiryPhone.getText() != null ? binding.etInquiryPhone.getText().toString().trim() : "";
        String email = binding.etInquiryEmail.getText() != null ? binding.etInquiryEmail.getText().toString().trim() : "";
        String message = binding.etInquiryMessage.getText() != null ? binding.etInquiryMessage.getText().toString().trim() : "";

        if (name.isEmpty()) {
            binding.etInquiryName.setError("Name is required");
            return;
        }

        if (phone.isEmpty() || phone.length() < 10) {
            binding.etInquiryPhone.setError("Valid phone number required");
            return;
        }

        if (message.isEmpty()) {
            binding.etInquiryMessage.setError("Inquiry message required");
            return;
        }

        viewModel.submitInquiry(propertyId, name, phone, email, message, () -> {
            Toast.makeText(this, "Inquiry Sent & Saved Locally!", Toast.LENGTH_LONG).show();
            finish();
        });
    }
}
