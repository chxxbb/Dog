package com.example.administrator.rilegou.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.administrator.rilegou.R;
import com.example.administrator.rilegou.data.MapData;
import com.example.administrator.rilegou.data.Map_Lbs_ReturnJson_Data;
import com.example.administrator.rilegou.fragment.FindFragment;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by Chen on 2016/12/24.
 */
public class NewMessageActivity extends Activity {

    ImageView new_message_imageview;
    Button new_message_send_button;
    String imageStr;

    //定位服务
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_message_activity);

        imageStr = getIntent().getExtras().getString("image");

        new_message_imageview = (ImageView) findViewById(R.id.new_message_imageview);
        new_message_send_button = (Button) findViewById(R.id.new_message_send_button);

        ImageLoader.getInstance().displayImage("file://" + imageStr, new_message_imageview);
        new_message_send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoc();
            }
        });

        System.out.println(imageStr);

    }

    private void startLoc() {
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setIsNeedAddress(true);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null) {
                return;
            }

            OkHttpUtils
                    .post()
                    .url(MapData.ServiceUrl)
                    .addParams("ak", MapData.ServiceAk)
                    .addParams("latitude", "" + location.getLatitude())
                    .addParams("longitude", "" + location.getLongitude())
                    .addParams("coord_type", MapData.LocType)
                    .addParams("geotable_id", MapData.ServiceId)
                    .addParams("state", "1")
                    .addParams("message_id", "5")
                    .addParams("user_id", "1")
                    .addParams("image", "http://tupian.enterdesk.com/2014/mxy/02/11/4/4.jpg")
                    .addParams("address", location.getAddrStr())
                    .addParams("title", location.getAddress().street)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Toast.makeText(NewMessageActivity.this, "网络连接失败", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Gson gson = new Gson();
                            Map_Lbs_ReturnJson_Data map_lbs_returnJson_data = gson.fromJson(response, Map_Lbs_ReturnJson_Data.class);

                            if (map_lbs_returnJson_data.getStatus() == 0) {
                                System.out.println("云存储成功");
                            } else {
                                System.out.println("云存储失败,错误代码: " + map_lbs_returnJson_data.getStatus() + ",错误信息: " + map_lbs_returnJson_data.getMessage());
                            }


                        }
                    });

            mLocClient.stop();

        }


        public void onReceivePoi(BDLocation poiLocation) {
        }
    }


}
