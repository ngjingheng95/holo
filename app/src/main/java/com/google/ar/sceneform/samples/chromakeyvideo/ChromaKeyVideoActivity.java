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
package com.google.ar.sceneform.samples.chromakeyvideo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.PixelCopy;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.ar.core.Anchor;
import com.google.ar.core.Camera;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.core.Trackable;
import com.google.ar.core.TrackingState;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.ExternalTexture;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.samples.chromakeyvideo.options.Commons;
import com.google.ar.sceneform.samples.chromakeyvideo.options.Commons2;
import com.google.ar.sceneform.samples.chromakeyvideo.options.Commons3;
import com.google.ar.sceneform.samples.chromakeyvideo.options.Commons4;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This is an example activity that shows how to display a video with chroma key filtering in
 * Sceneform.
 */

/*
* gallery => My Holos
* gallery2 => Featured
* gallery3 => Characters
* gallery4 => Animals (Baseline Implementation)
* gallery5 => Pixabay
* */
public class ChromaKeyVideoActivity extends AppCompatActivity {
    private static final String TAG = ChromaKeyVideoActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    private WritingArFragment arFragment;

    private RelativeLayout placement_group;

//    private Button btnTry;

    private FloatingActionButton captureBtn;

    private Session session;

    @Nullable private ModelRenderable videoRenderable;
    private MediaPlayer mediaPlayer;

    // The color to filter out of the video.
    private static final Color CHROMA_KEY_COLOR = new Color(0.1843f, 1.0f, 0.098f);
//    private static final Color CHROMA_KEY_COLOR = new Color(0.0f, 1.0f, 0.0f);

    // Controls the height of the video in world space.
    private static final float VIDEO_HEIGHT_METERS = 0.85f;

    private ArPlacementController placementController;

    // VideoRecorder encapsulates all the video recording functionality.
    private VideoRecorder videoRecorder;

    // The UI to record.
    //private FloatingActionButton recordButton;

    String object;
    String vimeoUrl;

    @BindView(R.id.gallery)
    GridView gallery;

    @BindView(R.id.gallery2)
    GridView gallery2;

    @BindView(R.id.gallery3)
    GridView gallery3;

    // Animals Tab (Baseline Implementation)
    // !! DON'T CHANGE !!
    @BindView(R.id.gallery4)
    GridView gallery4;

    // Pixabay Tab
    @BindView(R.id.gallery5)
    DynamicGridView gallery5;

    private List<File> mediaFiles = new ArrayList<>();
    private List<File> mediaFiles2 = new ArrayList<>();
    private List<File> mediaFiles3 = new ArrayList<>();
    private List<File> mediaFiles4 = new ArrayList<>();
    private ChromaKeyVideoActivity.MediaFileAdapter adapter;
    private ChromaKeyVideoActivity.MediaFileAdapter adapter2;
    private ChromaKeyVideoActivity.MediaFileAdapter adapter3;
    private ChromaKeyVideoActivity.MediaFileAdapter adapter4;

    private boolean mIsPlayerRelease = true;

    private PixabayVideoRequestInfo pixabayVideoRequestInfo;
    private List<PixabayVideoInfo> pixabayVideoInfo = new ArrayList<>();
    private PixabayAdapter pixabayAdapter;

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


