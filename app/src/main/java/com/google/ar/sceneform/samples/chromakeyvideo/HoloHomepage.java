package com.google.ar.sceneform.samples.chromakeyvideo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.google.ar.sceneform.samples.chromakeyvideo.camera.CameraActivity;
import com.google.ar.sceneform.samples.chromakeyvideo.options.Commons;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class HoloHomepage extends AppCompatActivity {

    private List<File> mediaFiles = new ArrayList<>();
    private HomepageMyHolosAdapter adapter;
    private ListView gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holo_homepage);

        gallery = (ListView) findViewById(R.id.homepage_my_holos_preview);

//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        gallery.setLayoutManager(layoutManager);

        adapter = new HomepageMyHolosAdapter(this, mediaFiles);
        gallery.setAdapter(adapter);

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

    @Override
    protected void onResume() {
//        Toast.makeText(this, "oRESUME =========================================================", Toast.LENGTH_SHORT).show();
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
    }
}
