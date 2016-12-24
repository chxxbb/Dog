package com.example.administrator.rilegou.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.administrator.rilegou.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Chen on 2016/12/24.
 */
public class NewMessageActivity extends Activity {

    ImageView new_message_imageview;
    String imageStr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_message_activity);

        imageStr = getIntent().getExtras().getString("image");

        new_message_imageview = (ImageView) findViewById(R.id.new_message_imageview);

        ImageLoader.getInstance().displayImage("file://" + imageStr, new_message_imageview);

        System.out.println(imageStr);

    }
}
