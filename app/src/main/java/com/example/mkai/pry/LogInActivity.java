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

    @Override
    public void onClick(View v) {
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
