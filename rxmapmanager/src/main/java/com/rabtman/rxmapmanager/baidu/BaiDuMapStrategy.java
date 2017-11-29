package com.rabtman.rxmapmanager.baidu;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.rabtman.rxmapmanager.BaseMapStrategy;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * @author Rabtman
 */
public abstract class BaiDuMapStrategy extends BaseMapStrategy<BDLocation, LocationClient> {

  public BaiDuMapStrategy() {
    super();
    BDAbstractLocationListener mListener = new BDAbstractLocationListener() {
      @Override
      public void onReceiveLocation(BDLocation bdLocation) {
        mapProcessor.onNext(bdLocation);
      }
    };
    mClient.registerLocationListener(mListener);
  }

  @Override
  public Flowable<BDLocation> getLastLocation() {
    return Single.just(mClient.getLastKnownLocation()).toFlowable();
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
      mClient = null;
    }
  }

  @Override
  public synchronized void onStart() {
    if (mClient != null && !mClient.isStarted()) {
      mClient.start();
    }
  }

  @Override
  public synchronized void onStop() {
    if (mClient != null && mClient.isStarted()) {
      mClient.stop();
    }
  }
}
