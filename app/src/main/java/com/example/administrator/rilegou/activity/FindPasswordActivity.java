package com.example.administrator.rilegou.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.administrator.rilegou.R;

/**
 * Created by Chen on 2016/12/21.
 */
public class FindPasswordActivity extends Activity {

    Button find_password_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        find_password_button = (Button) findViewById(R.id.find_password_button);
        find_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
