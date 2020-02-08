package com.google.ar.sceneform.samples.chromakeyvideo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.ar.sceneform.samples.chromakeyvideo.camera.CameraActivity;

public class HoloHomepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holo_homepage);

        Button btnCreateArHolo = (Button)findViewById(R.id.btn_create_holo);
        Button btnPlaceHolo = (Button)findViewById(R.id.btn_place_holo);

        btnCreateArHolo.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(HoloHomepage.this, CameraActivity.class);
                startActivity(intent);
            }});

        btnPlaceHolo.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(HoloHomepage.this, ChromaKeyVideoActivity.class);
                startActivity(intent);
            }});



    }
}
