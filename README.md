# android\-xrecyclerview

## Features

1. 可以像 ListView 一样添加 Header 和 Footer 的 RecyclerView。
2. 支持 `OnItemClickListener` 事件。

## Gradle

[![](https://www.jitpack.io/v/wuzhendev/android-xrecyclerview.svg)](https://www.jitpack.io/#wuzhendev/android-xrecyclerview)

```
repositories {
    maven {
        url "https://www.jitpack.io"
    }
}

dependencies {
    compile 'com.github.wuzhendev:android-xrecyclerview:x.y.z'
}
```

## Attrs

```
<!-- RecyclerView 的 spanCount，如果是单列的指定为1，默认是单列的 -->
<attr name="xrv_spanCount" format="integer" />

<!-- 是否是瀑布流，只有当 wrv_rvSpanCount 大于1时才生效 -->
<attr name="xrv_staggered" format="boolean" />
```

## Sample

[Sample sources][2]

[Sample APK](https://github.com/wuzhendev/android-xrecyclerview/raw/master/assets/XRecyclerView_Demo_v1_0_0.apk)

## License

```
Copyright 2016 wuzhen

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

[1]: ./assets/1.jpg
[2]: ./samples
