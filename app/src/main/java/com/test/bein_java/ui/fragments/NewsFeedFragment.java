package com.test.bein_java.ui.fragments;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.test.bein_java.R;
import com.test.bein_java.data.News;
import com.test.bein_java.databinding.NewsFeedFragmentBinding;
import com.test.bein_java.dialog.BottomSheetDialog;
import com.test.bein_java.adapter.NewsAdapter;
import com.test.bein_java.utils.ViewModelFactory;
import com.test.bein_java.view_mode.NewsFeedViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.test.bein_java.adapter.NewsAdapter.SHOW_BOTTOM_SHEET;
import static com.test.bein_java.ui.MainActivity.RC_CAMERA_AND_LOCATION;
import static com.test.bein_java.ui.MainActivity.RC_STORAGE;


public class NewsFeedFragment extends Fragment implements BottomSheetDialog.BottomSheetListener, EasyPermissions.PermissionCallbacks {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_SHARE_IMAGE = 2;

    private NewsFeedViewModel mViewModel;
    private NewsAdapter newsAdapter;
    private NewsFeedFragmentBinding binding;


    private final ItemClicked itemClicked = (news, type) -> {

        if (type == SHOW_BOTTOM_SHEET) {
            BottomSheetDialog bottomSheet = new BottomSheetDialog(this, news);
            bottomSheet.show(getChildFragmentManager(), "exampleBottomSheet");
        } else {
            if (EasyPermissions.hasPermissions(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE))
                shareImage(news.getImageUrl());
            else {
                new AppSettingsDialog.Builder(this).build().show();
            }
        }

    };


    public static NewsFeedFragment newInstance() {
        return new NewsFeedFragment();
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsAdapter = new NewsAdapter(getContext(), new ArrayList<>(), itemClicked);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = NewsFeedFragmentBinding.inflate(inflater, container, false);


        mViewModel = obtainViewModel(requireActivity());


        setupListAdapter();

        binding.fab.setOnClickListener(v -> {
            refreshLayoutAdapter();
        });

        binding.searchBar.cameraIcon.setOnClickListener(v -> {
            takePicture();
        });

        binding.searchBar.searchIcon.setOnClickListener(v -> {
            binding.searchBar.indicator.show();
            binding.searchBar.searchIcon.setVisibility(View.GONE);
            binding.searchBar.indicator.setVisibility(View.VISIBLE);

            new Handler().postDelayed(() -> {
                binding.searchBar.searchIcon.setVisibility(View.VISIBLE);
                binding.searchBar.indicator.hide();
            }, 3000);
        });


        // Create an `ItemTouchHelper` and attach it to the `RecyclerView`
        ItemTouchHelper ith = new ItemTouchHelper(_ithCallback);
        ith.attachToRecyclerView(binding.recyclerView);

        setupObserver();


        return binding.getRoot();
    }

    private void setupObserver() {
        mViewModel.getNewsLiveData().observe(getViewLifecycleOwner(), news -> {
            newsAdapter.replaceData(news);
        });

        mViewModel.getNoNewsLiveData().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                binding.recyclerView.setVisibility(View.GONE);
                binding.noFeed.getRoot().setVisibility(View.VISIBLE);
            } else {
                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.noFeed.getRoot().setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.start();
    }

    public NewsFeedViewModel obtainViewModel(FragmentActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        return new ViewModelProvider(activity, factory).get(NewsFeedViewModel.class);
    }


    ItemTouchHelper.Callback _ithCallback = new ItemTouchHelper.Callback() {
        //and in your imlpementaion of
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            // get the viewHolder's and target's positions in your adapter data, swap them
            Collections.swap(newsAdapter.getItems(), viewHolder.getAdapterPosition(), target.getAdapterPosition());
            // and notify the adapter that its dataset has changed
            newsAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            //TODO
        }

        //defines the enabled move directions in each state (idle, swiping, dragging).
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                    ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.START | ItemTouchHelper.END);
        }
    };

    @AfterPermissionGranted(RC_CAMERA_AND_LOCATION)
    public void takePicture() {
        if (EasyPermissions.hasPermissions(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
            dispatchTakePictureIntent();
        } else {
            resultLauncherPicture.launch(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA});
        }
    }

