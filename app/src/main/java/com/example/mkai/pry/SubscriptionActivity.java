package com.example.mkai.pry;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;


public class SubscriptionActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";
    Bitmap bmp;         // объект взятого изображения
    private static final int CAM_REQUEST = 1313;    // код реквеста камеры
    private static final int GALLERY_REQUEST = 1;   // код реквеста галлереи
    private static Uri mOutputFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String[] myDataset = getDataSet();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ListView subsListView = (ListView) findViewById(R.id.subsListView);
        // обрабатывает нажатие на пункт списка
        subsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.d(LOG_TAG, subsListView.getItemAtPosition(position).toString() + " itemClick: position = " + position + ", id = "
                        + id);
                Intent intent = new Intent(SubscriptionActivity.this, ResultActivity.class);
                startActivity(intent);
            }
        });

        //обрабатывает выделение пунктов списка (
        subsListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.d(LOG_TAG, "itemSelect: position = " + position + ", id = "
                  + id);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(LOG_TAG, "itemSelect: nothing");
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.sub_item, myDataset);
        subsListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private String[] getDataSet() {

        String[] mDataSet = new String[100];
        for (int i = 0; i < 100; i++) {
            mDataSet[i] = "item" + i;
        }
        return mDataSet;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_photo:
                Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Time time = new Time();
                time.setToNow();
                File checkDir = new File(Environment.getExternalStorageDirectory() + "/pry/Images");

                if(checkDir.exists()) {
                    Log.v(LOG_TAG, "directory exist");
                } else {
                    Log.v(LOG_TAG, "directory NOT exist");
                    checkDir.mkdirs();
                    Log.v(LOG_TAG, "directory create");
                }
                File file = new File(Environment.getExternalStorageDirectory()+"/pry/Images", Integer.toString(time.year) + Integer.toString(time.month) + Integer.toString(time.monthDay) + Integer.toString(time.hour) + Integer.toString(time.minute) + Integer.toString(time.second) + ".jpg");
                mOutputFileUri = Uri.fromFile(file);
                camIntent.putExtra(MediaStore.EXTRA_OUTPUT, mOutputFileUri);
                startActivityForResult(camIntent, CAM_REQUEST);
                break;
            default:
                break;
        }
    }

    // метод срабатывающий при получении резуьтата от камеры или галлереи
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Intent intent = new Intent(this, faceDetect.class);

        Uri u = null;
        // если ответ от камеры
        if (requestCode == CAM_REQUEST) {
            if (data != null) {
                if (data.hasExtra("data")) {
                    bmp = (Bitmap) data.getExtras().get("data");
                    // есть изображением в bmp
                    intent.putExtra("bmp", bmp);
                    intent.putExtra("stts", "bmp");
                }
            }else {
                intent.putExtra("uri", mOutputFileUri);
                intent.putExtra("stts", "uri");
            }
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQUEST) {
                u = (Uri) data.getData();
                intent.putExtra("uri", u);
                intent.putExtra("stts", "uri");
            }
        }
        startActivity(intent);
    }

    // событие для кнопки снимка
    class btnCameraClick implements Button.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Time time = new Time();
            time.setToNow();
            File checkDir = new File(Environment.getExternalStorageDirectory() + "/pry/Images");

            if(checkDir.exists()) {
                Log.v(LOG_TAG, "directory exist");
            } else {
                Log.v(LOG_TAG, "directory NOT exist");
                checkDir.mkdirs();
                Log.v(LOG_TAG, "directory create");
            }
            File file = new File(Environment.getExternalStorageDirectory()+"/pry/Images", Integer.toString(time.year) + Integer.toString(time.month) + Integer.toString(time.monthDay) + Integer.toString(time.hour) + Integer.toString(time.minute) + Integer.toString(time.second) + ".jpg");
            mOutputFileUri = Uri.fromFile(file);
            camIntent.putExtra(MediaStore.EXTRA_OUTPUT, mOutputFileUri);
            startActivityForResult(camIntent, CAM_REQUEST);
        }
    }

    class btnGalleryClick implements Button.OnClickListener {
        @Override
        public void onClick(View v) {
            // Этот код в кнопку выбор из галереи
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
        }
    }
}