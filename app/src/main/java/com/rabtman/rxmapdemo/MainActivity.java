package com.rabtman.rxmapdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
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

  private Button start, stop, get, clear;
  private RadioGroup selectMap;
  private RadioButton selectBaidu, selectAmap;
  private TextView result;
  //当前选择的地图平台
  private int currentMap;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    selectMap = findViewById(R.id.map_group);
    selectBaidu = findViewById(R.id.rbtn_baidu);
    selectAmap = findViewById(R.id.rbtn_amap);
    start = findViewById(R.id.btn_start);
    stop = findViewById(R.id.btn_stop);
    get = findViewById(R.id.btn_get);
    clear = findViewById(R.id.btn_clear);
    result = findViewById(R.id.tv_result);

    selectMap.setOnCheckedChangeListener(new OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == currentMap) {
          return;
        }

        currentMap = checkedId;
        if (selectBaidu.getId() == checkedId) {
          result.append("  选择了：百度地图\n");
          useBaiDu();
        } else if (selectAmap.getId() == checkedId) {
          result.append("  选择了：高德地图\n");
          useAMap();
        }
      }
    });

    clear.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        result.setText("");
      }
    });

    //开始定位
    start.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        selectBaidu.setEnabled(false);
        selectAmap.setEnabled(false);
        RxMapManager.getInstance().onStart();
      }
    });

    //停止定位
    stop.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        selectBaidu.setEnabled(true);
        selectAmap.setEnabled(true);
        RxMapManager.getInstance().onStop();
      }
    });

    //获取最近一次定位信息
    get.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        RxMapManager.getInstance().getLastLocation()
            .subscribe(new Consumer<Object>() {
              @Override
              public void accept(Object o) throws Exception {
                if (o == null) {
                  result.append("【主动获取定位】 null\n");
                } else {
                  if (selectBaidu.getId() == currentMap) {
                    result.append("【主动获取定位】lat:" + ((BDLocation) o).getLatitude()
                        + "，lng:" + ((BDLocation) o).getLongitude() + "\n");
                  } else if (selectAmap.getId() == currentMap) {
                    result.append("【主动获取定位】lat:" + ((AMapLocation) o).getLatitude()
                        + "，lng:" + ((AMapLocation) o).getLongitude() + "\n");
                  }
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

    init();
  }


  private void init() {
    currentMap = selectBaidu.getId();
    useBaiDu();
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

    //接收定位信息
    RxMapManager.getInstance().requestLocation()
        .subscribe(new Consumer<AMapLocation>() {
          @Override
          public void accept(AMapLocation o) throws Exception {
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

  @Override
  protected void onDestroy() {
    RxMapManager.getInstance().destory();
    super.onDestroy();
  }
}
