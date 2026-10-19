package com.example.mobilecomp4b.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mobilecomp4b.R;
import com.example.mobilecomp4b.databinding.ActivityMainBinding;
import com.example.mobilecomp4b.ui.browse.BrowseFragment;
import com.example.mobilecomp4b.ui.details.AddPropertyActivity;
import com.example.mobilecomp4b.ui.favorites.FavoritesFragment;
import com.example.mobilecomp4b.ui.home.HomeFragment;
import com.example.mobilecomp4b.ui.profile.ProfileActivity;
import com.example.mobilecomp4b.ui.requests.RequestsFragment;
import com.example.mobilecomp4b.utils.UserSessionManager;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private UserSessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new UserSessionManager(this);

        updateRoleUI();

        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment());
        }

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                replaceFragment(new HomeFragment());
                return true;
            } else if (itemId == R.id.nav_browse) {
                replaceFragment(new BrowseFragment());
                return true;
            } else if (itemId == R.id.nav_saved) {
                replaceFragment(new FavoritesFragment());
                return true;
            } else if (itemId == R.id.nav_requests) {
                replaceFragment(new RequestsFragment());
                return true;
            }
            return false;
        });

        binding.btnHeaderProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        binding.fabAddProperty.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddPropertyActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateRoleUI();
    }

    private void updateRoleUI() {
        if (sessionManager.isOwner()) {
            binding.tvHeaderRoleBadge.setText("Owner Mode");
            binding.fabAddProperty.setVisibility(View.VISIBLE);
        } else {
            binding.tvHeaderRoleBadge.setText("Tenant Mode");
            binding.fabAddProperty.setVisibility(View.GONE);
        }
    }

    public void navigateToBrowse(String category) {
        binding.bottomNavigation.setSelectedItemId(R.id.nav_browse);
        Fragment fragment = BrowseFragment.newInstance(category);
        replaceFragment(fragment);
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
