package com.example.mkai.pry.settings;


import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mkai.pry.R;

public class AlertInfo extends DialogFragment implements View.OnClickListener {

    final String LOG_TAG = "myLogs";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Title!");
        View v = inflater.inflate(R.layout.alert_info, null);
        v.findViewById(R.id.btnYes).setOnClickListener(AlertInfo.this);
        v.findViewById(R.id.btnNo).setOnClickListener(AlertInfo.this);
        return v;
    }

    public void onClick(View v) {
        Log.d(LOG_TAG, "Dialog Info: " + ((Button) v).getText());
        dismiss();
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
