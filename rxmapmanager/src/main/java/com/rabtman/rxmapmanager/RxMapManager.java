package com.rabtman.rxmapmanager;

import io.reactivex.Flowable;

public class RxMapManager {

  private static volatile RxMapManager sInst = null;
  private static volatile BaseMapStrategy mStrategy;

  public static RxMapManager getInstance() {
    RxMapManager inst = sInst;
    if (inst == null) {
      synchronized (RxMapManager.class) {
        inst = sInst;
        if (inst == null) {
          inst = new RxMapManager();
          sInst = inst;
        }
      }
    }
    return inst;
  }

  public void init(BaseMapStrategy strategy) {
    if (mStrategy != null) {
      destory();
    }
    mStrategy = strategy;
  }

  public Flowable getLastLocation() {
    return mStrategy.getLastLocation();
  }

  public Flowable requestLocation() {
    return mStrategy.requestLocation();
  }

  public void onStart() {
    mStrategy.onStart();
  }

  public void onStop() {
    mStrategy.onStop();
  }

  public void destory() {
    mStrategy.destory();
  }
}
