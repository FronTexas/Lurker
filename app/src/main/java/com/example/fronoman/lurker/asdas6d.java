//package com.example.androidviewflipper;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.support.v4.view.GestureDetectorCompat;
//import android.view.GestureDetector;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.widget.Button;
//import android.widget.Toast;
//import android.widget.ViewFlipper;
//
//public class MainActivity extends Activity implements
//        GestureDetector.OnGestureListener {
//
//    private GestureDetectorCompat mDetector;
//
//    Button buttonPrev, buttonNext;
//    ViewFlipper viewFlipper;
//
//    Animation slide_in_left, slide_out_right;
//    Animation slide_in_right, slide_out_left;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        mDetector = new GestureDetectorCompat(this,this);
//
//        buttonPrev = (Button) findViewById(R.id.prev);
//        buttonNext = (Button) findViewById(R.id.next);
//        viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
//
//        slide_in_left = AnimationUtils.loadAnimation(this,
//                R.anim.slide_in_left);
//        slide_out_right = AnimationUtils.loadAnimation(this,
//                R.anim.slide_out_right);
//
//        slide_in_right = AnimationUtils.loadAnimation(this,
//                R.anim.slide_in_right);
//        slide_out_left = AnimationUtils.loadAnimation(this,
//                R.anim.slide_out_left);
//
//        //viewFlipper.setInAnimation(slide_in_left);
//        //viewFlipper.setOutAnimation(slide_out_right);
//
//        buttonPrev.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                viewFlipper.setInAnimation(slide_in_right);
//                viewFlipper.setOutAnimation(slide_out_left);
//                viewFlipper.showPrevious();
//            }
//        });
//
//        buttonNext.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                viewFlipper.setInAnimation(slide_in_left);
//                viewFlipper.setOutAnimation(slide_out_right);
//                viewFlipper.showNext();
//            }
//        });
//        ;
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        this.mDetector.onTouchEvent(event);
//        return super.onTouchEvent(event);
//    }
//
//    @Override
//    public boolean onDown(MotionEvent arg0) {
//        // TODO Auto-generated method stub
//        return false;
//    }
//
//    @Override
//    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//                           float velocityY) {
//        float sensitvity = 50;
//
//        if((e1.getX() - e2.getX()) > sensitvity){
//            viewFlipper.setInAnimation(slide_in_right);
//            viewFlipper.setOutAnimation(slide_out_left);
//            viewFlipper.showPrevious();
//            Toast.makeText(MainActivity.this,
//                    "Previous", Toast.LENGTH_SHORT).show();
//        }else if((e2.getX() - e1.getX()) > sensitvity){
//            viewFlipper.setInAnimation(slide_in_left);
//            viewFlipper.setOutAnimation(slide_out_right);
//            viewFlipper.showNext();
//            Toast.makeText(MainActivity.this,
//                    "Next", Toast.LENGTH_SHORT).show();
//        }
//
//        return true;
//    }
//
//    @Override
//    public void onLongPress(MotionEvent e) {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
//                            float distanceY) {
//        // TODO Auto-generated method stub
//        return false;
//    }
//
//    @Override
//    public void onShowPress(MotionEvent e) {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public boolean onSingleTapUp(MotionEvent e) {
//        // TODO Auto-generated method stub
//        return false;
//    }
//
//}