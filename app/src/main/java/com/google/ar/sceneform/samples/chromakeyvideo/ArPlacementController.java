//package com.google.ar.sceneform.samples.chromakeyvideo;
//
//import android.animation.ValueAnimator;
//import android.support.design.widget.FloatingActionButton;
//import android.util.DisplayMetrics;
//import android.util.TypedValue;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewTreeObserver;
//
//public class ArPlacementController{
//    private float TARGET_BACKGROUND_ALPHA;
//    private long ANIMATION_DURATION;
//    private View menuBackground;
//    private ViewGroup menu;
//    private FloatingActionButton fab;
//    private ValueAnimator animator;
//    private int menuWidth;
//    private boolean visible;
//    private float visiblePosition;
//
//    private void applyVisibility() {
//        if (this.menuWidth == 0) {
//            this.applyVisibilityImmediate();
//        } else {
//            ValueAnimator animator = this.animator;
//            if (this.animator != null) {
//                animator.cancel();
//            }
//
//            float targetPosition = this.visible ? 1.0F : 0.0F;
//            this.animator = ValueAnimator.ofFloat(new float[]{this.visiblePosition, targetPosition});
//            animator = this.animator;
//            if (this.animator != null) {
//                animator.addUpdateListener((ValueAnimator.AnimatorUpdateListener)(new ValueAnimator.AnimatorUpdateListener() {
//                    public final void onAnimationUpdate(ValueAnimator it) {
//                        ArPlacementController animator = ArPlacementController.this;
//                        visiblePosition = (Float)it.getAnimatedValue();
//                        updateVisibility();
//                    }
//                }));
//            }
//
//            animator = this.animator;
//            if (this.animator != null) {
//                animator.setDuration(this.ANIMATION_DURATION);
//                animator.start();
//            }
//        }
//    }
//
//    private final void updateVisibility() {
//        if (this.visiblePosition > 0.0F) {
//            this.menu.setVisibility(View.VISIBLE);
//        } else {
//            this.menu.setVisibility(View.GONE);
//        }
//
//        this.fab.setRotation(-315.0F * this.visiblePosition);
//        this.fab.setScaleX(0.8F + 0.3F * ((float)1 - this.visiblePosition));
//        this.fab.setScaleY(0.8F + 0.3F * ((float)1 - this.visiblePosition));
//        DisplayMetrics displaymetrics = new DisplayMetrics();
//        int fabTranslation = (int) TypedValue.applyDimension(1, 52.0F, displaymetrics);
//        this.fab.setTranslationX((float)(-fabTranslation) * this.visiblePosition);
//        this.menuBackground.setAlpha(this.TARGET_BACKGROUND_ALPHA * this.visiblePosition);
//        int menuTranslation = (int)TypedValue.applyDimension(1, 16.0F, displaymetrics );
//        this.menu.setTranslationX(((float)this.menuWidth + 10.0F) * (1.0F - this.visiblePosition) + (float)menuTranslation);
//    }
//
//    private final void applyVisibilityImmediate() {
//        ValueAnimator animator = this.animator;
//        if (this.animator != null) {
//            animator.cancel();
//        }
//
//        this.visiblePosition = this.visible ? 1.0F : 0.0F;
//        this.updateVisibility();
//    }
//
//    public final void setVisible(boolean visible) {
//        this.visible = visible;
//        this.applyVisibility();
//    }
//
//    public ArPlacementController(ViewGroup placementGroup) {
//        this.TARGET_BACKGROUND_ALPHA = 0.33F;
//        this.ANIMATION_DURATION = 225L;
//
//
//        View v = placementGroup.findViewById(R.id.placement_menu_background);
//
//
//
//        this.menuBackground = v;
//        v = placementGroup.findViewById(R.id.placement_menu);
//
//        this.menu = (ViewGroup)v;
//
//        v = placementGroup.findViewById(R.id.placement_fab);
//
//        this.fab = (FloatingActionButton)v;
//
//
//        placementGroup.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver.OnGlobalLayoutListener)(new ViewTreeObserver.OnGlobalLayoutListener() {
//            public final void onGlobalLayout() {
//                boolean update = ArPlacementController.this.menuWidth == 0;
//                ArPlacementController.this.menuWidth = ArPlacementController.this.menu.getWidth();
//                if (update) {
//                    ArPlacementController.this.applyVisibilityImmediate();
//                }
//
//            }
//        }));
//        this.fab.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
//            public final void onClick(View it) {
//                ArPlacementController.this.visible = !ArPlacementController.this.visible;
//                ArPlacementController.this.applyVisibility();
//            }
//        }));
//        this.menuBackground.setOnTouchListener((View.OnTouchListener)(new View.OnTouchListener() {
//            public final boolean onTouch(View $noName_0, MotionEvent $noName_1) {
//                ArPlacementController.this.visible = false;
//                ArPlacementController.this.applyVisibility();
//                return false;
//            }
//        }));
//        this.menu.setOnClickListener((View.OnClickListener)null);
//    }
//
//}
//
