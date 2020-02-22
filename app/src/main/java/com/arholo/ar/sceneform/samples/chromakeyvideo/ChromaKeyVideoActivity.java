/*
 * Copyright 2018 Google LLC. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.arholo.ar.sceneform.samples.chromakeyvideo;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.arholo.ar.sceneform.samples.chromakeyvideo.options.Commons;
import com.arholo.ar.sceneform.samples.chromakeyvideo.options.Commons2;
import com.arholo.ar.sceneform.samples.chromakeyvideo.options.Commons3;
import com.arholo.ar.sceneform.samples.chromakeyvideo.options.Commons4;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.ExternalTexture;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.arholo.ar.sceneform.samples.chromakeyvideo.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChromaKeyVideoActivity extends AppCompatActivity implements RecyclerViewAdapter.OnArObjectListener, RecyclerViewPixabayAdapter.OnArObjectListener {
    private static final String TAG = ChromaKeyVideoActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    private WritingArFragment arFragment;

    private RelativeLayout placement_group;

    private FloatingActionButton captureBtn;

    @Nullable private ModelRenderable videoRenderable;
    private MediaPlayer mediaPlayer;

    // The color to filter out of the video.
    private static final Color CHROMA_KEY_COLOR = new Color(0.1843f, 1.0f, 0.098f);

    // Controls the height of the video in world space.
    private static final float VIDEO_HEIGHT_METERS = 0.85f;

    // VideoRecorder encapsulates all the video recording functionality.
    private VideoRecorder videoRecorder;

    String object;
    String vimeoUrl;
    final String defaultQuery = "green+screen";
    boolean safesearch = true;
    int page = 1;
    int perPage = 200;
    boolean isRotate = false;


    @BindView(R.id.add_holo_fab)
    FloatingActionButton addHoloFab;

    @BindView(R.id.main_tab)
    RelativeLayout mainTab;

    @BindView(R.id.main_tab_group)
    RelativeLayout mainTabGroup;

    @BindView(R.id.buttons_tab)
    LinearLayout buttonsTab;

    @BindView(R.id.animals_tab)
    RecyclerView animalsRecyclerView;

    @BindView(R.id.characters_tab)
    RecyclerView charactersRecyclerView;

    @BindView(R.id.new_tab)
    RecyclerView newRecyclerView;

    @BindView(R.id.my_holos_tab)
    RecyclerView myHoloRecyclerView;

    @BindView(R.id.pixabay_tab)
    RecyclerView pixabayRecyclerView;

    @BindView(R.id.back_button)
    RelativeLayout backButton;

    @BindView(R.id.pixabay_search_bar)
    RelativeLayout pixabaySearchBar;

    @BindView(R.id.pixabay_search_btn)
    RelativeLayout pixabaySearchBtn;

    @BindView(R.id.pixabay_search_text)
    AppCompatEditText pixabaySearchText;

    @BindView(R.id.no_internet_view)
    RelativeLayout noInternetView;

    private List<File> mediaFiles = new ArrayList<>();
    private List<File> mediaFiles2 = new ArrayList<>();
    private List<File> mediaFiles3 = new ArrayList<>();
    private List<File> mediaFiles4 = new ArrayList<>();
    private RecyclerViewAdapter animalsAdapter;
    private RecyclerView.LayoutManager animalsLayoutManager;
    private RecyclerViewAdapter charactersAdapter;
    private RecyclerView.LayoutManager charactersLayoutManager;
    private RecyclerViewAdapter newAdapter;
    private RecyclerView.LayoutManager newLayoutManager;
    private RecyclerViewAdapter myHoloAdapter;
    private RecyclerView.LayoutManager myHoloLayoutManager;
    private RecyclerViewPixabayAdapter pixabayRecyclerAdapter;
    private RecyclerView.LayoutManager pixabayLayoutManager;

    private boolean mIsPlayerRelease = true;

    private List<PixabayVideoInfo> pixabayVideoInfo = new ArrayList<>();

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    // CompletableFuture requires api level 24
    // FutureReturnValueIgnored is not valid
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }

        setContentView(R.layout.activity_video);

        arFragment = (WritingArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        //Animation that ask you to place the screen at a surface
        arFragment.getArSceneView().getPlaneRenderer().setEnabled(true);
        arFragment.getPlaneDiscoveryController().hide();
        arFragment.getPlaneDiscoveryController().setInstructionView(null);

        // Initialize the VideoRecorder.
        videoRecorder = new VideoRecorder();
        int orientation = getResources().getConfiguration().orientation;
        videoRecorder.setVideoQuality(CamcorderProfile.QUALITY_2160P, orientation);
        videoRecorder.setSceneView(arFragment.getArSceneView());

        captureBtn = (FloatingActionButton) findViewById(R.id.capture_video);
        captureBtn.setOnClickListener(this::toggleRecording);
        captureBtn.setEnabled(true);
        captureBtn.setImageResource(R.drawable.round_videocam);

        mainTabGroup = (RelativeLayout) findViewById(R.id.main_tab_group);

        // Create an ExternalTexture for displaying the contents of the video.
        ExternalTexture texture = new ExternalTexture();


        //A ViewGroup is a special view that can contain other views (called children.) The view group is the base class for layouts and views containers.
        //placementGroup is view with tabs containing the videos.
        ViewGroup placementGroup = (ViewGroup)this.findViewById(R.id.placement_group);

        ButterKnife.bind(this);

        // Tutorial

        Button HELP = (Button)findViewById(R.id.place_holo_help);

        HELP.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                MyCustomAlertDialog();
            }
        });

        /***
        *
        * Horizontal LinearLayout Tabs
        *
         ***/

        // New Tab
        newRecyclerView.setHasFixedSize(true);
        newLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        newRecyclerView.setLayoutManager(newLayoutManager);
        newAdapter = new RecyclerViewAdapter(this, mediaFiles2, this, "new");
        newRecyclerView.setAdapter(newAdapter);

        // Animals Tab
        animalsRecyclerView.setHasFixedSize(true);
        animalsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        animalsRecyclerView.setLayoutManager(animalsLayoutManager);
        animalsAdapter = new RecyclerViewAdapter(this, mediaFiles4, this, "animals");
        animalsRecyclerView.setAdapter(animalsAdapter);

        // Characters Tab
        charactersRecyclerView.setHasFixedSize(true);
        charactersLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        charactersRecyclerView.setLayoutManager(charactersLayoutManager);
        charactersAdapter = new RecyclerViewAdapter(this, mediaFiles3, this, "characters");
        charactersRecyclerView.setAdapter(charactersAdapter);

        // My Holos Tab
        myHoloRecyclerView.setHasFixedSize(true);
        myHoloLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        myHoloRecyclerView.setLayoutManager(myHoloLayoutManager);
        myHoloAdapter = new RecyclerViewAdapter(this, mediaFiles, this, "myHolo");
        myHoloRecyclerView.setAdapter(myHoloAdapter);


        // Pixabay Tab
        loadPixabayResults(defaultQuery, safesearch, page, perPage);
        pixabayRecyclerView.setHasFixedSize(true);
        pixabayLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        pixabayRecyclerView.setLayoutManager(pixabayLayoutManager);
        pixabayRecyclerAdapter = new RecyclerViewPixabayAdapter(this, pixabayVideoInfo, this, "pixabay");
        pixabayRecyclerView.setAdapter(pixabayRecyclerAdapter);

        // Pixabay Search
        pixabaySearchBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(!internetCheck()){
                    showNoInternetView();
                }
                else {
                    checkInternetView();
                    if (pixabaySearchText.getText().toString().isEmpty()) {
                        loadPixabayVideoRequestInfo1(defaultQuery, safesearch, page, perPage);
                    } else {
                        String q = pixabaySearchText.getText().toString().trim().replace(" +", "+") + "+" + defaultQuery;
                        loadPixabayVideoRequestInfo1(q, safesearch, page, perPage);
                    }
                }
            }
        });

        pixabaySearchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    if(!internetCheck()){
                        showNoInternetView();
                    }
                    else {
                        checkInternetView();
                        if (!pixabaySearchText.getText().toString().isEmpty()) {
                            // Perform action on key press
                            String q = pixabaySearchText.getText().toString().trim().replace(" +", "+") + "+" + defaultQuery;
                            loadPixabayVideoRequestInfo1(q, safesearch, page, perPage);
                            return true;
                        } else {
                            loadPixabayVideoRequestInfo1(defaultQuery, safesearch, page, perPage);
                        }
                    }

                }
                return false;
            }
        });
    }

    public void showNoInternetView(){

        noInternetView.setVisibility(View.VISIBLE);
        pixabayRecyclerView.setVisibility(View.GONE);

    }

    public void checkInternetView(){
        if (noInternetView.getVisibility() == View.VISIBLE){
            noInternetView.setVisibility(View.GONE);
            pixabayRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    public boolean internetCheck(){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        return connected;
    }

    @Override
    public void onArObjectClick(int position, String tag) {
        enableArPlaneListener(position, tag);
    }

    public void onClickMainTabView(View view){
        isRotate = FabAnimator.rotateFab(view, !isRotate);
        if (mainTabGroup.getVisibility() == View.GONE) {
            mainTabGroup.setVisibility(View.VISIBLE);
        }
        else{
            mainTabGroup.setVisibility(View.GONE);
        }
    }

    public void onClickAnimalsView(View view){
        buttonsTab.setVisibility(View.GONE);
        animalsRecyclerView.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.VISIBLE);
    }

    public void onClickCharactersView(View view){
        buttonsTab.setVisibility(View.GONE);
        charactersRecyclerView.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.VISIBLE);
    }

    public void onClickNewView(View view){
        buttonsTab.setVisibility(View.GONE);
        newRecyclerView.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.VISIBLE);
    }

    public void onClickMyHoloView(View view){
        buttonsTab.setVisibility(View.GONE);
        myHoloRecyclerView.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.VISIBLE);
    }

    public void onClickPixabayView(View view){
        buttonsTab.setVisibility(View.GONE);
        if (internetCheck()){
            pixabayRecyclerView.setVisibility(View.VISIBLE);
        }
        else{
            noInternetView.setVisibility(View.VISIBLE);
        }
        pixabaySearchBar.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.VISIBLE);
    }

    public void onClickInternetCheck(View view){
        if (internetCheck()){
            noInternetView.setVisibility(View.GONE);
            pixabayRecyclerView.setVisibility(View.VISIBLE);
            loadPixabayVideoRequestInfo1(defaultQuery, safesearch, page, perPage);
        }
        else{
            Toast.makeText(this, "No Internet!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickBackView(View view){
        animalsRecyclerView.setVisibility(View.GONE);
        charactersRecyclerView.setVisibility(View.GONE);
        newRecyclerView.setVisibility(View.GONE);
        myHoloRecyclerView.setVisibility(View.GONE);
        pixabayRecyclerView.setVisibility(View.GONE);
        pixabaySearchBar.setVisibility(View.GONE);
        noInternetView.setVisibility(View.GONE);
        buttonsTab.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.GONE);
    }

    public void onClickBackBtn1(View view){
        Intent intent=new Intent(ChromaKeyVideoActivity.this, HoloHomepage.class);
        startActivity(intent);
    }

    public void onClickCloseView(View view){
        mainTabGroup.setVisibility(View.GONE);
        isRotate = FabAnimator.rotateFab(addHoloFab, !isRotate);
    }

    public void onClickComingSoon(View view){
        Toast.makeText(this, "Feature Coming Soon!", Toast.LENGTH_SHORT).show();
    }

    public void enableArPlaneListener(int position, String tag){

        switch(tag){
            case "new":
                RecyclerViewAdapter newAdapter1 = (RecyclerViewAdapter) newRecyclerView.getAdapter();
                object = newAdapter1.getItem(position) + "";
                break;
            case "animals":
                RecyclerViewAdapter animalsAdapter1 = (RecyclerViewAdapter) animalsRecyclerView.getAdapter();
                object = animalsAdapter1.getItem(position) + "";
                break;
            case "characters":
                RecyclerViewAdapter charactersAdapter1 = (RecyclerViewAdapter) charactersRecyclerView.getAdapter();
                object = charactersAdapter1.getItem(position) + "";
                break;
            case "myHolo":
                RecyclerViewAdapter myHoloAdapter1 = (RecyclerViewAdapter) myHoloRecyclerView.getAdapter();
                object = myHoloAdapter1.getItem(position) + "";
                break;
            case "pixabay":
                RecyclerViewPixabayAdapter pixabayRecyclerAdapter1 = (RecyclerViewPixabayAdapter) pixabayRecyclerView.getAdapter();
                vimeoUrl = pixabayRecyclerAdapter1.getItem(position).getVideos().getSmall().getUrl();
                break;

        }

        if (tag == "pixabay") {
            arFragment.setOnTapArPlaneListener(
                    (HitResult hitresult, Plane plane, MotionEvent motionevent) -> {
                        changeObject1_vimeo(hitresult.createAnchor());
                        disableArPlaneListener(tag);
                    }
            );
        }
        else {
            arFragment.setOnTapArPlaneListener(
                    (HitResult hitresult, Plane plane, MotionEvent motionevent) -> {
                        changeObject1(hitresult.createAnchor());
                        disableArPlaneListener(tag);
                    }
            );
        }
    }

    public void disableArPlaneListener(String tag){
        arFragment.setOnTapArPlaneListener(null);
        switch(tag){
            case "new":
                newAdapter.clearHighlight();
                break;
            case "animals":
                animalsAdapter.clearHighlight();
                break;
            case "characters":
                charactersAdapter.clearHighlight();
                break;
            case "myHolo":
                myHoloAdapter.clearHighlight();
                break;
            case "pixabay":
                pixabayRecyclerAdapter.clearHighlight();
                break;
        }
    }

    public void loadPixabayResults(String q, boolean safesearch, int page, int perPage){
        pixabayVideoInfo.clear();
        PixabayService.createPixabayService().getVideoResults(getString(R.string.pixabay_api_key), q, safesearch, page, perPage).enqueue(new Callback<PixabayVideoRequestInfo>() {
            @Override
            public void onResponse(Call<PixabayVideoRequestInfo> call, Response<PixabayVideoRequestInfo> response) {
                pixabayVideoInfo.addAll(response.body().getHits());
            }
            @Override
            public void onFailure(Call<PixabayVideoRequestInfo> call, Throwable t) {
            }
        });
    }

    //  Cases:
//  - totalHits > 0 => Success
//  - totalHits == 0 => No Results
//  - totalHits == -1 => Failure
    public void loadPixabayVideoRequestInfo1(String q, boolean safesearch, int page, int perPage) {
        pixabayVideoInfo.clear();
        PixabayService.createPixabayService().getVideoResults(getString(R.string.pixabay_api_key), q, safesearch, page, perPage).enqueue(new Callback<PixabayVideoRequestInfo>() {
            @Override
            public void onResponse(Call<PixabayVideoRequestInfo> call, Response<PixabayVideoRequestInfo> response) {
                pixabayVideoInfo.addAll(response.body().getHits());
                pixabayRecyclerAdapter.notifyDataSetChanged();
                Toast.makeText(ChromaKeyVideoActivity.this, response.body().getTotalHits() + " results found.", Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onFailure(Call<PixabayVideoRequestInfo> call, Throwable t) {
                Log.d("MyApp", "onFailure()");
//                Toast.makeText(ChromaKeyVideoActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });

        Log.d("MyApp", "loadPixabayVideoRequestInfo1 END");
    }

//  Same as changeObject1 but streams video from online url
    private void changeObject1_vimeo(Anchor anchor) {
        ExternalTexture texture = new ExternalTexture();
        MediaPlayer mediaPlayer1 = new MediaPlayer();
        try {
            mediaPlayer1.setDataSource(vimeoUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer1.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer1.setSurface(texture.getSurface());
        mediaPlayer1.setLooping(true);

        float videoWidth = mediaPlayer1.getVideoWidth();
        float videoHeight = mediaPlayer1.getVideoHeight();
        //Display Portrait Videos
        if (videoWidth < videoHeight) {
            MediaPlayer finalMediaPlayer = mediaPlayer1;
            ModelRenderable.builder()
                    .setSource(this, R.raw.chroma_key_video_2)
                    .build()
                    .thenAccept(
                            renderable -> {
                                renderable.getMaterial().setExternalTexture("videoTexture", texture);
                                renderable.getMaterial().setFloat4("keyColor", CHROMA_KEY_COLOR);
                                addNodeToScene(anchor, renderable, texture, finalMediaPlayer);
                            })
                    .exceptionally(
                            throwable -> {
                                Toast toast =
                                        Toast.makeText(this, "Unable to load video renderable", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                return null;
                            });

        }
        //Display Landscape Videos
        else {
            MediaPlayer finalMediaPlayer1 = mediaPlayer1;
            ModelRenderable.builder()
                    .setSource(this, R.raw.chroma_key_video)
                    .build()
                    .thenAccept(
                            renderable -> {
                                renderable.getMaterial().setExternalTexture("videoTexture", texture);
                                renderable.getMaterial().setFloat4("keyColor", CHROMA_KEY_COLOR);
                                addNodeToScene(anchor, renderable, texture, finalMediaPlayer1);
                            })
                    .exceptionally(
                            throwable -> {
                                Toast toast =
                                        Toast.makeText(this, "Unable to load video renderable", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                return null;
                            });


        }
    }

//   takes in mediaplayer as param
    public void changeObject1(Anchor anchor) {
//        Toast.makeText(this, "changeObject1 called", Toast.LENGTH_SHORT).show();
//        MediaPlayer mediaPlayer1 = new MediaPlayer();
        // Create an Android MediaPlayer to capture the video on the external texture's surface.
        ExternalTexture texture = new ExternalTexture();
        MediaPlayer mediaPlayer1 = MediaPlayer.create(this, Uri.parse(object));
        mediaPlayer1.setSurface(texture.getSurface());
        mediaPlayer1.setLooping(true);

        float videoWidth = mediaPlayer1.getVideoWidth();
        float videoHeight = mediaPlayer1.getVideoHeight();
        //Display Portrait Videos
        if (videoWidth < videoHeight) {
            MediaPlayer finalMediaPlayer = mediaPlayer1;
            ModelRenderable.builder()
                    .setSource(this, R.raw.chroma_key_video_2)
                    .build()
                    .thenAccept(
                            renderable -> {
                                renderable.getMaterial().setExternalTexture("videoTexture", texture);
                                renderable.getMaterial().setFloat4("keyColor", CHROMA_KEY_COLOR);
                                addNodeToScene(anchor, renderable, texture, finalMediaPlayer);
                            })
                    .exceptionally(
                            throwable -> {
                                Toast toast =
                                        Toast.makeText(this, "Unable to load video renderable", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                return null;
                            });

        }
        //Display Landscape Videos
        else {
            MediaPlayer finalMediaPlayer1 = mediaPlayer1;
            ModelRenderable.builder()
                    .setSource(this, R.raw.chroma_key_video)
                    .build()
                    .thenAccept(
                            renderable -> {
                                renderable.getMaterial().setExternalTexture("videoTexture", texture);
                                renderable.getMaterial().setFloat4("keyColor", CHROMA_KEY_COLOR);
                                addNodeToScene(anchor, renderable, texture, finalMediaPlayer1);
                            })
                    .exceptionally(
                            throwable -> {
                                Toast toast =
                                        Toast.makeText(this, "Unable to load video renderable", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                return null;
                            });


        }
    }

    private void addNodeToScene(Anchor anchor, ModelRenderable renderable, ExternalTexture texture, MediaPlayer mediaPlayer) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        DoubleTapTransformableNode videoNode = new DoubleTapTransformableNode(arFragment.getTransformationSystem());
        videoNode.setParent(anchorNode);

        float videoWidth = mediaPlayer.getVideoWidth();
        float videoHeight = mediaPlayer.getVideoHeight();
        //all same size
        videoNode.setLocalScale(
                new Vector3(
                        VIDEO_HEIGHT_METERS * (videoWidth / videoHeight), VIDEO_HEIGHT_METERS, 1.0f));

        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();

            // Wait to set the renderable until the first frame of the  video becomes available.
            // This prevents the renderable from briefly appearing as a black quad before the video
            // plays.
            texture
                    .getSurfaceTexture()
                    .setOnFrameAvailableListener(
                            (SurfaceTexture surfaceTexture) -> {
                                videoNode.setRenderable(renderable);
                                texture.getSurfaceTexture().setOnFrameAvailableListener(null);
                            });
        } else {
            videoNode.setRenderable(renderable);
        }
        mIsPlayerRelease = false;

        //double tap to remove node
        videoNode.setOnDoubleTapListener((DoubleTapTransformableNode.OnDoubleTapListener)(new DoubleTapTransformableNode.OnDoubleTapListener() {
            public final void onDoubleTap() {
                if (!mIsPlayerRelease) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mIsPlayerRelease = true;
                }
//                anchor = null;
                anchorNode.removeChild((Node)videoNode);
            }
        }));
        arFragment.getArSceneView().getScene().addChild(anchorNode);
        videoNode.select();
    }

    @Override
    protected void onResume() {
        super.onResume();
        File file = new File(Commons.MEDIA_DIR);
        if (file.isDirectory()) {
            mediaFiles.clear();
            File[] files = file.listFiles();
            Arrays.sort(files, (f1, f2) -> {
                if (f1.lastModified() - f2.lastModified() == 0) {
                    return 0;
                } else {
                    return f1.lastModified() - f2.lastModified() > 0 ? -1 : 1;
                }
            });
            mediaFiles.addAll(Arrays.asList(files));
        }

        File file2 = new File(Commons2.MEDIA_DIR);
        if (file2.isDirectory()) {
            mediaFiles2.clear();
            File[] files2 = file2.listFiles();
            Arrays.sort(files2, (f1, f2) -> {
                if (f1.lastModified() - f2.lastModified() == 0) {
                    return 0;
                } else {
                    return f1.lastModified() - f2.lastModified() > 0 ? -1 : 1;
                }
            });
            mediaFiles2.addAll(Arrays.asList(files2));
        }

        File file3 = new File(Commons3.MEDIA_DIR);
        if (file3.isDirectory()) {
            mediaFiles3.clear();
            File[] files3 = file3.listFiles();
            Arrays.sort(files3, (f1, f2) -> {
                if (f1.lastModified() - f2.lastModified() == 0) {
                    return 0;
                } else {
                    return f1.lastModified() - f2.lastModified() > 0 ? -1 : 1;
                }
            });
            mediaFiles3.addAll(Arrays.asList(files3));
        }

        File file4 = new File(Commons4.MEDIA_DIR);
        if (file4.isDirectory()) {
            mediaFiles4.clear();
            File[] files4 = file4.listFiles();
            Arrays.sort(files4, (f1, f2) -> {
                if (f1.lastModified() - f2.lastModified() == 0) {
                    return 0;
                } else {
                    return f1.lastModified() - f2.lastModified() > 0 ? -1 : 1;
                }
            });
            mediaFiles4.addAll(Arrays.asList(files4));
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /**
     * Returns false and displays an error message if Sceneform can not run, true if Sceneform can run
     * on this device.
     *
     * <p>Sceneform requires Android N on the device as well as OpenGL 3.0 capabilities.
     *
     * <p>Finishes the activity if Sceneform can not run
     */
    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }

    @Override
    protected void onPause() {
        if (videoRecorder.isRecording()) {
            toggleRecording(null);
        }
        super.onPause();

    }

    /*
     * Used as a handler for onClick, so the signature must match onClickListener.
     */
    private void toggleRecording(View unusedView) {
        if (!arFragment.hasWritePermission()) {
            Log.e(TAG, "Video recording requires the WRITE_EXTERNAL_STORAGE permission");
            Toast.makeText(
                    this,
                    "Video recording requires the WRITE_EXTERNAL_STORAGE permission",
                    Toast.LENGTH_LONG)
                    .show();
            arFragment.launchPermissionSettings();
            return;
        }
        boolean recording = videoRecorder.onToggleRecord();
        if (recording) {
            captureBtn.setImageResource(R.drawable.round_stop);
        } else {
            captureBtn.setImageResource(R.drawable.round_videocam);
            String videoPath = videoRecorder.getVideoPath().getAbsolutePath();
            Toast.makeText(this, "Video saved: " + videoPath, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Video saved: " + videoPath);

            // Send  notification of updated content.
            ContentValues values = new ContentValues();
            values.put(MediaStore.Video.Media.TITLE, "Sceneform Video");
            values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
            values.put(MediaStore.Video.Media.DATA, videoPath);
            getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
        }
    }

    // Tutorial
    public void MyCustomAlertDialog(){
        final Dialog MyDialog = new Dialog(ChromaKeyVideoActivity.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.customdialog);
        MyDialog.setTitle("My Custom Dialog");

        Button hello = (Button)MyDialog.findViewById(R.id.hello);
        Button close = (Button)MyDialog.findViewById(R.id.close);

        hello.setEnabled(true);
        close.setEnabled(true);

        hello.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "Test3", Toast.LENGTH_LONG).show();
            }
        });
        close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                MyDialog.cancel();
            }
        });
        MyDialog.show();
    }

}
