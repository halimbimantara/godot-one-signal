# godot-one-signal
apply onesignal notification in godot | Android
you can receive notifications on your android mobile godot application
_________
Signin and create new Apps  on <b>Onesignal</b>
----------
1. Install <strong>Android Build Template</strong>
2. on <strong>Export</strong> check Custom Build
3. Change 
```sh
android/build/config.gradle
```
edit Compile SDK to 31 like this
```gradle
ext.versions = [
    androidGradlePlugin: '7.0.3',
    compileSdk         : 31,
    minSdk             : 19, // Also update 'platform/android/java/lib/AndroidManifest.xml#minSdkVersion' & 'platform/android/export/export_plugin.cpp#DEFAULT_MIN_SDK_VERSION'
    targetSdk          : 31, // Also update 'platform/android/java/lib/AndroidManifest.xml#targetSdkVersion' & 'platform/android/export/export_plugin.cpp#DEFAULT_TARGET_SDK_VERSION'
    buildTools         : '31.0.0',
    kotlinVersion      : '1.5.10',
    fragmentVersion    : '1.3.6',
    javaVersion        : 11,
    ndkVersion         : '21.4.7075529' // Also update 'platform/android/detect.py#get_project_ndk_version()' when this is updated.

]
```