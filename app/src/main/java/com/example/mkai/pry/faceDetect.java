package com.example.mkai.pry;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;

public class faceDetect extends AppCompatActivity {

    private FaceOverlayView mFaceOverlayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face_detect);

        //ImageView iv;// = (ImageView) findViewById(R.id.imgPhoto);
        Bitmap bitmap = null;

        Intent intent = getIntent();
        String status = intent.getStringExtra("stts");

        if(status.contentEquals("bmp")) {
            bitmap = (Bitmap) intent.getExtras().getParcelable("bmp");
        //    iv.setImageBitmap(bmp);
        }

        if(status.contentEquals("uri"))
        {
            Uri u = (Uri)intent.getExtras().getParcelable("uri");
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), u);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mFaceOverlayView = (FaceOverlayView) findViewById( R.id.face_overlay );
        mFaceOverlayView.setBitmap(bitmap);
        mFaceOverlayView.setOnTouchListener(new ClickFace());// setOnTouchListener(this);
        //mFaceOverlayView.setOnTouchListener(this);
    }

    class ClickFace implements View.OnTouchListener {
        //@Override
        public boolean onTouch(View view, MotionEvent event) {
            mFaceOverlayView = (FaceOverlayView) findViewById( R.id.face_overlay );
            int x = (int) event.getX();
            int y = (int) event.getY();
            Log.v("Mytag",  Integer.toString(x) + " " +  Integer.toString(y));//Для того, чтобы можно было отслеживать эти действия, записываем всю информацию о них в лог.
            mFaceOverlayView.chooseFace(x, y);
            return false;
        }
    }

}
