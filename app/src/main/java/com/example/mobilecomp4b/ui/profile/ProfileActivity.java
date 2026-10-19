package com.example.mobilecomp4b.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobilecomp4b.databinding.ActivityProfileBinding;
import com.example.mobilecomp4b.ui.splash.RoleSelectionActivity;
import com.example.mobilecomp4b.utils.UserSessionManager;
import com.example.mobilecomp4b.viewmodel.ProfileViewModel;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private ProfileViewModel viewModel;
    private UserSessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new UserSessionManager(this);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding.btnProfileBack.setOnClickListener(v -> finish());

        setupSpinner();
        observeViewModel();
        setupRoleAndLogout();

        binding.btnSaveProfile.setOnClickListener(v -> saveProfile());
        binding.btnResetProfile.setOnClickListener(v -> confirmResetProfile());
    }

    private void setupRoleAndLogout() {
        if (sessionManager.isOwner()) {
            binding.tvCurrentRoleDisplay.setText("Current Mode: Property Owner / Renter");
        } else {
            binding.tvCurrentRoleDisplay.setText("Current Mode: Tenant / Rentee");
        }

        binding.btnLogout.setOnClickListener(v -> confirmLogout());
    }

    private void confirmLogout() {
        new AlertDialog.Builder(this)
                .setTitle("Log Out")
                .setMessage("Are you sure you want to log out and switch your active role?")
                .setPositiveButton("Log Out", (dialog, which) -> performLogout())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void performLogout() {
        sessionManager.logout();
        Toast.makeText(this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ProfileActivity.this, RoleSelectionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"Both", "Students", "Families"}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerProfileTenant.setAdapter(adapter);
    }

    private void observeViewModel() {
        viewModel.getUserProfile().observe(this, profile -> {
            if (profile != null) {
                binding.tvProfileDisplayHeader.setText(profile.getName());
                binding.etProfileName.setText(profile.getName());
                binding.etProfileEmail.setText(profile.getEmail());
                binding.etProfilePhone.setText(profile.getPhone());

                String type = profile.getPreferredTenantType();
                if ("Students".equalsIgnoreCase(type)) {
                    binding.spinnerProfileTenant.setSelection(1);
                } else if ("Families".equalsIgnoreCase(type)) {
                    binding.spinnerProfileTenant.setSelection(2);
                } else {
                    binding.spinnerProfileTenant.setSelection(0);
                }
            }
        });

        viewModel.getFavoriteCount().observe(this, count -> {
            binding.tvSavedCount.setText(String.valueOf(count != null ? count : 0));
        });

        viewModel.getPendingRequestsCount().observe(this, count -> {
            binding.tvRequestsCount.setText(String.valueOf(count != null ? count : 0));
        });
    }

    private void saveProfile() {
        String name = binding.etProfileName.getText() != null ? binding.etProfileName.getText().toString().trim() : "";
        String email = binding.etProfileEmail.getText() != null ? binding.etProfileEmail.getText().toString().trim() : "";
        String phone = binding.etProfilePhone.getText() != null ? binding.etProfilePhone.getText().toString().trim() : "";
        String tenantType = binding.spinnerProfileTenant.getSelectedItem().toString();

        if (name.isEmpty()) {
            binding.etProfileName.setError("Name required");
            return;
        }

        if (phone.isEmpty()) {
            binding.etProfilePhone.setError("Phone required");
            return;
        }

        viewModel.saveProfile(name, email, phone, tenantType, () -> {
            Toast.makeText(ProfileActivity.this, "Profile Saved Successfully!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void confirmResetProfile() {
        new AlertDialog.Builder(this)
                .setTitle("Reset Profile")
                .setMessage("Are you sure you want to reset your profile details to defaults?")
                .setPositiveButton("Reset", (dialog, which) -> {
                    viewModel.resetProfile(() -> {
                        Toast.makeText(ProfileActivity.this, "Profile Reset to Default", Toast.LENGTH_SHORT).show();
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