        //Featured
        File folder = new File(Commons2.MEDIA_DIR);
        boolean success = true;
        if (!folder.exists()) {
            //Toast.makeText(MainActivity.this, "Directory Does Not Exist, Create It", Toast.LENGTH_SHORT).show();
            success = folder.mkdirs();
        }
        if (success) {
            try {
//                saveToSDCard(R.raw.dance, "dance.mp4", "Featured");
//                saveToSDCard(R.raw.lion_chroma, "lion_chroma.mp4", "Featured");
                saveToSDCard(R.raw.spiderman, "spiderman.mp4", "Featured");
                saveToSDCard(R.raw.ironman, "ironman.mp4", "Featured");
                saveToSDCard(R.raw.robot2, "robot2.mp4", "Featured");
                saveToSDCard(R.raw.robot3, "robot3.mp4", "Featured");
                saveToSDCard(R.raw.robot4, "robot4.mp4", "Featured");
                saveToSDCard(R.raw.robot5, "robot5.mp4", "Featured");
                saveToSDCard(R.raw.batman, "batman.mp4", "Featured");
                saveToSDCard(R.raw.deadpool, "deadpool.mp4", "Featured");
//                saveToSDCard(R.raw.hulk, "hulk.mp4", "Featured");
                saveToSDCard(R.raw.minions, "minions.mp4", "Featured");
//                saveToSDCard(R.raw.people, "people.mp4", "Featured");
//                saveToSDCard(R.raw.skeleton, "skeleton.mp4", "Featured");
                saveToSDCard(R.raw.chicken, "chicken.mp4", "Featured");
                saveToSDCard(R.raw.monkey, "monkey.mp4", "Featured");
                saveToSDCard(R.raw.cheetah, "cheetah.mp4", "Featured");
                saveToSDCard(R.raw.eagle, "eagle.mp4", "Featured");
                saveToSDCard(R.raw.elephants, "elephants.mp4", "Featured");
//                saveToSDCard(R.raw.flamingo, "flamingo.mp4", "Featured");
                saveToSDCard(R.raw.giraffe, "giraffe.mp4", "Featured");
//                saveToSDCard(R.raw.rabbit, "rabbit.mp4", "Featured");
//                saveToSDCard(R.raw.stock, "stock.mp4", "Featured");
                saveToSDCard(R.raw.tiger, "tiger.mp4", "Featured");
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        } else {
            Toast.makeText(ChromaKeyVideoActivity.this, "Failed - Error", Toast.LENGTH_SHORT).show();
        }
        //Characters
        File folder2 = new File(Commons3.MEDIA_DIR);
        boolean success2 = true;
        if (!folder2.exists()) {
            //Toast.makeText(MainActivity.this, "Directory Does Not Exist, Create It", Toast.LENGTH_SHORT).show();
            success2 = folder2.mkdirs();
        }
        if (success2) {
            try {
//                saveToSDCard(R.raw.dance, "dance.mp4", "Characters");
//                saveToSDCard(R.raw.lion_chroma, "lion_chroma.mp4", "Characters");
                saveToSDCard(R.raw.batman, "batman.mp4", "Characters");
                saveToSDCard(R.raw.deadpool, "deadpool.mp4", "Characters");
                saveToSDCard(R.raw.hulk, "hulk.mp4", "Characters");
                saveToSDCard(R.raw.ironman, "ironman.mp4", "Characters");
                saveToSDCard(R.raw.minions, "minions.mp4", "Characters");
                saveToSDCard(R.raw.people, "people.mp4", "Characters");
                saveToSDCard(R.raw.skeleton, "skeleton.mp4", "Characters");
                saveToSDCard(R.raw.spiderman, "spiderman.mp4", "Characters");
//                saveToSDCard(R.raw.robot1, "robot1.mp4", "Characters");
                saveToSDCard(R.raw.robot2, "robot2.mp4", "Characters");
                saveToSDCard(R.raw.robot3, "robot3.mp4", "Characters");
                saveToSDCard(R.raw.robot4, "robot4.mp4", "Characters");
                saveToSDCard(R.raw.robot5, "robot5.mp4", "Characters");
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        } else {
            Toast.makeText(ChromaKeyVideoActivity.this, "Failed - Error", Toast.LENGTH_SHORT).show();
        }
        //Animals
        File folder3 = new File(Commons4.MEDIA_DIR);
        boolean success3 = true;
        if (!folder3.exists()) {
            //Toast.makeText(ChromaKeyVideoActivity.this, "Directory Does Not Exist, Create It", Toast.LENGTH_SHORT).show();
            success3 = folder3.mkdirs();
        }
        if (success3) {
            try {
                saveToSDCard(R.raw.chicken, "chicken.mp4", "Animals");
                saveToSDCard(R.raw.monkey, "monkey.mp4", "Animals");
                saveToSDCard(R.raw.cheetah, "cheetah.mp4", "Animals");
                saveToSDCard(R.raw.eagle, "eagle.mp4", "Animals");
                saveToSDCard(R.raw.elephants, "elephants.mp4", "Animals");
                saveToSDCard(R.raw.flamingo, "flamingo.mp4", "Animals");
                saveToSDCard(R.raw.giraffe, "giraffe.mp4", "Animals");
                saveToSDCard(R.raw.rabbit, "rabbit.mp4", "Animals");
                saveToSDCard(R.raw.stock, "stock.mp4", "Animals");
                saveToSDCard(R.raw.tiger, "tiger.mp4", "Animals");

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        } else {
            Toast.makeText(ChromaKeyVideoActivity.this, "Failed - Error", Toast.LENGTH_SHORT).show();
        }

        arFragment = (WritingArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        //Remove animation
        //Animation that ask you to place the screen at a surface
//        arFragment.getArSceneView().getPlaneRenderer().setEnabled(false);
        arFragment.getArSceneView().getPlaneRenderer().setEnabled(true);
        // remove dots
        arFragment.getPlaneDiscoveryController().hide();
//        arFragment.getPlaneDiscoveryController().show();
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
        //captureBtn.setOnClickListener(view -> takePhoto());

        FloatingActionButton btnCreateHolo = (FloatingActionButton)findViewById(R.id.create_holo);

        btnCreateHolo.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                Intent createARActivity = new Intent(ChromaKeyVideoActivity.this, CreateARActivity.class);
                startActivity(createARActivity);
            }});


//    btnTry = (Button) this.findViewById(R.id.btnTry);

        // Create an ExternalTexture for displaying the contents of the video.
        ExternalTexture texture = new ExternalTexture();


        //A ViewGroup is a special view that can contain other views (called children.) The view group is the base class for layouts and views containers.
        //placementGroup is view with tabs containing the videos.
        ViewGroup placementGroup = (ViewGroup)this.findViewById(R.id.placement_group);
        this.placementController = new ArPlacementController(placementGroup);

        ButterKnife.bind(this);

        //mediaDir.setText(String.format("Media files are saved under:\n%s", Commons.MEDIA_DIR));

        setupTabLayout();



        // ADD VIMEO TEST
        FloatingActionButton btnAddVimeo = (FloatingActionButton)findViewById(R.id.add_vimeo_fab);
        btnAddVimeo.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                Frame frame = arFragment.getArSceneView().getArFrame();
                android.graphics.Point pt = getScreenCenter();
                List<HitResult> hits;
                if (frame != null){
                    hits = frame.hitTest(pt.x, pt.y);
                    for (HitResult hit : hits){
                        Trackable trackable = hit.getTrackable();
                        if (trackable instanceof Plane &&
                                ((Plane) trackable).isPoseInPolygon(hit.getHitPose())){
//                            Toast.makeText(this, "HIT!!!!", Toast.LENGTH_SHORT).show();
                            vimeoUrl = "https://player.vimeo.com/external/254487612.sd.mp4?s=4f03928350d2e957851399761889e6b56276bf8f&profile_id=165";
                            changeObject1_vimeo(hit.createAnchor());
                            break;
                        }
                    }
                }
            }});

