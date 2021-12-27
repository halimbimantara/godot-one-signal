package org.godot.onesignalplugin;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;

import org.godotengine.godot.Dictionary;
import org.godotengine.godot.Godot;
import org.godotengine.godot.plugin.GodotPlugin;
import org.godotengine.godot.plugin.UsedByGodot;

public class GodotOneSignal extends GodotPlugin {
    private static final String TAG = "GodotOneSignal";
    private Dictionary notificationData = new Dictionary();
    private String action = null;
    private String uri = null;
    private Boolean intentWasChecked = false;
    private Class mainClass;
    public GodotOneSignal(Godot godot) {
        super(godot);
        intentWasChecked = false;
    }

    @NonNull
    @Override
    public String getPluginName() {
        return "GodotOneSignal";
    }

    @UsedByGodot
    public void initialize(String appKey, boolean debug) {
        // OneSignal Initialization
        if (debug) {
            OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        }
        OneSignal.initWithContext(getActivity());
        OneSignal.setAppId(appKey);
        OneSignal.setNotificationOpenedHandler(result -> {
            mainClass = null;
            try {
                mainClass = Class.forName("com.godot.game.GodotApp");
            } catch (ClassNotFoundException e) {
                // app not found, do nothing
                e.printStackTrace();
            }
            Intent intent = new Intent(getActivity(),mainClass);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
        });
    }

    @Override
    public View onMainCreate(Activity activity) {
        Bundle extras = Godot.getCurrentIntent().getExtras();
        if (extras != null) {
            Log.d(TAG, "Extras: from notif" + extras.toString());
            Log.i(TAG, "onMainCreate: ");
        }
        return null;
    }

    @Override
    public void onMainResume() {
        //checkIntent();
        intentWasChecked = false;
    }

    private void checkIntent() {
        Log.w(TAG, "I'm going to check application intent");
        Intent intent = Godot.getCurrentIntent();
        if (intent == null) {
            Log.d(TAG, "No intent in app activity");
            return;
        }
        Log.w(TAG, "The intent isn't null, so check it closely.");
        if (intent.getExtras() != null) {
            Bundle extras = Godot.getCurrentIntent().getExtras();
            Log.d(TAG, "Extras:" + extras.toString());
            notificationData = new Dictionary();
            for (String key : extras.keySet()) {
                Object value = extras.get(key);
                try {
                    notificationData.put(key, value);
                    Log.w(TAG, "Get new value " + value.toString() + " for key " + key);
                } catch (Exception e) {
                    Log.d(TAG, "Conversion error: " + e.toString());
                    e.printStackTrace();
                }
            }
            Log.d(TAG, "Extras content: " + notificationData.toString());
        } else {
            Log.d(TAG, "No extra bundle in app activity!");
        }
        if (intent.getAction() != null) {
            Log.w(TAG, "Get deeplink action from intent");
            action = intent.getAction();
        }
        if (intent.getData() != null) {
            Log.w(TAG, "Get uri from intent");
            uri = intent.getData().toString();
        }
        intentWasChecked = true;
    }

    public Dictionary get_notification_data() {
        if (!intentWasChecked) checkIntent();
        return notificationData;
    }

    public String get_deeplink_action() {
        if (!intentWasChecked) checkIntent();
        return action;
    }

    public String get_deeplink_uri() {
        if (!intentWasChecked) checkIntent();
        return uri;
    }
}
