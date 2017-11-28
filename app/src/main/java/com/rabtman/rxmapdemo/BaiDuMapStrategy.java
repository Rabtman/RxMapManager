package com.rabtman.rxmapdemo;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Single;

/**
 * @author Rabtman
 */
public class BaiDuMapStrategy implements BaseMapStrategy<BDLocation, LocationClient> {

  private LocationClient mClient;

  public BaiDuMapStrategy(LocationClient client){
    this.mClient = client;
  }

  @Override
  public Flowable<BDLocation> getLastLocation() {
    return Single.just(mClient.getLastKnownLocation()).toFlowable();
  }

  @Override
  public Flowable<BDLocation> requestLocation() {
    return Flowable.create(new FlowableOnSubscribe<BDLocation>() {
      @Override
      public void subscribe(final FlowableEmitter<BDLocation> emitter) throws Exception {
        mClient.registerLocationListener(new BDAbstractLocationListener() {
          @Override
          public void onReceiveLocation(BDLocation bdLocation) {
            if(emitter.isCancelled()){
              emitter.onComplete();
            }
            emitter.onNext(bdLocation);
          }
        });
        synchronized (BaiDuMapStrategy.class){
          mClient.start();
        }
      }
    }, BackpressureStrategy.BUFFER);
  }

  @Override
  public void setOption(LocationClient client) {
    if(client == null){
      throw new RuntimeException("option can not be null");
    }
    this.mClient = client;
  }

  @Override
  public void destory() {

  }
}
