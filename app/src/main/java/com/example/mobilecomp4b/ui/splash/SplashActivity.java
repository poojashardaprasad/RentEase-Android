package com.example.mobilecomp4b.ui.splash;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.example.mobilecomp4b.databinding.ActivitySplashBinding;
import com.example.mobilecomp4b.ui.main.MainActivity;
import com.example.mobilecomp4b.utils.UserSessionManager;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY_MS = 1800;
    private ActivitySplashBinding binding;
    private UserSessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);

        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new UserSessionManager(this);

        // Animate Logo & Text
        startLogoAnimation();

        // Delayed navigation based on persistent session
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent;
            if (sessionManager.isLoggedIn()) {
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, RoleSelectionActivity.class);
            }
            startActivity(intent);
            finish();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                overrideActivityTransition(OVERRIDE_TRANSITION_OPEN, android.R.anim.fade_in, android.R.anim.fade_out);
            } else {
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }, SPLASH_DELAY_MS);
    }

    private void startLogoAnimation() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.85f, 1.0f,
                0.85f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setDuration(1000);
        scaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());

        binding.imgLogo.startAnimation(scaleAnimation);
        binding.txtAppName.setAlpha(0f);
        binding.txtTagline.setAlpha(0f);

        binding.txtAppName.animate().alpha(1f).setDuration(800).setStartDelay(200).start();
        binding.txtTagline.animate().alpha(1f).setDuration(800).setStartDelay(400).start();
    }
}
