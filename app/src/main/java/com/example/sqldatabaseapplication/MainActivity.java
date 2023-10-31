package com.example.sqldatabaseapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.sqldatabaseapplication.Fragment.CompliteFragment;
import com.example.sqldatabaseapplication.Fragment.UpcomingFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private SwitchCompat switchCompact;
    private RelativeLayout rl;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ImageView imgOpen;
    private SharedPreferences sharedPreferences;
    private static final String SWITCH_STATE_KEY = "biometricSwitchState";
    private boolean isBiometricEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rl = findViewById(R.id.rl);
        switchCompact = findViewById(R.id.switchcompact);
        tabLayout = findViewById(R.id.tablay);
        viewPager2 = findViewById(R.id.viewpage2);
        imgOpen = findViewById(R.id.imgOpen);

        UpcomingFragment upcomingFragment = new UpcomingFragment();
        CompliteFragment compliteFragment = new CompliteFragment();

        viewPager2.setAdapter(new ViewPagerAdapter(MainActivity.this, upcomingFragment, compliteFragment));
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0)
                    tab.setText("Upcoming");
                else
                    tab.setText("Complete");
            }
        }).attach();

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        isBiometricEnabled = sharedPreferences.getBoolean(SWITCH_STATE_KEY, false);
        switchCompact.setChecked(isBiometricEnabled);

        switchCompact.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isBiometricEnabled = isChecked;
            sharedPreferences.edit().putBoolean(SWITCH_STATE_KEY, isBiometricEnabled).apply();

            updateLockImage(isBiometricEnabled);  // Update the lock image

            if (isChecked) {
                enableBiometricAuthentication();
            } else {
                disableBiometricAuthentication();
            }
        });

        if (isBiometricEnabled) {
            enableBiometricAuthentication();
        }

        updateLockImage(isBiometricEnabled);  // Update the lock image when the app starts
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (biometricPrompt != null) {
            biometricPrompt.cancelAuthentication();
        }
    }

    private void enableBiometricAuthentication() {
        BiometricManager biometricManager = BiometricManager.from(this);
        if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
            Executor executor = ContextCompat.getMainExecutor(this);
            biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Toast.makeText(MainActivity.this, "Biometric authentication enabled", Toast.LENGTH_SHORT).show();
                    rl.setVisibility(View.VISIBLE);
                }
            });

            promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Ritesh project")
                    .setDescription("Use Fingerprint To Log In")
                    .setDeviceCredentialAllowed(true)
                    .build();
            biometricPrompt.authenticate(promptInfo);
        } else {
            Toast.makeText(this, "Device doesn't support biometric authentication", Toast.LENGTH_SHORT).show();
        }
    }

    private void disableBiometricAuthentication() {
        Toast.makeText(this, "Biometric authentication disabled", Toast.LENGTH_SHORT).show();
    }

    private void updateLockImage(boolean isBiometricEnabled) {
        if (isBiometricEnabled) {
            imgOpen.setImageResource(R.drawable.baseline_lock_24);
        } else {
            imgOpen.setImageResource(R.drawable.baseline_lock_open_24);
        }
    }

    public class ViewPagerAdapter extends FragmentStateAdapter {
        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, UpcomingFragment upcomingFragment, CompliteFragment compliteFragment) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return new UpcomingFragment();
            } else {
                return new CompliteFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}
