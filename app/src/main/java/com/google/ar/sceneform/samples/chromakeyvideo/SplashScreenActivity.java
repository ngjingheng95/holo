package com.google.ar.sceneform.samples.chromakeyvideo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.sceneform.samples.chromakeyvideo.options.Commons;
import com.google.ar.sceneform.samples.chromakeyvideo.options.Commons2;
import com.google.ar.sceneform.samples.chromakeyvideo.options.Commons3;
import com.google.ar.sceneform.samples.chromakeyvideo.options.Commons4;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class SplashScreenActivity extends AppCompatActivity {

    @BindView(R.id.splashscreen_loading_text)
    TextView loadingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Hides status bar and navigation bar in splash screen
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Check if permission for WRITE_EXTERNAL_STORAGE is granted, and ask user for permission if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        else{
            initApp();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // if user clicks "ALLOW" in pop-up box to grant WRITE_EXTERNAL_STORAGE, then initialise app
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    initApp();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
    public void initApp(){
        // initialise app
        // 1) Download raw assets
        // 2) Intent to ChromaKeyVideoActivity when assets are loaded
        new loadAssetTask().execute();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashScreenActivity.this,HoloHomepage.class);
                startActivity(intent);
                finish();
            }
        },1000);
    }

    private class loadAssetTask extends AsyncTask<Void, String, String>{
        // Downloads raw asset asynchronously during splash screen
        @Override
        protected String doInBackground(Void... voids) {
            Log.d("yoyo", "doInBackground: start");
            downloadRawAssets();
            Log.d("yoyo", "doInBackground: end");
            return "Done";
        }

        protected void onPostExecute(String result) {
//            Toast.makeText(SplashScreenActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }

    public void downloadRawAssets(){

        // My Holos
        File createHoloFolder = new File(Commons.MEDIA_DIR);
        if (!createHoloFolder.exists()) {
            createHoloFolder.mkdirs();
        }

        //Featured
        File folder = new File(Commons2.MEDIA_DIR);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            try {
                saveToSDCard(R.raw.dancingman, "dancingman.mp4", "Featured");
                saveToSDCard(R.raw.thanosdance, "thanosdance.mp4", "Featured");
                saveToSDCard(R.raw.peppapig2, "peppapig2.mp4", "Featured");
                saveToSDCard(R.raw.ironman, "ironman.mp4", "Featured");
                saveToSDCard(R.raw.alien, "alien.mp4", "Featured");
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        } else {
            Toast.makeText(SplashScreenActivity.this, "Failed - Error", Toast.LENGTH_SHORT).show();
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
                saveToSDCard(R.raw.dancingman, "dancingman.mp4", "Characters");
                saveToSDCard(R.raw.thanosdance, "thanosdance.mp4", "Characters");
                saveToSDCard(R.raw.ironman, "ironman.mp4", "Characters");
                saveToSDCard(R.raw.skeleton, "skeleton.mp4", "Characters");
                saveToSDCard(R.raw.alien, "alien.mp4", "Characters");
                saveToSDCard(R.raw.peppapig1, "peppapig1.mp4", "Characters");
                saveToSDCard(R.raw.peppapig2, "peppapig2.mp4", "Characters");
                saveToSDCard(R.raw.spiderman, "spiderman.mp4", "Characters");

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        } else {
            Toast.makeText(SplashScreenActivity.this, "Failed - Error", Toast.LENGTH_SHORT).show();
        }
        //Animals
        File folder3 = new File(Commons4.MEDIA_DIR);
        boolean success3 = true;
        if (!folder3.exists()) {
            success3 = folder3.mkdirs();
        }
        if (success3) {
            try {
                saveToSDCard(R.raw.monkey, "monkey.mp4", "Animals");
                saveToSDCard(R.raw.eagle, "eagle.mp4", "Animals");
                saveToSDCard(R.raw.flamingo, "flamingo.mp4", "Animals");
                saveToSDCard(R.raw.giraffe, "giraffe.mp4", "Animals");
                saveToSDCard(R.raw.rabbit, "rabbit.mp4", "Animals");
                saveToSDCard(R.raw.stock, "stock.mp4", "Animals");
                saveToSDCard(R.raw.pigeon, "pigeon.mp4", "Animals");
                saveToSDCard(R.raw.tiger, "tiger.mp4", "Animals");
                saveToSDCard(R.raw.shark, "shark.mp4", "Animals");
                saveToSDCard(R.raw.snake, "snake.mp4", "Animals");
                saveToSDCard(R.raw.trex1, "trex1.mp4", "Animals");
                saveToSDCard(R.raw.trex2, "trex2.mp4", "Animals");
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        } else {
            Toast.makeText(SplashScreenActivity.this, "Failed - Error", Toast.LENGTH_SHORT).show();
        }
        
    }

    public void saveToSDCard(int id, String name, String folder) throws Throwable {
        InputStream inStream = this.getResources().openRawResource(id);
        File file = new File(Environment.getExternalStorageDirectory() + "/0/dev/" + folder, name);
        if (!(file.exists())) {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
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

}