//      ==== ORIGINAL - MY HOLOS ====
//      ====    DON'T CHANGE     ====
        //My holos
        adapter = new ChromaKeyVideoActivity.MediaFileAdapter(this, mediaFiles);
        gallery.setAdapter(adapter);
        gallery.setOnItemClickListener((parent, view, position, id) -> {
            File file = adapter.getItem(position);

            //playOrViewMedia(file); Commons.MEDIA_DIR + "/"+
            object = file + "";
            changeObject(texture, object);

            // Create the Anchor.
            createAnchor(texture);
        });

//      ==== TEST 1 ====
        //Featured
        adapter2 = new ChromaKeyVideoActivity.MediaFileAdapter(this, mediaFiles2);
        gallery2.setOnItemClickListener((parent, view, position, id) -> {
            Frame frame = arFragment.getArSceneView().getArFrame();
            android.graphics.Point pt = getScreenCenter();
            List<HitResult> hits;
            if (frame != null){
                hits = frame.hitTest(pt.x, pt.y);
                for (HitResult hit : hits){
                    Trackable trackable = hit.getTrackable();
                    if (trackable instanceof Plane &&
                            ((Plane) trackable).isPoseInPolygon(hit.getHitPose())){
                        Toast.makeText(this, "HIT!!!!", Toast.LENGTH_SHORT).show();
//                        MediaPlayer mp1 = new MediaPlayer();
                        File file = adapter2.getItem(position);
                        object = file + "";
//                        File file1 = adapter2.getItem(position + 1);
//                        object1 = file1 + "";
//                        mp1 = MediaPlayer.create(this, Uri.parse(object));
//                        changeObject1(texture, hit.createAnchor(), mp1);
//                        changeObject1(texture, object1, hit.createAnchor(), mp2);
                        changeObject1(hit.createAnchor());
                        break;
                    }
                }
            }
        });

