package com.example.mobilecomp4b.ui.details;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilecomp4b.RentEaseApplication;
import com.example.mobilecomp4b.data.local.entity.PropertyEntity;
import com.example.mobilecomp4b.databinding.ActivityAddPropertyBinding;
import com.example.mobilecomp4b.utils.AppExecutors;
import com.example.mobilecomp4b.utils.ImageLoaderHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class AddPropertyActivity extends AppCompatActivity {

    private ActivityAddPropertyBinding binding;
    private AppExecutors executors;
    private String selectedImageName = "img_property_1";

    private final ActivityResultLauncher<String> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    saveAndDisplayPickedImage(uri);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPropertyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RentEaseApplication app = (RentEaseApplication) getApplication();
        executors = app.getExecutors();

        binding.btnAddBack.setOnClickListener(v -> finish());

        setupSpinners();

        binding.btnSelectImage.setOnClickListener(v -> showImagePickerDialog());
        binding.ivSelectedPropertyImage.setOnClickListener(v -> showImagePickerDialog());

        binding.btnDefaultCoords.setOnClickListener(v -> autoFillCoordinates());
        binding.btnSubmitProperty.setOnClickListener(v -> submitProperty());
    }

    private void showImagePickerDialog() {
        String[] options = {
                "📱 Upload Photo from Device Gallery",
                "Preset: Modern Apartment (Photo 1)",
                "Preset: Furnished Studio (Photo 2)",
                "Preset: Cozy Home (Photo 3)",
                "Preset: Student Apartment (Photo 4)",
                "Preset: Luxury Villa / House (Photo 5)",
                "Preset: Private Suite / PG (Photo 6)",
                "Preset: High-Rise Flat (Photo 7)",
                "Preset: Executive Residence (Photo 8)"
        };

        new AlertDialog.Builder(this)
                .setTitle("Select Property Photo")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        galleryLauncher.launch("image/*");
                    } else {
                        selectedImageName = "img_property_" + which;
                        ImageLoaderHelper.loadPropertyImage(binding.ivSelectedPropertyImage, selectedImageName);
                        Toast.makeText(this, "Photo updated: " + options[which], Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void saveAndDisplayPickedImage(Uri uri) {
        executors.diskIO().execute(() -> {
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                if (inputStream != null) {
                    File appDir = getFilesDir();
                    File destFile = new File(appDir, "prop_upload_" + System.currentTimeMillis() + ".jpg");
                    FileOutputStream outputStream = new FileOutputStream(destFile);
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    outputStream.close();
                    inputStream.close();

                    selectedImageName = destFile.getAbsolutePath();

                    runOnUiThread(() -> {
                        ImageLoaderHelper.loadPropertyImage(binding.ivSelectedPropertyImage, selectedImageName);
                        Toast.makeText(this, "Photo uploaded from device!", Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Failed to load image from device", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void setupSpinners() {
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(
                this,
                com.example.mobilecomp4b.R.layout.item_spinner,
                new String[]{"Flat", "Studio", "Independent House", "PG", "Shared Room", "Private Room"}
        );
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerAddType.setAdapter(typeAdapter);

        ArrayAdapter<String> furnishingAdapter = new ArrayAdapter<>(
                this,
                com.example.mobilecomp4b.R.layout.item_spinner,
                new String[]{"Furnished", "Semi-furnished", "Unfurnished"}
        );
        furnishingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerAddFurnishing.setAdapter(furnishingAdapter);

        ArrayAdapter<String> tenantAdapter = new ArrayAdapter<>(
                this,
                com.example.mobilecomp4b.R.layout.item_spinner,
                new String[]{"Both", "Students", "Families"}
        );
        tenantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerAddTenantType.setAdapter(tenantAdapter);

        ArrayAdapter<String> bedroomAdapter = new ArrayAdapter<>(
                this,
                com.example.mobilecomp4b.R.layout.item_spinner,
                new String[]{"1 BHK", "2 BHK", "3 BHK", "4+ BHK"}
        );
        bedroomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerAddBedrooms.setAdapter(bedroomAdapter);
    }

    private void autoFillCoordinates() {
        String city = binding.etAddCity.getText() != null ? binding.etAddCity.getText().toString().trim() : "";
        if ("Bengaluru".equalsIgnoreCase(city) || "Bangalore".equalsIgnoreCase(city)) {
            binding.etAddLatitude.setText("12.9716");
            binding.etAddLongitude.setText("77.5946");
        } else if ("Delhi".equalsIgnoreCase(city) || "Noida".equalsIgnoreCase(city) || "Gurugram".equalsIgnoreCase(city)) {
            binding.etAddLatitude.setText("28.6139");
            binding.etAddLongitude.setText("77.2090");
        } else if ("Pune".equalsIgnoreCase(city)) {
            binding.etAddLatitude.setText("18.5204");
            binding.etAddLongitude.setText("73.8567");
        } else if ("Hyderabad".equalsIgnoreCase(city)) {
            binding.etAddLatitude.setText("17.3850");
            binding.etAddLongitude.setText("78.4867");
        } else {
            // Default Mumbai
            binding.etAddLatitude.setText("19.1363");
            binding.etAddLongitude.setText("72.8277");
        }
        Toast.makeText(this, "Coordinates auto-filled for " + (city.isEmpty() ? "Mumbai" : city), Toast.LENGTH_SHORT).show();
    }

    private void submitProperty() {
        String title = binding.etAddTitle.getText() != null ? binding.etAddTitle.getText().toString().trim() : "";
        String description = binding.etAddDescription.getText() != null ? binding.etAddDescription.getText().toString().trim() : "";
        String locality = binding.etAddLocality.getText() != null ? binding.etAddLocality.getText().toString().trim() : "";
        String city = binding.etAddCity.getText() != null ? binding.etAddCity.getText().toString().trim() : "";
        String rentStr = binding.etAddRent.getText() != null ? binding.etAddRent.getText().toString().trim() : "";
        String depositStr = binding.etAddDeposit.getText() != null ? binding.etAddDeposit.getText().toString().trim() : "";
        String amenities = binding.etAddAmenities.getText() != null ? binding.etAddAmenities.getText().toString().trim() : "";

        if (title.isEmpty()) {
            binding.etAddTitle.setError("Title required");
            return;
        }

        if (locality.isEmpty()) {
            binding.etAddLocality.setError("Locality required");
            return;
        }

        if (city.isEmpty()) {
            binding.etAddCity.setError("City required");
            return;
        }

        if (rentStr.isEmpty()) {
            binding.etAddRent.setError("Rent required");
            return;
        }

        long monthlyRent = Long.parseLong(rentStr);
        long deposit = depositStr.isEmpty() ? monthlyRent * 2 : Long.parseLong(depositStr);

        String propertyType = binding.spinnerAddType.getSelectedItem().toString();
        String furnishing = binding.spinnerAddFurnishing.getSelectedItem().toString();
        String tenantType = binding.spinnerAddTenantType.getSelectedItem().toString();
        int bedrooms = binding.spinnerAddBedrooms.getSelectedItemPosition() + 1;

        String latStr = binding.etAddLatitude.getText() != null ? binding.etAddLatitude.getText().toString().trim() : "";
        String lngStr = binding.etAddLongitude.getText() != null ? binding.etAddLongitude.getText().toString().trim() : "";

        double latitude = latStr.isEmpty() ? 19.1363 : Double.parseDouble(latStr);
        double longitude = lngStr.isEmpty() ? 72.8277 : Double.parseDouble(lngStr);

        if (description.isEmpty()) {
            description = "Well maintained " + bedrooms + " BHK " + propertyType + " located in " + locality + ", " + city + ". Excellent connectivity and peaceful environment.";
        }

        if (amenities.isEmpty()) {
            amenities = "WiFi, Covered Parking, Security, Elevator, Water Backup";
        }

        PropertyEntity property = new PropertyEntity(
                0, // Auto-generate ID
                title,
                description,
                locality,
                city,
                monthlyRent,
                deposit,
                propertyType,
                bedrooms,
                tenantType,
                furnishing,
                amenities,
                selectedImageName,
                true,
                latitude,
                longitude
        );

        RentEaseApplication app = (RentEaseApplication) getApplication();
        executors.diskIO().execute(() -> {
            app.getDatabase().propertyDao().insertProperty(property);
            runOnUiThread(() -> {
                Toast.makeText(AddPropertyActivity.this, "🎉 Property Posted Successfully!", Toast.LENGTH_LONG).show();
                finish();
            });
        });
    }
}
