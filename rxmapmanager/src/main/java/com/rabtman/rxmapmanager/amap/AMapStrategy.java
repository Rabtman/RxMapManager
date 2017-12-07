package com.rabtman.rxmapmanager.amap;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.rabtman.rxmapmanager.BaseMapStrategy;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * @author Rabtman 高德定位策略
 */
public abstract class AMapStrategy extends BaseMapStrategy<AMapLocation, AMapLocationClient> {

  private AMapLocationListener mListener = new AMapLocationListener() {
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
      if (!mapProcessor.hasSubscribers()) {
        return;
      }
      mapProcessor.onNext(aMapLocation);
    }
  };

  public AMapStrategy() {
    super();
    mClient.setLocationListener(mListener);
  }

  @Override
  public Flowable<AMapLocation> getLastLocation() {
    AMapLocation location = mClient.getLastKnownLocation();
    return location == null ? Flowable.<AMapLocation>empty() : Single.just(location).toFlowable();
  }

  @Override
  public Flowable<AMapLocation> requestLocation() {
    return mapProcessor;
  }

  @Override
  public synchronized void destory() {
    if (mClient != null) {
      mClient.stopLocation();
      mClient.unRegisterLocationListener(mListener);
      mClient.onDestroy();
      mClient = null;
    }
  }

  @Override
  public synchronized void onStart() {
    validClient();
    mClient.startLocation();
  }

  @Override
  public synchronized void onStop() {
    validClient();
    mClient.stopLocation();
  }
}
