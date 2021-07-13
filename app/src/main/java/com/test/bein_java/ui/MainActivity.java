package com.test.bein_java.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.test.bein_java.databinding.ActivityMainBinding;
import com.test.bein_java.ui.fragments.TrashFragment;
import com.test.bein_java.utils.ActivityUtils;
import com.test.bein_java.R;
import com.test.bein_java.utils.ViewModelFactory;
import com.test.bein_java.ui.fragments.NewsFeedFragment;
import com.test.bein_java.view_mode.NewsFeedViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private final static int RC_CAMERA_AND_LOCATION = 0x25;
    private final static int NEWS_FEED = 0;
    private final static int TRASH = 1;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        methodRequiresTwoPermission();


        ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(), NewsFeedFragment.newInstance(), R.id.frame_layout);


        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == NEWS_FEED) {
                    ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(), NewsFeedFragment.newInstance(), R.id.frame_layout);

                } else {
                    ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(), TrashFragment.newInstance(), R.id.frame_layout);

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

    }

    @AfterPermissionGranted(RC_CAMERA_AND_LOCATION)
    private void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.camera_and_storage_rationale),
                    RC_CAMERA_AND_LOCATION, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull @NotNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull @NotNull List<String> perms) {

    }
}