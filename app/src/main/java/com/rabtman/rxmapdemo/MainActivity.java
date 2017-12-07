package com.rabtman.rxmapdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.rabtman.rxmapmanager.RxMapManager;
import com.rabtman.rxmapmanager.amap.AMapStrategy;
import com.rabtman.rxmapmanager.baidu.BaiDuMapStrategy;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

  private Button start, stop, get;
  private TextView result;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    start = findViewById(R.id.btn_start);
    stop = findViewById(R.id.btn_stop);
    get = findViewById(R.id.btn_get);
    result = findViewById(R.id.tv_result);

    //useAMap();
    useBaiDu();

    //接收定位信息
    RxMapManager.getInstance().requestLocation()
        .subscribe(new Consumer<BDLocation>() {
          @Override
          public void accept(BDLocation o) throws Exception {
            if (o == null) {
              result.append("【接收定位信息】 null\n");
            } else {
              result.append("【接收定位信息】lat:" + o.getLatitude()
                  + "，lng:" + o.getLongitude() + "\n");
            }
          }
        }, new Consumer<Throwable>() {
          @Override
          public void accept(Throwable throwable) throws Exception {
            throwable.printStackTrace();
          }
        });
  }

  //使用百度定位
  private void useBaiDu() {
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
                if (o == null) {
                  result.append("【主动获取定位】 null\n");
                } else {
                  result.append("【主动获取定位】lat:" + o.getLatitude()
                      + "，lng:" + o.getLongitude() + "\n");
                }
              }
            }, new Consumer<Throwable>() {
              @Override
              public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
              }
            });
      }
    });
  }

  //使用高德定位
  private void useAMap() {
    RxMapManager.getInstance().init(new AMapStrategy() {
      @Override
      public AMapLocationClient initClient() {
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setNeedAddress(true);
        option.setInterval(5000);

        AMapLocationClient client = new AMapLocationClient(getBaseContext());
        client.setLocationOption(option);

        return client;
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
            .subscribe(new Consumer<AMapLocation>() {
              @Override
              public void accept(AMapLocation o) throws Exception {
                if (o == null) {
                  result.append("【主动获取定位】 null\n");
                } else {
                  result.append("【主动获取定位】lat:" + o.getLatitude()
                      + "，lng:" + o.getLongitude() + "\n");
                }
              }
            }, new Consumer<Throwable>() {
              @Override
              public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
              }
            });
      }
    });
  }

}
