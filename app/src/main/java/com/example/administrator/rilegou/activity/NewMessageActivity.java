package com.example.administrator.rilegou.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.administrator.rilegou.R;
import com.example.administrator.rilegou.data.MapData;
import com.example.administrator.rilegou.data.Map_Lbs_ReturnJson_Data;
import com.example.administrator.rilegou.data.QiNiuData.QiNiuKey;
import com.example.administrator.rilegou.data.QiNiuData.QiNiu_Json;
import com.example.administrator.rilegou.fragment.FindFragment;
import com.example.administrator.rilegou.utils.AssetsCopyTOSDcard;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import okhttp3.Call;

/**
 * Created by Chen on 2016/12/24.
 */
public class NewMessageActivity extends Activity implements View.OnClickListener {

    Button new_message_send_button;
    String imageStr;
    File file;

    //发表按钮的控件
    RelativeLayout new_message_sendmessage_relativelayout;

    //退出控件
    RelativeLayout new_message_exit;

    //图片
    ImageView new_message_content_imageview;

    //地点选择按钮
    RelativeLayout new_message_loc_relativelayout;

    //状态选择按钮
    RelativeLayout new_message_state_relativelayout;

    //定位服务
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();

    //七牛云服务相关
    //指定upToken, 强烈建议从服务端提供get请求获取, 这里为了掩饰直接指定key

    private UploadManager uploadManager;
    BDLocation location1;

    String imageUrl;

    //时间相关
    int month;
    int day;
    int hour;
    int min;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_message_activity);

        //获取当前时间
        initTime();

        //绑定控件/监听/准备数据
        find();

        startLoc();

    }

    /**
     * 关闭当前Activity的时候删除存在手机里面的临时照片
     */
    @Override
    protected void onPause() {
        if (file != null && fileIsExists(MapData.path)) {
            file.delete();
        }
        super.onPause();
    }

    /**
     * 绑定控件
     * 设置监听器
     * 准备数据
     */
    private void find() {
        imageStr = getIntent().getExtras().getString("image");
        file = (File) getIntent().getExtras().get("file");

        new_message_send_button = (Button) findViewById(R.id.new_message_send_button);

        //发表按钮的控件绑定监听
        new_message_sendmessage_relativelayout = (RelativeLayout) findViewById(R.id.new_message_sendmessage_relativelayout);
        new_message_sendmessage_relativelayout.setOnClickListener(this);

        //退出绑定
        new_message_exit = (RelativeLayout) findViewById(R.id.new_message_exit);
        new_message_exit.setOnClickListener(this);

        //设置图片
        new_message_content_imageview = (ImageView) findViewById(R.id.new_message_content_imageview);
        ImageLoader.getInstance().displayImage("file://" + imageStr, new_message_content_imageview);

        //地点选择按钮
        new_message_loc_relativelayout = (RelativeLayout) findViewById(R.id.new_message_loc_relativelayout);
        new_message_loc_relativelayout.setOnClickListener(this);

        //状态选择按钮
        new_message_state_relativelayout = (RelativeLayout) findViewById(R.id.new_message_state_relativelayout);
        new_message_state_relativelayout.setOnClickListener(this);

    }

    /**
     * 获取当前时间
     */
    private void initTime() {
        GregorianCalendar calendar = new GregorianCalendar();
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);
    }

    /**
     * 监听器设置
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_message_exit:
                finish();
                break;
            case R.id.new_message_sendmessage_relativelayout:
                qiniuUp();
                break;
            case R.id.new_message_loc_relativelayout:
                break;
            case R.id.new_message_state_relativelayout:
                break;
        }
    }

    /**
     * 七牛云上传
     * upkey 是文件上传后的文件名
     */
    private void qiniuUp() {
        Configuration config = new Configuration.Builder().zone(Zone.httpAutoZone).build();
        //new一个uploadManager类
        uploadManager = new UploadManager(config);

        File file = new File(imageStr);
        //设置上传后文件的key(文件名)
        String upkey = null;
        uploadManager.put(file, upkey, QiNiuKey.uptoken, new UpCompletionHandler() {    //设置上传属性并开始上传
            public void complete(String key, ResponseInfo rinfo, JSONObject response) {

                if (rinfo.isOK()) {     //判断七牛云是否上传成功 imageUrl为上传成功后的文件链接
                    Gson gson = new Gson();
                    QiNiu_Json Urlkey = gson.fromJson("" + response, QiNiu_Json.class);
                    imageUrl = "http://oip7xw0lg.bkt.clouddn.com/" + Urlkey.getKey();
                    System.out.println("url" + imageUrl);   //显示上传后文件的url

                    //拿到七牛云反馈的文件链接后,开始百度地图数据的上传
                    OkHttpUtils
                            .post()
                            .url(MapData.ServiceUrl)
                            .addParams("ak", MapData.ServiceAk)
                            .addParams("latitude", "" + location1.getLatitude())
                            .addParams("longitude", "" + location1.getLongitude())
                            .addParams("coord_type", MapData.LocType)
                            .addParams("geotable_id", MapData.ServiceId)
                            .addParams("state", "1")
                            .addParams("message_id", "5")
                            .addParams("user_id", "1")
                            .addParams("image", imageUrl)   //七牛云的图片文件地址
                            .addParams("address", location1.getAddrStr())
                            .addParams("title", location1.getAddress().street)
                            .addParams("time", month + "月" + day + "日" + hour + "点" + min + "分")
                            .addParams("content", "王八蛋老板日了狗")
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

                } else {
                    System.out.println("七牛云上传失败");
                }

            }
        }, new UploadOptions(null, "test-type", true, null, null));
    }

    /**
     * 进行定位
     * option.setIsNeedAddress(true) 设置在定位的时候获取地址数据
     */
    private void startLoc() {
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setIsNeedAddress(true);
        mLocClient.setLocOption(option);
        MapData.mLocClient = mLocClient;
        mLocClient.start();
    }


    /**
     * 定位成功后会返回BDLocation到这里,在这里执行相关操作
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null) {
                return;
            }

            location1 = location;

            System.out.println("新消息页面定位完成");

            //只定位一次
            mLocClient.stop();

        }


        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param file 目标文件
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
