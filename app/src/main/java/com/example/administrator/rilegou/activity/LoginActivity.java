package com.example.administrator.rilegou.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.example.administrator.rilegou.R;
import com.example.administrator.rilegou.data.ActivityData;
import com.example.administrator.rilegou.data.MapData;
import com.example.administrator.rilegou.utils.AssetsCopyTOSDcard;

import java.io.File;

public class LoginActivity extends AppCompatActivity {

    Button login_button;
    TextView registered, login_find_password_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        ActivityData.LOGIN_ACTIVITY = this;

        //申请权限
        startPermissionsActivity();

        //自定义地图准备
        initMapStyle();


        login_button = (Button) findViewById(R.id.login_button);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        registered = (TextView) findViewById(R.id.login_registered);
        registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisteredActivity.class);
                startActivity(intent);
            }
        });

        login_find_password_textview = (TextView) findViewById(R.id.login_find_password_textview);
        login_find_password_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FindPasswordActivity.class);
                startActivity(intent);
            }
        });

        startPermissionsActivity();


    }

    /**
     * 把assets里面的样式文件复制到SD卡,以便设置
     */
    private void initMapStyle() {
        //自定义地图,把assets里面的样式文件复制到SD卡
        String appPath = "item/custom.txt";
        MapData.path = Environment.getExternalStorageDirectory().toString() + "/" + appPath;

        if (!fileIsExists(MapData.path)) {
            AssetsCopyTOSDcard assetsCopyTOSDcard = new AssetsCopyTOSDcard(getApplicationContext());
            assetsCopyTOSDcard.AssetToSD(appPath, MapData.path);
        } else {
            File file = new File(MapData.path);
            file.delete();
            AssetsCopyTOSDcard assetsCopyTOSDcard = new AssetsCopyTOSDcard(getApplicationContext());
            assetsCopyTOSDcard.AssetToSD(appPath, MapData.path);
            file = null;
        }
    }


    //    添加权限 先判断是否是Android6.0
    private void startPermissionsActivity() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_SYNC_SETTINGS,
                        Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET}, 0);
            }
        }
    }

    /**
     * 判断文件是否已经存在
     *
     * @param file 目标文件地址
     * @return 文件是否存在
     */
    public boolean fileIsExists(String file) {
        try {
            File f = new File(file);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;
    }
}
