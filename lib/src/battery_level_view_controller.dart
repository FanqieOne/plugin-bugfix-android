part of batterylevel; 

class BatteryLevelViewController {
   StreamController _flutterToEvaluteStream;
   final MethodChannel _battery_view_channel;
   final EventChannel _battery_view_event_channel; 
   StreamSubscription _eventSinkStreamSubscription;
  
   BatteryLevelViewController({@required int viewId, StreamController flutterToEvaluteStream})
   : _battery_view_channel = MethodChannel("flutter.io/batterylevel_view_$viewId"),
   _battery_view_event_channel = EventChannel("flutter.io/batterylevel_view_event_$viewId"),
    _flutterToEvaluteStream = flutterToEvaluteStream; 

   Future<void> sendMessageToNatvie(String message) async{
     final result = await _battery_view_channel.invokeMethod("nativeToEvalute",message);
     return result;
   }

   void bindNativeMethodCallBackHandler() {
     _battery_view_channel.setMethodCallHandler(_handler);
   }

   Future<dynamic> _handler(MethodCall call) async {
     switch (call.method){
       case "flutterToEvalute":
       _flutterToEvaluteStream.sink.add(call.arguments);
       break;
       default: 
       break;
     }
   }

   Future<void> listentNativeContinuesEvents() async {
     if (_eventSinkStreamSubscription != null){
        _eventSinkStreamSubscription.cancel();
     }
     _eventSinkStreamSubscription = _battery_view_event_channel.receiveBroadcastStream("eventSink2").cast<String>()
       .listen((data){
          _flutterToEvaluteStream.sink.add(data);
       });
       return Future.value();
   }
}