package com.arholo.ar.sceneform.samples.chromakeyvideo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class PlaceArTutorial extends AppCompatActivity {
    private ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_ar_tutorial);

        viewFlipper = findViewById(R.id.view_flipper);
        TextView textView = new TextView(this);
        textView.setText("Try it for yourself!");
        textView.setTextSize(24);
        textView.setGravity(Gravity.CENTER);

        viewFlipper.addView(textView);

        viewFlipper.setFlipInterval(2000);
        viewFlipper.stopFlipping();
    }

    public void onClickBackBtn(View view){
        Intent intent=new Intent(PlaceArTutorial.this, ChromaKeyVideoActivity.class);
        startActivity(intent);
    }


    public void previousView(View v) {
        viewFlipper.setInAnimation(this, R.anim.slide_in_right);
        viewFlipper.setOutAnimation(this, R.anim.slide_out_left);
        viewFlipper.showPrevious();
    }

    public void nextView(View v) {
        viewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);
        viewFlipper.showNext();
    }
}