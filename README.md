[![](https://jitpack.io/v/hannesa2/android-maps-extensions.svg)](https://jitpack.io/#hannesa2/android-maps-extensions)

Android Maps Extensions
=======================

Library extending capabilities of Google Maps Android API v2.  
While [Google Maps Android API v2](https://developers.google.com/maps/documentation/android-api/)
is a huge leap forward compared to its predecessor,
it lacks commonly used patterns like marker clustering.
This library aims to fill this gap by adding many useful features
and improving on the responsiveness issues of the official Google library.

Usage
=====

You may use any version of [Google Play Services](https://developer.android.com/google/play-services/index.html) from 3.2.65 (the last working on Android API 8) or above.

Gradle
------

```Groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

```Groovy
dependencies {
    implementation 'com.github.hannesa2:android-maps-extensions:$latest'
}
```

Origin developed by
===================

* Maciej Górski - <maciek.gorski@gmail.com>

License
=======

    Copyright (C) 2013 hannesa2

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

          http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
