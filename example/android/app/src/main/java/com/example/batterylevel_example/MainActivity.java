package com.example.batterylevel_example;

import android.os.Bundle;
import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //Step1: 全局插件的入口注册(这只是一个抽象类,用于集中管理多个插件的注册),在`anroid`应用的程序入口注册flutter插件.
    GeneratedPluginRegistrant.registerWith(this);
  }
}
