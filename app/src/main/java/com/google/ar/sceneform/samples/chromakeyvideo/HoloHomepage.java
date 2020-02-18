package com.google.ar.sceneform.samples.chromakeyvideo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.ar.sceneform.samples.chromakeyvideo.camera.CameraActivity;

public class HoloHomepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_homepage);
    }

    public void onClickCreateAr(View view){
        Intent intent = new Intent(HoloHomepage.this, CameraActivity.class);
        startActivity(intent);
    }

    public void onClickPlaceAr(View view){
        Intent intent = new Intent(HoloHomepage.this, ChromaKeyVideoActivity.class);
        startActivity(intent);
    }

}
