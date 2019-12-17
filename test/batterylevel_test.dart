import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:batterylevel/batterylevel.dart';

void main() {
  const MethodChannel channel = MethodChannel('batterylevel');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    // expect(await Batterylevel.platformVersion, '42');
  });
}
