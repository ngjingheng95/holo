package com.arholo.ar.sceneform.samples.chromakeyvideo.camera;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ViewFlipper;


import com.arholo.ar.sceneform.samples.chromakeyvideo.CreateArTutorial;
import com.arholo.ar.sceneform.samples.chromakeyvideo.HoloHomepage;
import com.arholo.ar.sceneform.samples.chromakeyvideo.R;

public class CameraActivity extends AppCompatActivity {

   // private AlertDialog.Builder cameraHelpDialogBuilder;
    //private AlertDialog cameraHelpAlert;

    private ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect rect = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);


                setToImmersiveMode();

            }
        });


        // Initialise 'Help' Dialog
/*        cameraHelpDialogBuilder = new AlertDialog.Builder(this);
        cameraHelpDialogBuilder.setTitle("Create a Holo");
        cameraHelpDialogBuilder.setMessage("1. Stand in front of a green screen\n2. Ensure that the green screen covers your whole screen\n3. Tap on the red dot to start recording\n4. Tap on the white square once you are done to end recording.\n5. You can now add your creation into the surroundings!");
        cameraHelpDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        cameraHelpAlert = cameraHelpDialogBuilder.create();
*/
        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, VideoRecordingFragment.newInstance())
                    .commit();
        }
    }

    // Flipper Tutorial
    public void onClickCreateArTutorial(View view) {
        Intent intent = new Intent(CameraActivity.this, CreateArTutorial.class);
        startActivity(intent);
    }

    public void onClickBackBtn(View view) {
        Intent intent = new Intent(CameraActivity.this, HoloHomepage.class);
        startActivity(intent);
    }

    private void setToImmersiveMode() {
        // set to immersive
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    //public void onClickCameraHelpDialog(View view) {
    //    cameraHelpAlert.show();
    //}

}
