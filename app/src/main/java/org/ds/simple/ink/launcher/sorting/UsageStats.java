package org.ds.simple.ink.launcher.sorting;

import android.content.Context;
import android.content.SharedPreferences;

public final class UsageStats {

    private static final String PREFS = "usage_counts";
    private static SharedPreferences prefs;

    private UsageStats() {
    }

    public static synchronized void init(final Context context) {
        if (prefs == null) {
            prefs = context.getApplicationContext()
                    .getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        }
    }

    public static int get(final String flattenName) {
        return prefs == null ? 0 : prefs.getInt(flattenName, 0);
    }

    public static void increment(final String flattenName) {
        if (prefs != null) {
            prefs.edit().putInt(flattenName, prefs.getInt(flattenName, 0) + 1).apply();
        }
    }
}
