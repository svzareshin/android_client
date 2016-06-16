package com.example.mkai.pry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.mkai.pry.subscription.SubscriptionActivity;

public class LogInActivity extends Activity implements OnClickListener {
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
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
     * @title - текст, что пойдёт в заглавие
     * @text - текст самого сообщения
     */
    public void showMsgBox(String title,String text)
    {
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
                //Call SUbscriptionActivity
                Intent intent = new Intent(LogInActivity.this, SubscriptionActivity.class);
                startActivity(intent);
            default:
                break;
        }
    }
}
