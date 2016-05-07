package com.example.mkai.pry;

import android.content.Context;
import android.graphics.Bitmap;
//import android.hardware.camera2.params.Face;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.*;

/**
 * Created by Антон on 05.04.2016.
 */
public class FaceOverlayView extends ImageView {

    private Bitmap mBitmap;
    private SparseArray<Face> mFaces;
    private int chX =-1;
    private int chY =-1;
    private int chFace = -1;
    double scaleX;
    double scaleY;
    public Bitmap getmBitmap()
    {
        return mBitmap;
    }

    public Bitmap getRectBitmap(int x, int y, int w, int h)
    {
        return Bitmap.createBitmap(mBitmap, x, y, w, h);
    }

    public FaceOverlayView(Context context) {
        this(context, null);
    }

    public FaceOverlayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FaceOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBitmap( Bitmap bitmap ) {
        mBitmap = bitmap;

        FaceDetector detector = new FaceDetector.Builder( getContext() )
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.FAST_MODE)
                .build();

        if (!detector.isOperational()) {
            //Handle contingency
        } else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            mFaces = detector.detect(frame);
            detector.release();
        }
        invalidate();
    }

    public void chooseFace(int x, int y)
    {
        //chFace = 3;
        chX = x;
        chY = y;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if ((mBitmap != null) && (mFaces != null)) {
            drawBitmap(canvas);
            drawFaceBox(canvas);
        }
    }

    private void drawBitmap( Canvas canvas ) {
        double viewWidth = canvas.getWidth();
        double viewHeight = canvas.getHeight();
        double imageWidth = mBitmap.getWidth();
        double imageHeight = mBitmap.getHeight();
        scaleX = viewWidth / imageWidth;
        scaleY = viewHeight / imageHeight;

        Rect destBounds = new Rect( (int)((viewWidth/2)-(imageWidth/2)*scaleX), (int)((viewHeight/2)-(imageHeight/2)*scaleY), (int) ( imageWidth * scaleX ), (int) ( imageHeight * scaleY ) );
        canvas.drawBitmap(mBitmap, null, destBounds, null);
    }

    private void drawFaceBox(Canvas canvas) {
        //paint should be defined as a member variable rather than
        //being created on each onDraw request, but left here for
        //emphasis.
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);


        float left = 0;
        float top = 0;
        float right = 0;
        float bottom = 0;
        boolean flagPic = false;
        for( int i = 0; i < mFaces.size(); i++ ) {
            Face face = mFaces.valueAt(i);

            left = (float) ( face.getPosition().x * scaleX );
            top = (float) ( face.getPosition().y * scaleY );
            right = (float) scaleX * ( face.getPosition().x + face.getWidth() );
            bottom = (float) scaleY * ( face.getPosition().y + face.getHeight() );
            if(flagPic == false && chX >= left && chX <= right && chY >= top && chY <= bottom) {
                if(chFace==i)
                {
                    // Здесь мы получаем итоговое изображение и передаём туда, куда нам нужно
                    // пока загружаем в наш же контрол, далее творим что хотим)
                    this.setBitmap(getRectBitmap((int)face.getPosition().x, (int)face.getPosition().y, (int)face.getWidth() , (int)face.getHeight()));
                }
                chFace = i;
                flagPic = true;
            }
            if(i == chFace)
                paint.setColor(Color.RED);
            canvas.drawRect( left, top, right, bottom, paint );

            paint.setColor(Color.GREEN);
        }
    }

}