package com.example.mkai.pry.settings;


import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import com.example.mkai.pry.R;
import com.example.mkai.pry.aleksey2093.GiveMeSettings;

public class AlertInfo extends DialogFragment implements View.OnClickListener {

    final String LOG_TAG = "myLogs";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Title!");
        View v = inflater.inflate(R.layout.alert_info, null);
        v.findViewById(R.id.btnYes).setOnClickListener(AlertInfo.this);
        v.findViewById(R.id.btnNo).setOnClickListener(AlertInfo.this);
        loadSetting(v);
        return v;
    }

    /**
     * Загружает настройки информации загружаемой из социальной сети
     */
    private void loadSetting(View v) {
        GiveMeSettings giveMeSettings = new GiveMeSettings();
        boolean[] mass = giveMeSettings.getInfo();
        ((Switch)v.findViewById(R.id.switchName)).setChecked(mass[0]);
        ((Switch)v.findViewById(R.id.switchPhoto)).setChecked(mass[1]);
        ((Switch)v.findViewById(R.id.switchBirthday)).setChecked(mass[2]);
        ((Switch)v.findViewById(R.id.switchCity)).setChecked(mass[3]);
        ((Switch)v.findViewById(R.id.switchWork)).setChecked(mass[4]);
        ((Switch)v.findViewById(R.id.switchPhone)).setChecked(mass[5]);
    }

    public void onClick(View v) {
        Log.d(LOG_TAG, "Dialog Info: " + ((Button) v).getText());
        switch (v.getId()) {
            case R.id.btnYes:
                saveInfo();
                break;
        }
        dismiss();
    }

    /**
     * Сохраняет настройки загружаемой из социальной сети информации
     */
    private void saveInfo()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                View v = getView();
                assert v != null;
                boolean[] mass = new boolean[6];
                mass[0] = ((Switch)v.findViewById(R.id.switchName)).isChecked();
                mass[1] =  ((Switch)v.findViewById(R.id.switchPhoto)).isChecked();
                mass[2] =  ((Switch)v.findViewById(R.id.switchBirthday)).isChecked();
                mass[3] =  ((Switch)v.findViewById(R.id.switchCity)).isChecked();
                mass[4] =  ((Switch)v.findViewById(R.id.switchWork)).isChecked();
                mass[5] =  ((Switch)v.findViewById(R.id.switchPhone)).isChecked();
                GiveMeSettings giveMeSettings = new GiveMeSettings();
                giveMeSettings.setInfo(mass);
            }
        });
        thread.setName("Thread AlertInfo saveInfo");
        thread.start();
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(LOG_TAG, "Dialog Info: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(LOG_TAG, "Dialog Info: onCancel");
    }
}
