package com.rabtman.rxmapdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.rabtman.rxmapmanager.RxMapManager;
import com.rabtman.rxmapmanager.baidu.BaiDuMapStrategy;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    Button start = findViewById(R.id.btn_start);
    Button stop = findViewById(R.id.btn_stop);
    Button get = findViewById(R.id.btn_get);

    //初始化
    RxMapManager.getInstance().init(new BaiDuMapStrategy() {
      @Override
      public LocationClient initClient() {
        LocationClientOption option = new LocationClientOption();
        // Hight_Accuracy高精度、Battery_Saving低功耗、Device_Sensors仅设备(GPS)
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
        option.setOpenGps(true);// 打开gps
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        option.disableCache(true);//禁用缓存定位
        option.setEnableSimulateGps(true);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要

        LocationClient locationClient = new LocationClient(getBaseContext());
        locationClient.setLocOption(option);

        return locationClient;
      }
    });

    start.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        RxMapManager.getInstance().onStart();
      }
    });

    stop.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        RxMapManager.getInstance().onStop();
      }
    });

    get.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        RxMapManager.getInstance().getLastLocation()
            .subscribe(new Consumer<BDLocation>() {
              @Override
              public void accept(BDLocation o) throws Exception {
                Log.d("MainActivity",
                    "get location-----lat:" + o.getLatitude() + "|lng:" + o.getLongitude());
              }
            }, new Consumer<Throwable>() {
              @Override
              public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
              }
            });
      }
    });

    //获取持续定位的信息
    RxMapManager.getInstance().requestLocation()
        .subscribe(new Consumer<BDLocation>() {
          @Override
          public void accept(BDLocation o) throws Exception {
            Log.d("MainActivity", "lat:" + o.getLatitude() + "|lng:" + o.getLongitude());
          }
        });
  }

}