//      ==== TEST 2 ====
        adapter3 = new ChromaKeyVideoActivity.MediaFileAdapter(this, mediaFiles3);
        gallery3.setAdapter(adapter3);
        gallery3.setOnItemClickListener((parent, view, position, id) -> {
            Frame frame = arFragment.getArSceneView().getArFrame();
            android.graphics.Point pt = getScreenCenter();
            List<HitResult> hits;
            //if the centre is on plane
            boolean hitAllowed = false;
            if (frame != null){
                hits = frame.hitTest(pt.x, pt.y);
                for (HitResult hit : hits){
                    Trackable trackable = hit.getTrackable();
                    if (trackable instanceof Plane &&
                            ((Plane) trackable).isPoseInPolygon(hit.getHitPose())){
                        hitAllowed = true;
//                        MediaPlayer mp1 = new MediaPlayer();
                        Toast.makeText(this, "HIT!!!! 3333", Toast.LENGTH_SHORT).show();
                        File file = adapter3.getItem(position);
                        object = file + "";
//                        mp1 = MediaPlayer.create(this, Uri.parse(object));
//                        changeObject1(texture2, hit.createAnchor(), mp1);
                        changeObject1(hit.createAnchor());
                        break;
                    }
                }
                if (!hitAllowed){
                    Toast.makeText(this, "NOT HIT!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

//      ==== ORIGINAL 1 - ANIMALS ====
//      ====     DON'T CHANGE!    ====
        adapter4 = new ChromaKeyVideoActivity.MediaFileAdapter(this, mediaFiles4);
        gallery4.setAdapter(adapter4);
        gallery4.setOnItemClickListener((parent, view, position, id) -> {
            File file = adapter4.getItem(position);

            //playOrViewMedia(file); Commons.MEDIA_DIR + "/"+

            object = file + "";
            changeObject(texture, object);
            // Create the Anchor.
            createAnchor(texture);
        });

//      ==== PIXABAY TAB ====
        String q = "green+screen";
        boolean safesearch = true;
        int page = 1;
        int perPage = 200;
        pixabayAdapter = new PixabayAdapter(this, pixabayVideoInfo);
        gallery5.setAdapter(pixabayAdapter);
        loadPixabayVideoRequestInfo(q, safesearch, page, perPage);
//        pixabayVideoInfo = pixabayVideoRequestInfo.getHits();
        gallery5.setOnItemClickListener((parent, view, position, id) -> {
            Frame frame = arFragment.getArSceneView().getArFrame();
            android.graphics.Point pt = getScreenCenter();
            List<HitResult> hits;
            if (frame != null){
                hits = frame.hitTest(pt.x, pt.y);
                for (HitResult hit : hits){
                    Trackable trackable = hit.getTrackable();
                    if (trackable instanceof Plane &&
                            ((Plane) trackable).isPoseInPolygon(hit.getHitPose())){
                        Toast.makeText(this, "HIT!!!!", Toast.LENGTH_SHORT).show();
                        vimeoUrl = pixabayVideoInfo.get(position).getVideos().getSmall().getUrl();
                        changeObject1_vimeo(hit.createAnchor());
                        break;
                    }
                }
            }
        });
    }


//    private ExternalTexture nextAvailableTexture() {
//
//    }

    public void loadPixabayVideoRequestInfo(String q, boolean safesearch, int page, int perPage) {
        Log.d("MyApp", "loadPixabayVideoRequestInfo");
//        String q = query.concat("+green+screen");
//
        PixabayService.createPixabayService().getVideoResults(getString(R.string.pixabay_api_key), q, safesearch, page, perPage).enqueue(new Callback<PixabayVideoRequestInfo>() {
            @Override
            public void onResponse(Call<PixabayVideoRequestInfo> call, Response<PixabayVideoRequestInfo> response) {
                Log.d("MyApp", "onResponse()");
                addImagesToList(response.body());
//                pixabayVideoRequestInfo = response.body();
            }

            @Override
            public void onFailure(Call<PixabayVideoRequestInfo> call, Throwable t) {
                Log.d("MyApp", "onFailure()");

            }


        });

        Log.d("MyApp", "loadPixabayVideoRequestInfo END");
    }

    public void addImagesToList(PixabayVideoRequestInfo response){
        int position = pixabayVideoInfo.size();
        pixabayVideoInfo.addAll(response.getHits());
        pixabayAdapter.notifyDataSetChanged();
    }

    public void changeObject(ExternalTexture texture, String object) {
        Toast.makeText(this, "changeObject called", Toast.LENGTH_SHORT).show();
        mediaPlayer = new MediaPlayer();
        // Create an Android MediaPlayer to capture the video on the external texture's surface.
        mediaPlayer = MediaPlayer.create(this, Uri.parse(object));
        mediaPlayer.setSurface(texture.getSurface());
        mediaPlayer.setLooping(true);

        float videoWidth = mediaPlayer.getVideoWidth();
        float videoHeight = mediaPlayer.getVideoHeight();
        //Display Portrait Videos
        if (videoWidth < videoHeight) {
            ModelRenderable.builder()
                    .setSource(this, R.raw.chroma_key_video_2)
                    .build()
                    .thenAccept(
                            renderable -> {
                                videoRenderable = renderable;
                                videoRenderable.getMaterial().setExternalTexture("videoTexture", texture);
                                videoRenderable.getMaterial().setFloat4("keyColor", CHROMA_KEY_COLOR);
                            })
                    .exceptionally(
                            throwable -> {
                                Toast toast =
                                        Toast.makeText(this, "Unable to load video renderable", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                return null;
                            });

//          ModelRenderable.builder()
//                    .setSource(this, R.raw.chroma_key_video_2)
//                    .build()
//                    .thenAccept(
//                            renderable -> {
//                                videoRenderable = renderable;
//                                renderable.getMaterial().setExternalTexture("videoTexture", texture);
//                                renderable.getMaterial().setFloat4("keyColor", CHROMA_KEY_COLOR);
//                            })
//                    .exceptionally(
//                            throwable -> {
//                                Toast toast =
//                                        Toast.makeText(this, "Unable to load video renderable", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
//                                return null;
//                            });
        }
        //Display Landscape Videos
        else {
            ModelRenderable.builder()
                    .setSource(this, R.raw.chroma_key_video)
                    .build()
                    .thenAccept(
                            renderable -> {
                                videoRenderable = renderable;
                                videoRenderable.getMaterial().setExternalTexture("videoTexture", texture);
                                videoRenderable.getMaterial().setFloat4("keyColor", CHROMA_KEY_COLOR);
                            })
                    .exceptionally(
                            throwable -> {
                                Toast toast =
                                        Toast.makeText(this, "Unable to load video renderable", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                return null;
                            });

//            ModelRenderable.builder()
//                    .setSource(this, R.raw.chroma_key_video)
//                    .build()
//                    .thenAccept(
//                            renderable -> {
//                                videoRenderable = renderable;
//                                renderable.getMaterial().setExternalTexture("videoTexture", texture);
//                                renderable.getMaterial().setFloat4("keyColor", CHROMA_KEY_COLOR);
//                            })
//                    .exceptionally(
//                            throwable -> {
//                                Toast toast =
//                                        Toast.makeText(this, "Unable to load video renderable", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
//                                return null;
//                            });
        }

        // Create a renderable with a material that has a parameter of type 'samplerExternal' so that
        // it can display an ExternalTexture. The material also has an implementation of a chroma key
        // filter.

        try {
            // Create the session.
            session = new Session(/* context= */ this);

        } catch (UnavailableArcoreNotInstalledException
                e) {

        } catch (UnavailableApkTooOldException e) {

        } catch (UnavailableSdkTooOldException e) {

        }

        catch (Exception e) {

        }
    }


    public void changeObject1(ExternalTexture texture, String object, Anchor anchor) {
//        Toast.makeText(this, "changeObject1 called", Toast.LENGTH_SHORT).show();
//        MediaPlayer mediaPlayer1 = new MediaPlayer();
        // Create an Android MediaPlayer to capture the video on the external texture's surface.
        MediaPlayer mediaPlayer1 = MediaPlayer.create(this, Uri.parse(object));
        mediaPlayer1.setSurface(texture.getSurface());
        mediaPlayer1.setLooping(true);

        float videoWidth = mediaPlayer1.getVideoWidth();
        float videoHeight = mediaPlayer1.getVideoHeight();
        //Display Portrait Videos
        if (videoWidth < videoHeight) {
            ModelRenderable.builder()
                    .setSource(this, R.raw.chroma_key_video_2)
                    .build()
                    .thenAccept(
                            renderable -> {
                                renderable.getMaterial().setExternalTexture("videoTexture", texture);
                                renderable.getMaterial().setFloat4("keyColor", CHROMA_KEY_COLOR);
                                addNodeToScene(anchor, renderable, texture, mediaPlayer1);
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
            ModelRenderable.builder()
                    .setSource(this, R.raw.chroma_key_video)
                    .build()
                    .thenAccept(
                            renderable -> {
                                renderable.getMaterial().setExternalTexture("videoTexture", texture);
                                renderable.getMaterial().setFloat4("keyColor", CHROMA_KEY_COLOR);
                                addNodeToScene(anchor, renderable, texture, mediaPlayer1);
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



//    takes in mediaplayer as param - pass in texture and mediaplayer
    public void changeObject1(ExternalTexture texture, Anchor anchor, MediaPlayer mediaPlayer1) {
//        Toast.makeText(this, "changeObject1 called", Toast.LENGTH_SHORT).show();
//        MediaPlayer mediaPlayer1 = new MediaPlayer();
        // Create an Android MediaPlayer to capture the video on the external texture's surface.
//        mediaPlayer1 = MediaPlayer.create(this, Uri.parse(object));
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
//        videoNode.setRenderable(renderable);
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
//        videoNode.select();

        arFragment.getArSceneView().getScene().addChild(anchorNode);
        videoNode.select();
    }

    public void createAnchor(ExternalTexture texture) {
        Toast.makeText(this, "createAnchor called", Toast.LENGTH_SHORT).show();
        session = arFragment.getArSceneView().getSession();

        try {
            if (session == null) {
                arFragment.getArSceneView().setupSession(session);
                session = arFragment.getArSceneView().getSession();
            }

            Frame frame = session.update();
            Camera camera = frame.getCamera();

            // initial placement of AR objects
            Anchor anchor = null;
            //Pose cameraPosition = arFragment.getArSceneView().getScene()..getCamera().getDisplayOrientedPose();
            Pose mCameraRelativePose= Pose.makeTranslation(0.15f, -0.15f, -0.5f);
            Pose rotation=Pose.makeRotation(0,0,0 ,0 );
            Pose rotate = mCameraRelativePose.compose(rotation);

            Pose cameraPos =  camera.getPose().compose(rotate);
            //(Plane plane : session.getAllTrackables(Plane.class))
            for (Plane plane : frame.getUpdatedTrackables(Plane.class)) {
                if (plane.getType() == com.google.ar.core.Plane.Type.HORIZONTAL_UPWARD_FACING
                        && camera.getTrackingState() == TrackingState.TRACKING) {

                    if (anchor == null) {
                        anchor = plane.createAnchor(plane.getCenterPose().compose(cameraPos));
                    }
                    break;
                }
                else {
                    //anchor = session.createAnchor(cameraPos);
                }
            }

            //Anchor anchor = session.createAnchor(cameraPos);

            AnchorNode anchorNode = new AnchorNode(anchor);
            //anchorNode.setLocalRotation(Quaternion.axisAngle(new Vector3(0, 0, 1f), 90));
            anchorNode.setParent(arFragment.getArSceneView().getScene());

            // Create a node to render the video and add it to the anchor.
            //Node videoNode = new Node();
            DoubleTapTransformableNode videoNode = new DoubleTapTransformableNode(arFragment.getTransformationSystem());
            videoNode.setParent(anchorNode);
            //arFragment.getArSceneView().getScene().addChild(anchorNode);
            //anchorNode.addChild((Node)videoNode);

            // Set the scale of the node so that the aspect ratio of the video is correct.
            float videoWidth = mediaPlayer.getVideoWidth();
            float videoHeight = mediaPlayer.getVideoHeight();
//        if (videoHeight > videoWidth) {
//            videoNode.setLocalScale(
//                    new Vector3(
//                            VIDEO_HEIGHT_METERS, VIDEO_HEIGHT_METERS * (videoHeight / videoWidth), 1.0f));
//        }
//        else {
            videoNode.setLocalScale(
                    new Vector3(
                            VIDEO_HEIGHT_METERS * (videoWidth / videoHeight), VIDEO_HEIGHT_METERS, 1.0f));
//        }
            //Quaternion rotation1 = Quaternion.axisAngle(new Vector3(0.0f, 0.0f, 1.0f), 90); // rotate X axis 90 degrees
            //videoNode.setWorldRotation(rotation1);
            // Start playing the video when the first node is placed.
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();

                // Wait to set the renderable until the first frame of the  video becomes available.
                // This prevents the renderable from briefly appearing as a black quad before the video
                // plays.
                texture
                        .getSurfaceTexture()
                        .setOnFrameAvailableListener(
                                (SurfaceTexture surfaceTexture) -> {
                                    videoNode.setRenderable(videoRenderable);
                                    texture.getSurfaceTexture().setOnFrameAvailableListener(null);
                                });
            } else {
                videoNode.setRenderable(videoRenderable);
            }
            mIsPlayerRelease = false;

            videoNode.setOnDoubleTapListener((DoubleTapTransformableNode.OnDoubleTapListener)(new DoubleTapTransformableNode.OnDoubleTapListener() {
                public final void onDoubleTap() {
                    if (!mIsPlayerRelease) {
                        mediaPlayer.release();
                        mIsPlayerRelease = true;
                    }
//                anchor = null;
                    anchorNode.removeChild((Node)videoNode);
                }
            }));
            videoNode.select();

        } catch (CameraNotAvailableException e) {
            e.printStackTrace();
        }
    }

//    public void createAnchor1(ExternalTexture texture) {
//        Toast.makeText(this, "createAnchor called", Toast.LENGTH_SHORT).show();
////        session = arFragment.getArSceneView().getSession();
//
//        try {
//            if (session == null) {
//                arFragment.getArSceneView().setupSession(session);
//                session = arFragment.getArSceneView().getSession();
//            }
//
//            Frame frame = session.update();
//            Camera camera = frame.getCamera();
//
//            // initial placement of AR objects
//            Anchor anchor = null;
//            //Pose cameraPosition = arFragment.getArSceneView().getScene()..getCamera().getDisplayOrientedPose();
//            Pose mCameraRelativePose= Pose.makeTranslation(0.15f, -0.15f, -0.5f);
//            Pose rotation=Pose.makeRotation(0,0,0 ,0 );
//            Pose rotate = mCameraRelativePose.compose(rotation);
//
//            Pose cameraPos =  camera.getPose().compose(rotate);
//            //(Plane plane : session.getAllTrackables(Plane.class))
//            for (Plane plane : frame.getUpdatedTrackables(Plane.class)) {
//                if (plane.getType() == com.google.ar.core.Plane.Type.HORIZONTAL_UPWARD_FACING
//                        && camera.getTrackingState() == TrackingState.TRACKING) {
//
//                    if (anchor == null) {
//                        anchor = plane.createAnchor(plane.getCenterPose().compose(cameraPos));
//                    }
//                    break;
//                }
//                else {
//                    //anchor = session.createAnchor(cameraPos);
//                }
//            }
//
//            //Anchor anchor = session.createAnchor(cameraPos);
//
//            AnchorNode anchorNode = new AnchorNode(anchor);
//            //anchorNode.setLocalRotation(Quaternion.axisAngle(new Vector3(0, 0, 1f), 90));
//            anchorNode.setParent(arFragment.getArSceneView().getScene());
//
//            // Create a node to render the video and add it to the anchor.
//            //Node videoNode = new Node();
//            DoubleTapTransformableNode videoNode = new DoubleTapTransformableNode(arFragment.getTransformationSystem());
//            videoNode.setParent(anchorNode);
//            //arFragment.getArSceneView().getScene().addChild(anchorNode);
//            //anchorNode.addChild((Node)videoNode);
//
//            // Set the scale of the node so that the aspect ratio of the video is correct.
//            float videoWidth = mediaPlayer.getVideoWidth();
//            float videoHeight = mediaPlayer.getVideoHeight();
////        if (videoHeight > videoWidth) {
////            videoNode.setLocalScale(
////                    new Vector3(
////                            VIDEO_HEIGHT_METERS, VIDEO_HEIGHT_METERS * (videoHeight / videoWidth), 1.0f));
////        }
////        else {
//            videoNode.setLocalScale(
//                    new Vector3(
//                            VIDEO_HEIGHT_METERS * (videoWidth / videoHeight), VIDEO_HEIGHT_METERS, 1.0f));
////        }
//            //Quaternion rotation1 = Quaternion.axisAngle(new Vector3(0.0f, 0.0f, 1.0f), 90); // rotate X axis 90 degrees
//            //videoNode.setWorldRotation(rotation1);
//            // Start playing the video when the first node is placed.
//            if (!mediaPlayer.isPlaying()) {
//                mediaPlayer.start();
//
//                // Wait to set the renderable until the first frame of the  video becomes available.
//                // This prevents the renderable from briefly appearing as a black quad before the video
//                // plays.
//                texture
//                        .getSurfaceTexture()
//                        .setOnFrameAvailableListener(
//                                (SurfaceTexture surfaceTexture) -> {
//                                    videoNode.setRenderable(videoRenderable);
//                                    texture.getSurfaceTexture().setOnFrameAvailableListener(null);
//                                });
//            } else {
//                videoNode.setRenderable(videoRenderable);
//            }
//            mIsPlayerRelease = false;
//
//            videoNode.setOnDoubleTapListener((DoubleTapTransformableNode.OnDoubleTapListener)(new DoubleTapTransformableNode.OnDoubleTapListener() {
//                public final void onDoubleTap() {
//                    if (!mIsPlayerRelease) {
//                        mediaPlayer.release();
//                        mIsPlayerRelease = true;
//                    }
////                anchor = null;
//                    anchorNode.removeChild((Node)videoNode);
//                }
//            }));
//            videoNode.select();
//
//        } catch (CameraNotAvailableException e) {
//            e.printStackTrace();
//        }
//    }

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
            adapter.notifyDataSetChanged();
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
            adapter2.notifyDataSetChanged();
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
            adapter3.notifyDataSetChanged();
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
            adapter4.notifyDataSetChanged();
        }

    }

    public void saveToSDCard(int id, String name, String folder) throws Throwable {
        InputStream inStream = this.getResources().openRawResource(id);
        File file = new File(Environment.getExternalStorageDirectory() + "/0/dev/" + folder, name);
        if (!(file.exists())) {
            FileOutputStream fileOutputStream = new FileOutputStream(file);//存入SDCard
            byte[] buffer = new byte[10];
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            int len = 0;
            while((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            byte[] bs = outStream.toByteArray();
            fileOutputStream.write(bs);
            outStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        }
        else {
            return;
        }
        inStream.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void setupTabLayout() {
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabTapped(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                onTabTapped(tab.getPosition());
            }
        });
    }


    private void onTabTapped(int position) {
        Toast.makeText(this, "gallery: " + position, Toast.LENGTH_SHORT).show();
        switch (position) {
            case 0:
                gallery.setVisibility(View.VISIBLE);
                gallery2.setVisibility(View.GONE);
                gallery3.setVisibility(View.GONE);
                gallery4.setVisibility(View.GONE);
                gallery5.setVisibility(View.GONE);
                break;
            case 1:
                gallery.setVisibility(View.GONE);
                gallery2.setVisibility(View.VISIBLE);
                gallery3.setVisibility(View.GONE);
                gallery4.setVisibility(View.GONE);
                gallery5.setVisibility(View.GONE);
                break;
            case 2:
                gallery.setVisibility(View.GONE);
                gallery2.setVisibility(View.GONE);
                gallery3.setVisibility(View.VISIBLE);
                gallery4.setVisibility(View.GONE);
                gallery5.setVisibility(View.GONE);
                break;
            case 3:
                gallery.setVisibility(View.GONE);
                gallery2.setVisibility(View.GONE);
                gallery3.setVisibility(View.GONE);
                gallery4.setVisibility(View.VISIBLE);
                gallery5.setVisibility(View.GONE);
                break;
            case 4:
                gallery.setVisibility(View.GONE);
                gallery2.setVisibility(View.GONE);
                gallery3.setVisibility(View.GONE);
                gallery4.setVisibility(View.GONE);
                gallery5.setVisibility(View.VISIBLE);
                break;
            default:
                break;
                //Toast.makeText(this, "Tapped " + position, Toast.LENGTH_SHORT);
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

    private class MediaFileAdapter extends BaseAdapter {

        private List<File> files;

        private Context context;

        MediaFileAdapter(Context c, List<File> files) {
            context = c;
            this.files = files;
        }

        public int getCount() {
            return files.size();
        }

        public File getItem(int position) {
            return files.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_media, parent, false);
            }
            imageView = convertView.findViewById(R.id.item_image);
            View indicator = convertView.findViewById(R.id.item_indicator);
            File file = getItem(position);
            if (isVideo(file)) {
                indicator.setVisibility(View.VISIBLE);
            } else {
                indicator.setVisibility(View.GONE);
            }
            Glide.with(context).load(file).into(imageView);
            return convertView;
        }
    }

//    private class GridViewAdapter extends ArrayAdapter<GridItem>

    private String generateFilename() {
        String date =
                new SimpleDateFormat("yyyyMMddHHmmss", java.util.Locale.getDefault()).format(new Date());
        return Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES) + File.separator + "Sceneform/" + date + "_screenshot.jpg";
    }

    private void saveBitmapToDisk(Bitmap bitmap, String filename) throws IOException {

        File out = new File(filename);
        if (!out.getParentFile().exists()) {
            out.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(filename);
             ByteArrayOutputStream outputData = new ByteArrayOutputStream()) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputData);
            outputData.writeTo(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException ex) {
            throw new IOException("Failed to save bitmap to disk", ex);
        }
    }

    private void takePhoto() {
        final String filename = generateFilename();
        ArSceneView view = arFragment.getArSceneView();

        // Create a bitmap the size of the scene view.
        final Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);

        // Create a handler thread to offload the processing of the image.
        final HandlerThread handlerThread = new HandlerThread("PixelCopier");
        handlerThread.start();
        // Make the request to copy.
        PixelCopy.request(view, bitmap, (copyResult) -> {
            if (copyResult == PixelCopy.SUCCESS) {
                try {
                    saveBitmapToDisk(bitmap, filename);
                } catch (IOException e) {
                    Toast toast = Toast.makeText(ChromaKeyVideoActivity.this, e.toString(),
                            Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                        "Photo saved", Snackbar.LENGTH_LONG);
                snackbar.setAction("Open in Photos", v -> {
                    File photoFile = new File(filename);

                    Uri photoURI = FileProvider.getUriForFile(ChromaKeyVideoActivity.this,
                            ChromaKeyVideoActivity.this.getPackageName() + ".provider",
                            photoFile);
                    Intent intent = new Intent(Intent.ACTION_VIEW, photoURI);
                    intent.setDataAndType(photoURI, "image/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);

                });
                snackbar.show();
            } else {
                Toast toast = Toast.makeText(ChromaKeyVideoActivity.this,
                        "Failed to copyPixels: " + copyResult, Toast.LENGTH_LONG);
                toast.show();
            }
            handlerThread.quitSafely();
        }, new Handler(handlerThread.getLooper()));
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

    private android.graphics.Point getScreenCenter() {
        View vw = findViewById(android.R.id.content);
        return new android.graphics.Point(vw.getWidth()/2, vw.getHeight()/2); }

    private boolean isVideo(File file) {
        return file != null && file.getName().endsWith(".mp4");
    }

}
