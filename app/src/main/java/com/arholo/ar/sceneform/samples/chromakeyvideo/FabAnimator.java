package com.arholo.ar.sceneform.samples.chromakeyvideo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

public class FabAnimator {
    public static boolean rotateFab(final View v, boolean rotate) {
        v.animate().setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                })
                .rotation(rotate ? 135f : 0f).scaleX(rotate ? 0.8f : 1f).scaleY(rotate ? 0.8f : 1f);
        return rotate;
    }
}
