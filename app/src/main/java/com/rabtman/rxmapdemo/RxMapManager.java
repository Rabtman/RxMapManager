package com.rabtman.rxmapdemo;

import io.reactivex.Flowable;

public class RxMapManager implements BaseMapStrategy {

  private BaseMapStrategy mStrategy;

  public RxMapManager(BaseMapStrategy strategy){
    setMapStrategy(strategy);
  }

  public void setMapStrategy(BaseMapStrategy mStrategy) {
    this.mStrategy = mStrategy;
  }

  @Override
  public Flowable getLastLocation() {
    return mStrategy.getLastLocation();
  }

  @Override
  public Flowable requestLocation() {
    return mStrategy.requestLocation();
  }

  @Override
  public void setOption(Object client) {
    mStrategy.setOption(client);
  }

  @Override
  public void destory() {
    mStrategy.destory();
  }
}
