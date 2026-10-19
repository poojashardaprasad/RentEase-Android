package com.example.mobilecomp4b.ui.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilecomp4b.databinding.ActivityRoleSelectionBinding;
import com.example.mobilecomp4b.ui.main.MainActivity;
import com.example.mobilecomp4b.utils.UserSessionManager;

public class RoleSelectionActivity extends AppCompatActivity {

    private ActivityRoleSelectionBinding binding;
    private UserSessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoleSelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new UserSessionManager(this);

        binding.btnContinueTenant.setOnClickListener(v -> selectRole(UserSessionManager.ROLE_TENANT, "Tenant User"));
        binding.cardSelectTenant.setOnClickListener(v -> selectRole(UserSessionManager.ROLE_TENANT, "Tenant User"));

        binding.btnContinueOwner.setOnClickListener(v -> selectRole(UserSessionManager.ROLE_OWNER, "Property Owner"));
        binding.cardSelectOwner.setOnClickListener(v -> selectRole(UserSessionManager.ROLE_OWNER, "Property Owner"));
    }

    private void selectRole(String role, String defaultName) {
        sessionManager.setSession(role, defaultName);
        Intent intent = new Intent(RoleSelectionActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
