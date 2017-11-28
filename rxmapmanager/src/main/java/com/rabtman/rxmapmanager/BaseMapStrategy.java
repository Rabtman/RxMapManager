package com.rabtman.rxmapmanager;

import io.reactivex.Flowable;

/**
 * @author Rabtman
 */
public interface BaseMapStrategy<L, C> {

  /**
   * 获取最后一次定位的信息
   *
   * @return observable that emit last location
   */
  Flowable<L> getLastLocation();

  /**
   * 申请定位，获取定位信息
   *
   * @return observable that emit location
   */
  Flowable<L> requestLocation();

  /**
   * 设置定位配置信息
   */
  void setOption(C client);

  /**
   * 销毁定位，释放资源
   */
  void destory();
}
