package com.example.mkai.pry.subscription;

import java.util.ArrayList;
import java.util.List;

public class Subscription {
    String subName;
    private List<Subscription> subs;

    Subscription(String subName) {
        this.subName = subName;
    }

    private void initializeData() {
        subs = new ArrayList<>();
        subs.add(new Subscription("Emma Wilson"));
        subs.add(new Subscription("Lavery Maiss"));
        subs.add(new Subscription("Lillie Watts"));
    }
}