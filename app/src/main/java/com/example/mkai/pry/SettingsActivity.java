package com.example.mkai.pry;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity {
    final String LOG_TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Получаем экземпляр элемента Spinner
        //final Spinner spinner = (Spinner)findViewById(R.id.spinnerEncrypt);

// Настраиваем адаптер
//        ArrayAdapter<?> adapter =
//                ArrayAdapter.createFromResource(this, R.array.encrypts, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Вызываем адаптер
       // spinner.setAdapter(adapter);
        final ListView settingsListView = (ListView) findViewById(R.id.settingsListView);
        // обрабатывает нажатие на пункт списка
        settingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                int pos = (int) id;
                switch (pos) {
                    case 0:
                        AlertEncrypt dialog0 = new AlertEncrypt();
                        dialog0.show(getFragmentManager(), "dialog0");
                        break;
                    case 1:
                        AlertSocial dialog1 = new AlertSocial();
                        dialog1.show(getFragmentManager(), "dialog1");
                        break;
                    case 2:
                        AlertInfo dialog3 = new AlertInfo();
                        dialog3.show(getFragmentManager(), "dialog2");
                    default:
                        break;
                }
            }
        });

        //обрабатывает выделение пунктов списка (
        settingsListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                R.layout.set_item, getDataSet());
        settingsListView.setAdapter(adapter);

    }

    private String[] getDataSet() {

        String[] mDataSet = new String[4];
        mDataSet[0] = "Тип шифрования";
        mDataSet[1] = "Социальная сеть";
        mDataSet[2] = "Информация";
        mDataSet[3] = "Настройка запроса";
        return mDataSet;
    }

    public void onClick(View v) {

    }


}
