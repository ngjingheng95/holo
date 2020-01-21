package com.google.ar.sceneform.samples.chromakeyvideo;

import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.ar.sceneform.samples.chromakeyvideo.options.Commons2;
import com.google.ar.sceneform.samples.chromakeyvideo.options.Commons3;
import com.google.ar.sceneform.samples.chromakeyvideo.options.Commons4;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Hide status bar and navigation bar in splash screen
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

//        Toast.makeText(this, "========================================================= SPLASH ONCREATE ", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        downloadRawAssets();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashScreenActivity.this,ChromaKeyVideoActivity.class);
                startActivity(intent);
                finish();
            }
        },4000);
//        Intent intent = new Intent(getApplicationContext(), ChromaKeyVideoActivity.class);
//        startActivity(intent);
//        finish();

    }

    public void downloadRawAssets(){

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
                Toast.makeText(this, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ CHHROMA ONCREATE @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(SplashScreenActivity.this, "Failed - Error", Toast.LENGTH_SHORT).show();
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
//                Toast.makeText(SplashScreenActivity.this, "ANIMALS DONE", Toast.LENGTH_SHORT).show();
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
}