//    @AfterPermissionGranted(RC_STORAGE)
//    public void tryToShareImage(String imageUrl) {
//        if (EasyPermissions.hasPermissions(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//            shareImage(imageUrl);
//        } else {
//            resultLauncherShare.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        }
//    }

    private void shareImage(String imageUrl) {

        Glide.with(requireContext()).asBitmap().load(imageUrl).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                Intent shareIntent = new Intent();

                shareIntent.setAction(Intent.ACTION_SEND);

                String path = MediaStore.Images.Media.insertImage(requireContext().getContentResolver(), resource, "", null);
                Log.i("Mohammad", "path: " + resource);

                Uri screenshotUri = Uri.parse(path);

                shareIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                shareIntent.setType("image/jpeg");
                requireContext().startActivity(Intent.createChooser(shareIntent, "share image"));
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
            }
        });

    }

    /**
     * because onRequestPermissionsResult @deprecated , we check result of (request permission manually)
     */
    ActivityResultLauncher<String[]> resultLauncherPicture = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
        boolean hasDenied = false;
        for (Map.Entry<String, Boolean> entry : result.entrySet()) {
            if (!entry.getValue()) {
                hasDenied = true;
            }
        }
        if (hasDenied) {
            new AppSettingsDialog.Builder(this).build().show();
        } else {
            takePicture();
        }

    });

    /**
     * because onRequestPermissionsResult @deprecated , we check result of (request permission manually)
     */
//    ActivityResultLauncher<String> resultLauncherShare = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
//        Log.i("Mohammad", "launcher: " + result);
//        if (!result) {
//            new AppSettingsDialog.Builder(this).build().show();
//        } else {
//        }
//
//    });
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            launcher.launch(takePictureIntent);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Log.i("Mohammad", "CameraPhoto");


                        Bundle extras = result.getData().getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        String path = MediaStore.Images.Media.insertImage(requireContext().getContentResolver(), imageBitmap, "", null);

                        Uri screenshotUri = Uri.parse(path);

                        Log.i("Mohammad", "screenShotUri: " + screenshotUri.toString());

                        News news = new News(String.valueOf(System.currentTimeMillis()), "test for android developer", screenshotUri.toString());

                        mViewModel.addNewsFromCamera(news);
                        newsAdapter.addNews(news);

                        createNotificationChannel();

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), "312")
                                .setSmallIcon(R.drawable.ic_launcher_foreground)
                                .setContentTitle("New Item")
                                .setContentText("you added News to list")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                        NotificationManager notificationManager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify((new Random()).nextInt(), builder.build());

                    }
                }
            });

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        CharSequence name = "channel_name";
        String description = "channel_description";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("312", name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = requireContext().getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    private void refreshLayoutAdapter() {
        if (binding.recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            newsAdapter.setLayoutManagerType(NewsAdapter.AUTOMATIC);
            binding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        } else {
            newsAdapter.setLayoutManagerType(NewsAdapter.FIXED);
            binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        newsAdapter.notifyDataSetChanged();

    }


    private void setupListAdapter() {

        binding.recyclerView.setAdapter(newsAdapter);

        runTimer();
    }

    private void runTimer() {
        long minute_10 = 10 * 60 * 1000;
//        long minute_10 = 5 * 1000;
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (newsAdapter.getItemCount() > 10) {
                    requireActivity().runOnUiThread(() -> {
                        newsAdapter.removeItemsMoreThan10();
                    });

                }

            }
        }, 0, minute_10);
    }

    @Override
    public void onButtonClicked(News news) {
        mViewModel.deleteNews(news);
        newsAdapter.deleteNews(news);

        if (newsAdapter.getItemCount() == 0) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.noFeed.getRoot().setVisibility(View.VISIBLE);
        } else {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.noFeed.getRoot().setVisibility(View.GONE);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull @NotNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull @NotNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    public interface ItemClicked {
        void onItemClicked(News news, int type);
    }
}