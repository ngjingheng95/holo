package com.google.ar.sceneform.samples.chromakeyvideo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.google.ar.sceneform.samples.chromakeyvideo.camera.CameraActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HoloHomepage extends AppCompatActivity {

//    private HomepageMyHolosAdapter adapter;
//    private List<File> mediaFiles = new ArrayList<>();
//
//    private ListView gallery = (ListView) findViewById(R.id.homepage_my_holos_preview);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holo_homepage);

//        adapter = new HomepageMyHolosAdapter(this, mediaFiles);
//        gallery.setAdapter(adapter);



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
