package com.example.batterylevel;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONObject;
import org.w3c.dom.Text;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.EventChannel.EventSink;
import io.flutter.plugin.common.EventChannel.StreamHandler;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.plugin.platform.PlatformView;

public class AndroidViewDemo implements PlatformView, MethodCallHandler, StreamHandler {

    private BinaryMessenger  binaryMessenger;
    private View view = null;
    private EditText recieveText = null;
    private TextView sendText = null;
    private Button sendEventButton = null;
    private Button sendButton = null;
    private MethodChannel methodChannel;
    private EventSink eventSink = null;
    private EventChannel eventChannel = null;

    public AndroidViewDemo(int viewId,
                       BinaryMessenger messenger,
                       Registrar registrar,
                       Object o) {
        this.binaryMessenger = messenger;
        this.methodChannel = new MethodChannel(binaryMessenger, Constant.PLUGIN_PLAT_FORM_BASIC_VIEW_NAME + "_" + viewId);
        this.methodChannel.setMethodCallHandler(this);
        this.eventChannel = new EventChannel(binaryMessenger, "flutter.io/batterylevel_view_event_" + viewId);
        this.eventChannel.setStreamHandler(this);
        setupViews(registrar);
    }

    @Override
    public void onInputConnectionLocked() {

    }

    @Override
    public void onInputConnectionUnlocked() {

    }

    private void setupViews(final Registrar registrar) {

        view = (View) LayoutInflater.from(registrar.activity()).inflate(R.layout.android_view, null);
        sendText = (TextView)view.findViewById(R.id.sendEditText);
        sendText.setHint("please in put text send to flutter");
        sendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendText.requestFocus();
                InputMethodManager imm = (InputMethodManager) registrar.context().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        sendText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        sendText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                sendText.setText(s);

            }
        });

        sendButton = view.findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = sendText.getText().toString();
                methodChannel.invokeMethod("flutterToEvalute", text);
            }
        });

        recieveText = view.findViewById(R.id.recieveEditText);
        recieveText.setHint("please in put text send to flutter");
        sendEventButton = view.findViewById(R.id.recieveButton);
        sendEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eventSink != null){
                    eventSink.success("clicked eventSink button");
                }
            }
        });

    }

    @Override
    public void onMethodCall(MethodCall methodCall, Result result) {
        if (methodCall.method.equals("nativeToEvalute")) {
            if(methodCall.arguments instanceof String) {
                String text = (String)methodCall.arguments;
                recieveText.setText(text);
            } else if (methodCall.arguments instanceof JSONObject) {
                JSONObject json = (JSONObject)methodCall.arguments;
                recieveText.setText(json.toString());
            }
        } else {
            result.notImplemented();
        }

    }

    @Override
    public View getView() {
         return view;
    }

    @Override
    public void dispose() {
        eventSink = null;
        eventChannel.setStreamHandler(null);
        methodChannel.setMethodCallHandler(null);
    }

    @Override
    public void onListen(Object o, EventSink eventSink) {
        this.eventSink = eventSink;
    }

    @Override
    public void onCancel(Object o) {
        eventSink = null;
    }
}

//TextField edit event monitor
/**
 * //        sendText.addTextChangedListener(new TextWatcher() {
 * //            @Override
 * //            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
 * //
 * //            }
 * //
 * //            @Override
 * //            public void onTextChanged(CharSequence s, int start, int before, int count) {
 * //
 * //            }
 * //
 * //            @Override
 * //            public void afterTextChanged(Editable s) {
 * //                // methodChannel.invokeMethod("flutterToEvalute", textView.getText());
 * //            }
 * //        });
 * */
//JSON Convert
//  try {
//          JSONObject text1 = new JSONObject("test");
//          System.out.println(text1);
//          } catch (JSONException e) {
//          System.out.println(e.toString());
//          }