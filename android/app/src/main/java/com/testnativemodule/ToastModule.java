package com.testnativemodule;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by i8e4 on 12/13/17.
 */

public class ToastModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private static final String DURATION_SHORT_KEY = "SHORT";
    private static final String DURATION_LONG_KEY = "LONG";
    private static final int REQUEST_CODE = 123456;

    public ToastModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "ToastTest";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
        constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
        return constants;
    }

    @ReactMethod
    public void show(String message, int duration) {
        Toast.makeText(reactContext, message, duration).show();
    }

    @ReactMethod
    public void startAlarm() {
        Intent intent = new Intent(reactContext, TestService.class);
        intent.setAction(Alarm.ACTION_START_ALARM);

        boolean alarmRunning = (PendingIntent.getService(
                reactContext,
                REQUEST_CODE,
                intent,
                PendingIntent.FLAG_NO_CREATE) != null
        );

        if (alarmRunning) {
            Log.e("AlarmManager", "Alarm already running!");
            return;
        }

        PendingIntent pendingIntent = PendingIntent.getService(
                reactContext,
                REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) reactContext.getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + 3000,
                    60000,
                    pendingIntent);
        }
    }

    @ReactMethod
    public void stopAlarm() {
        Intent intent = new Intent(reactContext, TestService.class);
        intent.setAction(Alarm.ACTION_START_ALARM);
        PendingIntent pendingIntent = PendingIntent.getService(
                reactContext,
                REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        AlarmManager alarmManager = (AlarmManager) reactContext.getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            Log.e("AlarmManager", "Stoping...");
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }
}
