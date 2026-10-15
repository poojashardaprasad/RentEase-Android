package com.example.mobilecomp4b.ui.details;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobilecomp4b.R;
import com.example.mobilecomp4b.data.local.entity.PropertyEntity;
import com.example.mobilecomp4b.databinding.ActivityPropertyDetailsBinding;
import com.example.mobilecomp4b.ui.requests.InquiryActivity;
import com.example.mobilecomp4b.ui.requests.RequestVisitActivity;
import com.example.mobilecomp4b.utils.CurrencyFormatter;
import com.example.mobilecomp4b.viewmodel.PropertyDetailsViewModel;

public class PropertyDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_PROPERTY_ID = "extra_property_id";

    private ActivityPropertyDetailsBinding binding;
    private PropertyDetailsViewModel viewModel;
    private int propertyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPropertyDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        propertyId = getIntent().getIntExtra(EXTRA_PROPERTY_ID, -1);
        if (propertyId == -1) {
            Toast.makeText(this, "Property not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this).get(PropertyDetailsViewModel.class);
        viewModel.setPropertyId(propertyId);

        binding.btnDetailsBack.setOnClickListener(v -> finish());

        viewModel.getProperty().observe(this, property -> {
            if (property != null) {
                populatePropertyDetails(property);
            }
        });

        viewModel.isFavorite().observe(this, isFavorite -> {
            if (Boolean.TRUE.equals(isFavorite)) {
                binding.btnDetailsFavorite.setImageResource(R.drawable.ic_favorite);
            } else {
                binding.btnDetailsFavorite.setImageResource(R.drawable.ic_favorite_border);
            }
        });

        binding.btnDetailsFavorite.setOnClickListener(v -> viewModel.toggleFavorite());

        binding.btnRequestVisit.setOnClickListener(v -> {
            Intent intent = new Intent(PropertyDetailsActivity.this, RequestVisitActivity.class);
            intent.putExtra(RequestVisitActivity.EXTRA_PROPERTY_ID, propertyId);
            startActivity(intent);
        });

        binding.btnInquire.setOnClickListener(v -> {
            Intent intent = new Intent(PropertyDetailsActivity.this, InquiryActivity.class);
            intent.putExtra(InquiryActivity.EXTRA_PROPERTY_ID, propertyId);
            startActivity(intent);
        });
    }

    private void populatePropertyDetails(PropertyEntity property) {
        binding.tvDetailsTitle.setText(property.getTitle());
        binding.tvDetailsRent.setText(CurrencyFormatter.formatRent(property.getMonthlyRent()));
        binding.tvDetailsDeposit.setText(CurrencyFormatter.formatDeposit(property.getDeposit()));
        binding.tvDetailsLocation.setText(property.getLocality() + ", " + property.getCity());
        binding.tvDetailsType.setText(property.getPropertyType());
        binding.tvDetailsBedrooms.setText(property.getBedrooms() + " BHK");
        binding.tvDetailsFurnishing.setText(property.getFurnishing());
        binding.tvDetailsTenantBadge.setText("Suitable for " + property.getTenantType());
        binding.tvDetailsAvailability.setText(property.isAvailable() ? "Available Now" : "Currently Occupied");
        binding.tvDetailsAmenities.setText(property.getAmenities());
        binding.tvDetailsDescription.setText(property.getDescription());

        int resId = getDrawableResId(property.getImageName());
        if (resId != 0) {
            binding.ivDetailsImage.setImageResource(resId);
        } else {
            binding.ivDetailsImage.setImageResource(R.drawable.img_default_property);
        }
    }

    private int getDrawableResId(String name) {
        if (name == null || name.isEmpty()) return 0;
        try {
            Resources resources = getResources();
            return resources.getIdentifier(name, "drawable", getPackageName());
        } catch (Exception e) {
            return 0;
        }
    }
}
