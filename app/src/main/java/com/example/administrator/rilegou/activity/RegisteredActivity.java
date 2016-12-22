package com.example.administrator.rilegou.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.administrator.rilegou.R;
import com.example.administrator.rilegou.data.ActivityData;

/**
 * Created by Chen on 2016/12/21.
 */
public class RegisteredActivity extends Activity {

    Button registered_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registered);


        registered_button = (Button) findViewById(R.id.registered_button);
        registered_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisteredActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                ActivityData.LOGIN_ACTIVITY.finish();
            }
        });

    }
}
