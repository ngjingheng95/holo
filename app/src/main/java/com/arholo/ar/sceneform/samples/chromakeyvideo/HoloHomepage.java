package com.arholo.ar.sceneform.samples.chromakeyvideo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.arholo.ar.sceneform.samples.chromakeyvideo.camera.CameraActivity;

import java.util.List;
import java.util.Stack;

public class HoloHomepage extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private AlertDialog.Builder aboutDialogBuilder;
    private AlertDialog aboutAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_homepage);
        // Initialise 'Help' Dialog
        aboutDialogBuilder = new AlertDialog.Builder(this);
        aboutDialogBuilder.setTitle("About ARHolo");
        aboutDialogBuilder.setMessage("ARHolo is created by the following students from NTU School of Computer Science and Engineering (SCSE):\n- Ng Jing Heng (ngjingheng95@gmail.com)\n- Gan Jia Jun (jia_jun_94@hotmail.com)\n- Shiro Takeguchi (shiro.take96@gmail.com)\n\nSupervised by:\n- Dr Owen Noel Newton Fernando (ofernando@ntu.edu.sg)\n\n******\n\nIcons that appear in ARHolo were retrieved from\n- The Noun Project (thenounproject.com)\n- Flaticon (flaticon.com)\n\nARHolo uses the following resources to search for video assets:\n- Pixabay API (pixabay.com)");
        aboutDialogBuilder.setPositiveButton("Ok bro", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        aboutAlert = aboutDialogBuilder.create();
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

    public void onClickAboutDialog(View view){
        aboutAlert.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.homepage_help:
                Toast.makeText(this, "\'Help\' page Coming Soon!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.homepage_feedback:
                sendEmail();
                return true;
            case R.id.homepage_about:
                aboutAlert.show();
                return true;
            default:
                return false;
        }

    }

    public void sendEmail(){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("*/*");
        i.putExtra(Intent.EXTRA_EMAIL, new String[] {
                "arhologram2020@gmail.com"
        });
        i.putExtra(Intent.EXTRA_SUBJECT, "ARHolo Feedback");

        startActivity(createEmailOnlyChooserIntent(i, "Send via email"));
    }

    public Intent createEmailOnlyChooserIntent(Intent source,
                                               CharSequence chooserTitle) {
        Stack<Intent> intents = new Stack<Intent>();
        Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",
                "arhologram2020@gmail.com", null));
        List<ResolveInfo> activities = getPackageManager()
                .queryIntentActivities(i, 0);

        for(ResolveInfo ri : activities) {
            Intent target = new Intent(source);
            target.setPackage(ri.activityInfo.packageName);
            intents.add(target);
        }

        if(!intents.isEmpty()) {
            Intent chooserIntent = Intent.createChooser(intents.remove(0),
                    chooserTitle);
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                    intents.toArray(new Parcelable[intents.size()]));

            return chooserIntent;
        } else {
            return Intent.createChooser(source, chooserTitle);
        }
    }




}
