package com.arholo.ar.sceneform.samples.chromakeyvideo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.arholo.ar.sceneform.samples.chromakeyvideo.camera.CameraActivity;

public class HoloHomepage extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

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

    public void showPopup(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.homepage_menu_actions, popup.getMenu());
        popup.show();
    }

    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.homepage_menu_actions);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.homepage_help:
                Toast.makeText(this, "\'Help\' page Coming Soon!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.homepage_about:
                Toast.makeText(this, "\'About\' page Coming Soon!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }


//        Intent helpIntent = new Intent(HoloHomepage.this, ChromaKeyVideoActivity.class);
//        Intent aboutIntent = new Intent(HoloHomepage.this, CameraActivity.class);
//        switch (menuItem.getItemId()) {
//            case R.id.homepage_help:
//                startActivity(helpIntent);
//                return true;
//            case R.id.homepage_about:
//                startActivity(aboutIntent);
//                return true;
//            default:
//                return false;
//        }
    }
}
