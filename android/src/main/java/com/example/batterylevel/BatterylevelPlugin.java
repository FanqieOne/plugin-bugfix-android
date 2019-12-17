package com.example.batterylevel;

//android packages
import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.BatteryManager;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import com.example.batterylevel.AndroidViewFactory;



/** BatterylevelPlugin */
public class BatterylevelPlugin implements MethodCallHandler {
  /** Plugin registration. */

  static private Context applicationContext = null;
  public static void registerWith(Registrar registrar) {

      //step1: 注册MethodChannel用于和flutter通信, `registrar.messenger()`为MainActivity中绑定的BannaryMessager,name为对应的渠道标示。
    final MethodChannel battery_channel = new MethodChannel(registrar.messenger(), Constant.PLUGIN_BASIC_METHOD);

    //保存activity的currentContext实例,用于获取batteryManager
    applicationContext = registrar.context();
      //step2: 注册ViewFactory,用于接受来自flutter端的构造视图的指令
    registrar.platformViewRegistry().registerViewFactory(Constant.PLUGIN_PLAT_FORM_BASIC_VIEW_NAME, new AndroidViewFactory(registrar, registrar.messenger()));
      //step3: 绑定battery_channel的回调方法
      battery_channel.setMethodCallHandler(new BatterylevelPlugin());
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else if (call.method.equals("getBatteryLevel")){
      result.success(getBatteryLevel());
    } else {
      result.notImplemented();
    }
  }

  private int getBatteryLevel() {
    int batteryLevel = -1;
    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      BatteryManager batteryManager = (BatteryManager) applicationContext.getSystemService(applicationContext.BATTERY_SERVICE);
      batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
    } else {
      Intent intent = new ContextWrapper(applicationContext).
              registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
      batteryLevel = (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100) /
              intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
    }
    return batteryLevel;
  }
}

