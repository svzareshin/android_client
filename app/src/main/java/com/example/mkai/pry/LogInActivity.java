package com.example.mkai.pry;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.mkai.pry.aleksey2093.GiveMeSettings;
import com.example.mkai.pry.aleksey2093.ListenResultFromServer;
import com.example.mkai.pry.subscription.SubscriptionActivity;

public class LogInActivity extends Activity implements OnClickListener {
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        GiveMeSettings giveMeSettings = new GiveMeSettings(this, getSharedPreferences("pry_settings", Context.MODE_PRIVATE));
        String tmp = giveMeSettings.getLpkString(true);
        ((EditText) this.findViewById(R.id.input_login)).setText(tmp);
        tmp = giveMeSettings.getLpkString(false);
        ((EditText) this.findViewById(R.id.input_password)).setText(tmp);
    }

    /**
     * Проверка на наличие подключения к Интернету
     */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Вывод местного MessageBox
     *
     * @title - текст, что пойдёт в заглавие
     * @text - текст самого сообщения
     */
    public void showMsgBox(String title, String text) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setTitle(title);
        dlgAlert.setMessage(text);
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //finish();
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    @Override
    public void onClick(View v) {
        /*Вот так можно вызвать проверку соединения. По ходу склейки будет видно, где его стоит применить
        if (!isOnline())
        {
            showMsgBox("Error","No Internet connection");
            return;
        }*/
        switch (v.getId()) {
            case R.id.btn_login:
                saveLPK_and_StartListen();
                Intent intent = new Intent(LogInActivity.this, SubscriptionActivity.class);
                startActivity(intent);
            default:
                break;
        }
    }

    /**
     * Сохранение логина и пароля
     */
    private void saveLPK_and_StartListen() {
        final String login = ((EditText) this.findViewById(R.id.input_login)).getText().toString();
        final String password = ((EditText) this.findViewById(R.id.input_password)).getText().toString();
        CheckBox checkBox = (CheckBox) this.findViewById(R.id.checkBox);
        if (checkBox.isChecked()) {
            new GiveMeSettings().setLpkString(login, password);
        } else {
            new GiveMeSettings().setLpkString("", "");
        }
    }
}
