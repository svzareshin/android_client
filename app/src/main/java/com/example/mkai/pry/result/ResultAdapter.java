package com.example.mkai.pry.result;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mkai.pry.R;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class ResultAdapter extends BaseAdapter {
    private Context context;
    private List<PersonDescriptor> items;
    private int widthDisplay;

    public ResultAdapter(List<PersonDescriptor> items, Context context) {
        super();
        this.items = items;
        this.context = context;
        this.widthDisplay = 30;
    }

    /**
     * Создание адаптера
     * @param items список данных
     * @param context контект
     * @param widthDisplay ширина фотографии
     */
    public ResultAdapter(List<PersonDescriptor> items, Context context, int widthDisplay)
    {
        super();
        this.items = items;
        this.context = context;
        this.widthDisplay = widthDisplay;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public PersonDescriptor getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = mInflater.inflate(R.layout.result_item, null);
        }
        final ImageView ivPhoto = (ImageView) v.findViewById(R.id.photo);
        final TextView tvName = (TextView) v.findViewById(R.id.name);
        final TextView tvBirthday = (TextView) v.findViewById(R.id.birthday);
        final TextView tvCity = (TextView) v.findViewById(R.id.city);

        final String photo = items.get(position).getPhoto();
        final String name = items.get(position).getName();
        final String birthday = items.get(position).getBirthday();
        final String city = items.get(position).getCity();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final URL url = new URL(photo);
                    final Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
                    ivPhoto.post(new Runnable() {
                        @Override
                        public void run() {
                            ivPhoto.setMaxWidth(30);
                            ivPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            ivPhoto.setImageBitmap(bitmap);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        tvName.setText(name);
        tvBirthday.setText(birthday);
        tvCity.setText(city);
        return v;
    }
}
