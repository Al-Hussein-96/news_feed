# Android test for the Vacancy (Tapped Application)
<div>
      <img src="https://github.com/Al-Hussein-96/news_feed/blob/master/photo_2021-07-14_10-34-23.jpg" alt="Snow" style="width:100%">


</div>
### Summary
This sample is written in Java and based on the master branch which uses the following Architecture Components:

- ViewModel
- LiveData
- View Binding


if I have <b> enough time </b> I can do it with the following Architecture Components with (Kotlin):
- ViewModel
- LiveData
- View Binding & data Binding
- hilt for injection
- room
- kotlin-coroutines

## Questions No- 2
 A button to switch between fixed grid size and automatic height
based on the image's aspect ratio.


I just use StraggedLayoutManager with AdjustBound = true for automatic size and fix height of item for fixed size

#### for recycleView
```
<androidx.recyclerview.widget.RecyclerView
    android:id="@+id/recycler_view"
    android:layout_width="match_parent"
    android:layout_height="0dp"
    app:layoutManager="androidx.recyclerview.widget.StaggeredGridLayoutManager"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintTop_toBottomOf="@id/search_bar"
    app:spanCount="2"
    tools:itemCount="10"
    tools:listitem="@layout/item_news" />
```

#### for ImageView to get Aspect
```
android:adjustViewBounds="true"
```

## Questions No- 3
Cache all the images into Disk after downloading to be used offline.


### note: I didn't need to download image to disk because third-library have a Strategy to cache on disk

I can use any of this third-library to load image and cache theme on disk or memory
 - Fressco (https://github.com/facebook/fresco) (This is what I prefer and i will use it if I hava enough time)
 - Picasso (https://github.com/square/picasso)
 - Glide (https://github.com/bumptech/glide)    (This is what I used in a test)
 

## Questions No- 4
Make a custom search bar that looks like the following:
Note: don't forget to show/hide the progress
loader in the search bar

I do it with thrid-library (https://github.com/81813780/AVLoadingIndicatorView)

after cliked of serach icon I will start progress for 3 second then hide it

## Questions No- 5
Tap on the camera icon to take a picture from the camera and insert it
into the grid.
Note: Once you add the item to the grid, show a local system
notification

after get permission for (Camera & Storage)

pure code for take photo and add it to NewsFeed & show Local system notification
```
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
                       
                    /// for create Notification Channel api +28
                    createNotificationChannel();
                    
                    /// release notitfication
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
```

## Questions No- 6
Long press gestures on the grid item rearrange the positions of the
item

pure code for do this tasks (ItemTouchHelper)

```
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

```

## Questions No- 7

7. Tap on the grid item to delete with a confirmation bottom sheet.

- class BottomSheetDialog.java extends BottomSheetDialogFragment
- create resource: bottom_sheet_layout.xml

## Questions No- 8
easy to do with ViewMode

## Questions No- 9
Share button on the grid item to share the image to WhatsApp,
Facebook, etc

load image by Glide then share it.

```
 Glide.with(context).asBitmap().load(imageUrl).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);

                String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), resource, "", null);
                Log.i("Mohammad", "path: " + resource);

                Uri screenshotUri = Uri.parse(path);

                shareIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                shareIntent.setType("image/jpeg");
                context.startActivity(Intent.createChooser(shareIntent, "share image"));
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
            }
        });
```

## Questions No- 9

Add a timer to check every 10 minutes; if there are more than ten feeds,
remove all the extras

```
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
```

## Questions No- 10
 All the deleted feeds should go into Tab 2 as a list.
 
 easy to do
 
 
 ### tab2 (Trash)
 
 ## Questions No- 1
A table view is displaying all the deleted feeds with the date and time
of deletion.

I didn't use TableView , I just use RecycleView with items and add the date of deletion

## Questions No- 2
Add the 'swipe to delete' action to delete the feed permanently

do it with pure code 

- class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback 


