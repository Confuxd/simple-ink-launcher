package org.ds.simple.ink.launcher;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class LightService extends AccessibilityService {

    public static final String ACTION = "org.ds.simple.ink.launcher.LIGHT";
    private static final String ID = "com.android.systemui:id/";

    private final Handler h = new Handler(Looper.getMainLooper());

    private final BroadcastReceiver rx = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent i) {
            String btn = i.getStringExtra("btn");
            press(btn == null ? "led_control" : btn);
        }
    };

    @Override
    protected void onServiceConnected() {
        registerReceiver(rx, new IntentFilter(ACTION));
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent e) {
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    public void onDestroy() {
        try {
            unregisterReceiver(rx);
        } catch (Exception ignored) {
        }
        super.onDestroy();
    }

    private void press(final String btn) {
        performGlobalAction(GLOBAL_ACTION_NOTIFICATIONS);
        h.postDelayed(new Runnable() {
            public void run() {
                AccessibilityNodeInfo root = getRootInActiveWindow();
                if (root != null) {
                    List<AccessibilityNodeInfo> l =
                            root.findAccessibilityNodeInfosByViewId(ID + btn);
                    if (!l.isEmpty()) {
                        l.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                }
                h.postDelayed(new Runnable() {
                    public void run() {
                        performGlobalAction(GLOBAL_ACTION_BACK);
                    }
                }, 800);
            }
        }, 1500);
    }
}
