package com.google.ar.sceneform.samples.chromakeyvideo.camera;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.ar.sceneform.samples.chromakeyvideo.HoloHomepage;
import com.google.ar.sceneform.samples.chromakeyvideo.R;
import com.google.ar.sceneform.samples.chromakeyvideo.options.Commons;

import java.io.File;

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, VideoRecordingFragment.newInstance())
                    .commit();
        }
    }

    public void onClickBackBtn(View view){
        Intent intent=new Intent(CameraActivity.this, HoloHomepage.class);
        startActivity(intent);
    }
}
