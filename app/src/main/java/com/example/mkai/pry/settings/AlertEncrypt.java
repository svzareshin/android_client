package com.example.mkai.pry.settings;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.mkai.pry.R;
import com.example.mkai.pry.aleksey2093.GiveMeSettings;


public class AlertEncrypt extends DialogFragment implements OnClickListener{

    final String LOG_TAG = "myLogs";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Title!");
        View v = inflater.inflate(R.layout.alert_encrypt, null);
        v.findViewById(R.id.btnYes).setOnClickListener(AlertEncrypt.this);
        v.findViewById(R.id.btnNo).setOnClickListener(AlertEncrypt.this);
        loadEncrypt(v);
        return v;
    }

    @Override
    public void onClick(View v) {
        Log.d(LOG_TAG, "Dialog Encrypt: " + ((Button) v).getText());
        switch (v.getId()) {
            case R.id.btnYes:
                saveEncrypt();
                break;
        }
        dismiss();
    }

    /**
     * Загружает настройки шифрования
     */
    private void loadEncrypt(View v)
    {
        GiveMeSettings giveMeSettings = new GiveMeSettings();
        switch (giveMeSettings.getEncryption()) {
            case 0:
                RadioButton radioButton1 = (RadioButton) v.findViewById(R.id.radioButton2);
                radioButton1.setChecked(true);
                break;
            case 1:
                RadioButton radioButton2 = (RadioButton) v.findViewById(R.id.radioButton);
                radioButton2.setChecked(true);
                break;
            case 2:
                RadioButton radioButton3 = (RadioButton) v.findViewById(R.id.radioButton3);
                radioButton3.setChecked(true);
                break;
        }
    }

    /**
     * Сохранение установленных настроек шифрования
     */
    private void saveEncrypt() {
       Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                View v = getView();
                GiveMeSettings giveMeSettings = new GiveMeSettings();
                String tmp = "null";
                assert v != null;
                RadioButton radioButton = (RadioButton) v.findViewById(R.id.radioButton2);
                if (radioButton.isChecked())
                    tmp = (radioButton.getText().toString());
                else {
                    radioButton = (RadioButton) v.findViewById(R.id.radioButton);
                    if (radioButton.isChecked())
                        tmp = (radioButton.getText().toString());
                    else {
                        radioButton = (RadioButton) v.findViewById(R.id.radioButton3);
                        if (radioButton.isChecked())
                            tmp = (radioButton.getText().toString());
                    }
                }
                giveMeSettings.setEncryption(tmp);
            }
        });
        thread.setName("Thread AlertEncrypt saveEncrypt");
        thread.start();
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(LOG_TAG, "Dialog Encrypt: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(LOG_TAG, "Dialog Encrypt: onCancel");
    }
}
