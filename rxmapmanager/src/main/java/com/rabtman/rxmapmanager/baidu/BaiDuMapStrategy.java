package com.rabtman.rxmapmanager.baidu;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.rabtman.rxmapmanager.BaseMapStrategy;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * @author Rabtman
 * 百度定位策略
 */
public abstract class BaiDuMapStrategy extends BaseMapStrategy<BDLocation, LocationClient> {

  private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {
    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
      if (!mapProcessor.hasSubscribers()) {
        return;
      }
      mapProcessor.onNext(bdLocation);
    }
  };

  public BaiDuMapStrategy() {
    super();
    mClient.registerLocationListener(mListener);
  }

  @Override
  public Flowable<BDLocation> getLastLocation() {
    BDLocation location = mClient.getLastKnownLocation();
    return location == null ? Flowable.<BDLocation>empty() : Single.just(location).toFlowable();
  }

  @Override
  public Flowable<BDLocation> requestLocation() {
    return mapProcessor;
  }

  @Override
  public synchronized void destory() {
    if (mClient != null) {
      if (mClient.isStarted()) {
        mClient.stop();
      }
      mClient.unRegisterLocationListener(mListener);
      mClient = null;
    }
  }

  @Override
  public synchronized void onStart() {
    validClient();
    if (!mClient.isStarted()) {
      mClient.start();
    }
  }

  @Override
  public synchronized void onStop() {
    validClient();
    if (mClient.isStarted()) {
      mClient.stop();
    }
  }
}
