package com.example.mobilecomp4b.ui.requests;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobilecomp4b.RentEaseApplication;
import com.example.mobilecomp4b.databinding.ActivityRequestVisitBinding;
import com.example.mobilecomp4b.utils.CurrencyFormatter;
import com.example.mobilecomp4b.utils.DateUtils;
import com.example.mobilecomp4b.viewmodel.VisitRequestViewModel;

import java.util.Calendar;
import java.util.Locale;

public class RequestVisitActivity extends AppCompatActivity {

    public static final String EXTRA_PROPERTY_ID = "extra_property_id";

    private ActivityRequestVisitBinding binding;
    private VisitRequestViewModel viewModel;
    private int propertyId;
    private int selectedYear, selectedMonth, selectedDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRequestVisitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        propertyId = getIntent().getIntExtra(EXTRA_PROPERTY_ID, -1);
        if (propertyId == -1) {
            Toast.makeText(this, "Property not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this).get(VisitRequestViewModel.class);

        binding.btnVisitBack.setOnClickListener(v -> finish());

        // Pre-fill user profile info if available
        RentEaseApplication app = (RentEaseApplication) getApplication();
        app.getProfileRepository().getUserProfile().observe(this, profile -> {
            if (profile != null) {
                if (binding.etVisitName.getText() == null || binding.etVisitName.getText().toString().isEmpty()) {
                    binding.etVisitName.setText(profile.getName());
                }
                if (binding.etVisitPhone.getText() == null || binding.etVisitPhone.getText().toString().isEmpty()) {
                    binding.etVisitPhone.setText(profile.getPhone());
                }
            }
        });

        // Load property header info
        app.getPropertyRepository().getPropertyById(propertyId).observe(this, property -> {
            if (property != null) {
                binding.tvVisitPropertyTitle.setText(property.getTitle());
                binding.tvVisitPropertyRent.setText(CurrencyFormatter.formatRent(property.getMonthlyRent()) + " · " + property.getLocality() + ", " + property.getCity());
            }
        });

        binding.etVisitDate.setOnClickListener(v -> showDatePicker());
        binding.etVisitTime.setOnClickListener(v -> showTimePicker());

        binding.btnSubmitVisit.setOnClickListener(v -> submitVisitRequest());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, (view, y, m, d) -> {
            if (DateUtils.isPastDate(y, m, d)) {
                Toast.makeText(this, "Please select a present or future date", Toast.LENGTH_SHORT).show();
                return;
            }
            selectedYear = y;
            selectedMonth = m;
            selectedDay = d;
            String dateFormatted = String.format(Locale.US, "%04d-%02d-%02d", y, m + 1, d);
            binding.etVisitDate.setText(dateFormatted);
        }, year, month, day);

        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog dialog = new TimePickerDialog(this, (view, hourOfDay, minuteOfHour) -> {
            String timeFormatted = String.format(Locale.US, "%02d:%02d", hourOfDay, minuteOfHour);
            binding.etVisitTime.setText(timeFormatted);
        }, hour, minute, false);

        dialog.show();
    }

    private void submitVisitRequest() {
        String name = binding.etVisitName.getText() != null ? binding.etVisitName.getText().toString().trim() : "";
        String phone = binding.etVisitPhone.getText() != null ? binding.etVisitPhone.getText().toString().trim() : "";
        String date = binding.etVisitDate.getText() != null ? binding.etVisitDate.getText().toString().trim() : "";
        String time = binding.etVisitTime.getText() != null ? binding.etVisitTime.getText().toString().trim() : "";
        String message = binding.etVisitMessage.getText() != null ? binding.etVisitMessage.getText().toString().trim() : "";

        if (name.isEmpty()) {
            binding.etVisitName.setError("Name is required");
            return;
        }

        if (phone.isEmpty() || phone.length() < 10) {
            binding.etVisitPhone.setError("Valid 10-digit phone number required");
            return;
        }

        if (date.isEmpty()) {
            Toast.makeText(this, "Please select a visit date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (time.isEmpty()) {
            Toast.makeText(this, "Please select a visit time", Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.requestVisit(propertyId, name, phone, date, time, message, () -> {
            Toast.makeText(this, "Visit Request Saved Locally!", Toast.LENGTH_LONG).show();
            finish();
        });
    }
}
