package com.example.mkai.pry.settings;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.mkai.pry.R;
import com.example.mkai.pry.aleksey2093.GiveMeSettings;

public class AlertSocial extends DialogFragment implements View.OnClickListener {

    final String LOG_TAG = "myLogs";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Title!");
        View v = inflater.inflate(R.layout.alert_social, null);
        v.findViewById(R.id.btnYes).setOnClickListener(AlertSocial.this);
        v.findViewById(R.id.btnNo).setOnClickListener(AlertSocial.this);
        loadSocial(v);
        return v;
    }

    public void onClick(View v) {
        Log.d(LOG_TAG, "Dialog Social: " + ((Button) v).getText());
        switch (v.getId())
        {
            case R.id.btnYes:
                saveSocial();
                break;
        }
        dismiss();
    }

    /**
     * Загружает настройки источников информациии
     */
    private void loadSocial(View v)
    {
        GiveMeSettings giveMeSettings = new GiveMeSettings();
        ((RadioButton)v.findViewById(R.id.radioButton2)).setChecked(giveMeSettings.getSourceData());
        ((RadioButton)v.findViewById(R.id.radioButton)).setChecked(!giveMeSettings.getSourceData());
        /*if (giveMeSettings.getSourceData())
            ((RadioButton)v.findViewById(R.id.radioButton2)).setChecked(true);
        else
            ((RadioButton)v.findViewById(R.id.radioButton)).setChecked(true);*/
    }

    /**
     * Сохраняет настройки источников информации
     */
    private void saveSocial()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                GiveMeSettings giveMeSettings = new GiveMeSettings();
                View v = getView();
                assert v != null;
                if (((RadioButton)v.findViewById(R.id.radioButton2)).isChecked())
                    giveMeSettings.setSourceData(true);
                else
                    giveMeSettings.setSourceData(false);
            }
        });
        thread.setName("Thread AlertSocial saveSocial");
        thread.start();
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(LOG_TAG, "Dialog Social: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(LOG_TAG, "Dialog Social: onCancel");
    }
}
