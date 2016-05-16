package com.example.mkai.pry;


import android.graphics.drawable.Drawable;
import android.media.Image;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URI;

public class PersonDescriptor {
    private String photo;
    private String name;
    private String city;
    private String birthday;

   /* public PersonDescriptor() {
        name.setText("Вася Пупкин");
        birthday.setText("13 марта");
        city.setText("Москва");
    }*/
    public PersonDescriptor() {
        photo = new String("https://pp.vk.me/c405723/v405723154/9af9/v_JbvY12RnM.jpg");
        name = new String("Вася Пупкин");
        birthday = new String("13 марта");
        city = new String("Москва");
    }

    public String getName() {
        return name;
    }
    public String getBirthday() {
        return birthday;
    }
    public String getCity() {
        return city;
    }

    public String getPhoto() {
        return photo;
    }
}
