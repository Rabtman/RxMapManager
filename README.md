# RxMapManager

easy to use map by rxjava.

## Features

1. rxjava2
2. integrate baidu map and amap
3. location-info can be received anywhere

## Download

Maven:

```xml
<dependency>
  <groupId>com.rabtman.rxmapmanager</groupId>
  <artifactId>rxmapmanager</artifactId>
  <version>0.1.0</version>
  <type>pom</type>
</dependency>
```

or Gradle:

```groovy
compile 'com.rabtman.rxmapmanager:rxmapmanager:0.1.0'
```

## How to use

Init rxmapmanager:

```
//use baidu map
RxMapManager.getInstance().init(new BaiDuMapStrategy() {
      @Override
      public LocationClient initClient() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(5000);
        option.setOpenGps(true);
        option.setIsNeedAddress(true);
        option.setNeedDeviceDirect(true);
        LocationClient locationClient = new LocationClient(getBaseContext());
        locationClient.setLocOption(option);
        return locationClient;
      }
    });
    
//or use amap
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
```


Before receiving location-info, you need to start:

```
RxMapManager.getInstance().onStart();
```

Keep receiving location-info:

```
RxMapManager.getInstance().requestLocation();
```

Get the last location-info:

```
RxMapManager.getInstance().getLastLocation();
```


Need to stop receiving, you can:

```
RxMapManager.getInstance().onStop();
```

Release resources:

```
RxMapManager.getInstance().destory();
```


## Preview

![](https://github.com/Rabtman/RxMapManager/raw/master/screenshots/rxmapdemo.gif)

## License

```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

