#import "BatterylevelPlugin.h"
#import "DemoViewFactory.h"

@implementation BatterylevelPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"flutter.io/batterylevel"
            binaryMessenger:[registrar messenger]];
  BatterylevelPlugin* instance = [[BatterylevelPlugin alloc] init];
    
    DemoViewFactory* factory = [[DemoViewFactory alloc]initWithRegistrar:registrar];
    [registrar registerViewFactory:factory withId:@"flutter.io/batterylevel_view"];
  [registrar addMethodCallDelegate:instance channel:channel];
    
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
  if ([@"getPlatformVersion" isEqualToString:call.method]) {
    result([@"iOS " stringByAppendingString:[[UIDevice currentDevice] systemVersion]]);
  } else if ([@"getBatteryLevel" isEqualToString:call.method]) {
      int batterylevel = (int)[UIDevice currentDevice].batteryLevel;
      NSNumber* number = [NSNumber numberWithInt:batterylevel];
//      NSString* str = [NSString stringWithFormat:@"%d", batterylevel];
      result(number);
  } else {
    result(FlutterMethodNotImplemented);
  }
}

@end
