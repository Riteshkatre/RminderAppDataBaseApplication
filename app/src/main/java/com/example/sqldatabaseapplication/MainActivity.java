package com.example.sqldatabaseapplication;
import static android.app.PendingIntent.getActivity;

import static androidx.core.content.ContentProviderCompat.requireContext;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import com.example.sqldatabaseapplication.Fragment.CompliteFragment;
import com.example.sqldatabaseapplication.Fragment.UpcomingFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;


public class MainActivity extends AppCompatActivity {

    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;
    ImageView imgLock;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    UpcomingFragment upcomingFragment;
    CompliteFragment completedFragment;
    SwitchCompat switchLock;
    RelativeLayout lyt;
    private SharedPreferences sharedPreferences;
    private static final String SWITCH_STATE_KEY = "biometricSwitchState";
    private boolean isBiometricEnabled = false;
    private boolean userLoggedIn = false;
    Intent i;
    String from, newDate, newTime, newDesc;

    MyDataBaseHelper myDataBaseHelper;
    private ArrayList<MyDataModel> dataList = new ArrayList<>();
    int position = 0;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        i = getIntent();

        from = i.getStringExtra("from");

        if (from != null && from.equals("notification")) {
            newDesc = i.getStringExtra("description");
            newDate = i.getStringExtra("date");
            newTime = i.getStringExtra("time");
            showNotificationDialog(newDesc, newDate, newTime);
        }


        tabLayout = findViewById(R.id.tablay);
        viewPager2 = findViewById(R.id.viewpage2);
        switchLock = findViewById(R.id.switchcompact);
        lyt = findViewById(R.id.rl);
        imgLock = findViewById(R.id.imgOpen);

        upcomingFragment = new UpcomingFragment();
        completedFragment = new CompliteFragment();

        viewPager2.setAdapter(new ViewPagerAdapter(MainActivity.this, upcomingFragment, completedFragment));
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0) {
                    tab.setText("Upcoming");
                } else if (position == 1) {
                    tab.setText("Completed");
                }
            }
        }).attach();

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        isBiometricEnabled = sharedPreferences.getBoolean(SWITCH_STATE_KEY, false);
        switchLock.setChecked(isBiometricEnabled);

        switchLock.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isBiometricEnabled = isChecked;
            sharedPreferences.edit().putBoolean(SWITCH_STATE_KEY, isBiometricEnabled).apply();

            if (isChecked) {
                imgLock.setImageResource(R.drawable.off);
                enableBiometricAuthentication();
            } else {
                imgLock.setImageResource(R.drawable.on);
                disableBiometricAuthentication();
            }
        });

        if (isBiometricEnabled) {
            lyt.setVisibility(View.GONE);
            enableBiometricAuthentication();
        }
    }

    private void disableBiometricAuthentication() {
        Toast.makeText(this, "App Lock Deactivated", Toast.LENGTH_SHORT).show();
    }

    private void enableBiometricAuthentication() {
        BiometricManager biometricManager = BiometricManager.from(this);
        if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
            Executor executor = ContextCompat.getMainExecutor(this);
            biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Toast.makeText(MainActivity.this, "App Lock Activated", Toast.LENGTH_SHORT).show();
                    lyt.setVisibility(View.VISIBLE);
                    switchLock.setChecked(true);
                    userLoggedIn = true;
                    imgLock.setImageResource(R.drawable.off);
                }

                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    Toast.makeText(MainActivity.this, errString, Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Authentication");
                    builder.setMessage("Authentication is Required. \nDo you want to continue without Security?");
                    builder.setCancelable(false);

                    builder.setPositiveButton("Unlock", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            lyt.setVisibility(View.GONE);
                            imgLock.setImageResource(R.drawable.on);
                            dialogInterface.dismiss();
                            enableBiometricAuthentication();
                            lyt.setVisibility(View.VISIBLE);
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (!userLoggedIn) {
                                switchLock.setChecked(true);
                                finish();
                            } else {
                                switchLock.setChecked(false); // Keep the switch in the checked state
                                lyt.setVisibility(View.VISIBLE);
                                imgLock.setImageResource(R.drawable.on);
                                dialogInterface.dismiss();
                            }
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

            promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Unlock Lock Reminder")
                    .setDescription("Use PIN, PATTERN or PASSWORD to unlock")
                    .setDeviceCredentialAllowed(true)
                    .setNegativeButtonText(null)
                    .build();
            biometricPrompt.authenticate(promptInfo);
        } else {
            Toast.makeText(this, "Device Doesn't Support Biometric Authentication", Toast.LENGTH_SHORT).show();
            switchLock.setChecked(false);
            imgLock.setImageResource(R.drawable.on);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (biometricPrompt != null) {
            biometricPrompt.cancelAuthentication();
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
    private void showNotificationDialog(String description, String date, String time) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View notificationDialogView = getLayoutInflater().inflate(R.layout.notificationchannel, null);
        builder.setView(notificationDialogView);
        builder.setTitle("Notification Details");

        TextView tvDescription = notificationDialogView.findViewById(R.id.tvDescription);
        TextView tvDate = notificationDialogView.findViewById(R.id.tvDate);
        TextView tvTime = notificationDialogView.findViewById(R.id.tvTime);
        Button btnOK = notificationDialogView.findViewById(R.id.btnOK);
        Button btnCancel = notificationDialogView.findViewById(R.id.btnCancel);

        tvDescription.setText(description);
        tvDate.setText(date);
        tvTime.setText(time);
        AlertDialog dialog = builder.create();
        dialog.show();

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataList != null && position < dataList.size()) {
                    MyDataModel myDataModel = dataList.get(position);

                    completedFragment.addItemToCompletedList(myDataModel);

                    dataList.remove(position);

                    // Notify the upcoming fragment's adapter that the item has been removed
//                    upcomingFragment.notifyItemRemoved(position);
//                    upcomingFragment.notifyItemRangeChanged(position, dataList.size());

                    dialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}

