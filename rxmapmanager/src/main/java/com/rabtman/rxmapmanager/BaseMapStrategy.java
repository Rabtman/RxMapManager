package com.rabtman.rxmapmanager;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * @author Rabtman
 * @param <L> Location 定位信息类型
 * @param <C> Client 定位客户端类型
 */
public abstract class BaseMapStrategy<L, C> {

  //定位信息处理源
  protected final FlowableProcessor<L> mapProcessor;
  //定位客户端
  protected C mClient;

  public BaseMapStrategy() {
    this.mClient = initClient();
    validClient();
    mapProcessor = PublishProcessor.create();
  }

  /**
   * 校验client
   */
  protected void validClient() {
    if (mClient == null) {
      throw new RuntimeException("client can not be null");
    }
  }

  /**
   * 获取最后一次定位的信息
   */
  public abstract Flowable<L> getLastLocation();

  /**
   * 申请定位，获取定位信息
   */
  public abstract Flowable<L> requestLocation();

  /**
   * 设置定位配置信息
   */
  public abstract C initClient();

  /**
   * 开始定位
   */
  public abstract void onStart();

  /**
   * 停止定位
   */
  public abstract void onStop();

  /**
   * 销毁定位，释放资源
   */
  public abstract void destory();
}
