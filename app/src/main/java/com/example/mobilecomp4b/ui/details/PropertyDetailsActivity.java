package com.example.mobilecomp4b.ui.details;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobilecomp4b.R;
import com.example.mobilecomp4b.data.local.entity.PropertyEntity;
import com.example.mobilecomp4b.databinding.ActivityPropertyDetailsBinding;
import com.example.mobilecomp4b.ui.requests.InquiryActivity;
import com.example.mobilecomp4b.ui.requests.RequestVisitActivity;
import com.example.mobilecomp4b.utils.CurrencyFormatter;
import com.example.mobilecomp4b.viewmodel.PropertyDetailsViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PropertyDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String EXTRA_PROPERTY_ID = "extra_property_id";

    private ActivityPropertyDetailsBinding binding;
    private PropertyDetailsViewModel viewModel;
    private int propertyId;
    private GoogleMap googleMap;
    private PropertyEntity currentProperty;

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

        // Setup SupportMapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragmentDetails);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        viewModel.getProperty().observe(this, property -> {
            if (property != null) {
                currentProperty = property;
                populatePropertyDetails(property);
                updateMapMarker();
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

        com.example.mobilecomp4b.utils.UserSessionManager sessionManager = new com.example.mobilecomp4b.utils.UserSessionManager(this);
        if (sessionManager.isOwner()) {
            binding.btnDetailsDelete.setVisibility(View.VISIBLE);
            binding.btnDetailsDelete.setOnClickListener(v -> confirmDeleteProperty());
        } else {
            binding.btnDetailsDelete.setVisibility(View.GONE);
        }

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

        binding.btnOpenDirections.setOnClickListener(v -> {
            if (currentProperty != null && currentProperty.getLatitude() != 0) {
                Uri gmmIntentUri = Uri.parse("geo:" + currentProperty.getLatitude() + "," +
                        currentProperty.getLongitude() + "?q=" +
                        Uri.encode(currentProperty.getTitle() + ", " + currentProperty.getLocality()));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.google.com/maps/search/?api=1&query=" +
                                    currentProperty.getLatitude() + "," + currentProperty.getLongitude()));
                    startActivity(browserIntent);
                }
            }
        });
    }

    private void confirmDeleteProperty() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Delete Property Listing")
                .setMessage("Are you sure you want to permanently delete this property listing?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    viewModel.deleteProperty(() -> {
                        Toast.makeText(PropertyDetailsActivity.this, "Property Deleted Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        this.googleMap = map;
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);
        this.googleMap.getUiSettings().setMapToolbarEnabled(true);
        this.googleMap.getUiSettings().setAllGesturesEnabled(true);

        View transparentOverlay = findViewById(R.id.mapFragmentDetails);
        if (transparentOverlay != null) {
            transparentOverlay.setOnTouchListener((v, event) -> {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            });
        }

        updateMapMarker();
    }

    private void updateMapMarker() {
        if (googleMap == null || currentProperty == null) return;

        double lat = currentProperty.getLatitude();
        double lng = currentProperty.getLongitude();
        if (lat == 0.0 && lng == 0.0) return;

        LatLng position = new LatLng(lat, lng);
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions()
                .position(position)
                .title(currentProperty.getTitle())
                .snippet(CurrencyFormatter.formatRent(currentProperty.getMonthlyRent()) + " • " +
                        currentProperty.getLocality() + ", " + currentProperty.getCity())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        );

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15f));
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

        com.example.mobilecomp4b.utils.ImageLoaderHelper.loadPropertyImage(binding.ivDetailsImage, property.getImageName());
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
