package org.ds.simple.ink.launcher.toolbar;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageButton;

import org.ds.simple.ink.launcher.LightService;
import org.ds.simple.ink.launcher.R;

public class BacklightSwitch extends AppCompatImageButton {

    private final Handler handler = new Handler(Looper.getMainLooper());

    public BacklightSwitch(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        ensureService();
        refreshImage();
        setOnClickListener(v -> {
            final boolean justEnabled = ensureService();
            final Intent i = new Intent(LightService.ACTION)
                    .setPackage(getContext().getPackageName())
                    .putExtra("btn", "led_control");
            handler.postDelayed(() -> getContext().sendBroadcast(i), justEnabled ? 3000 : 0);
            handler.postDelayed(this::refreshImage, 6000);
        });
    }

    /** Turns the accessibility service on if the system switched it off. */
    private boolean ensureService() {
        try {
            final ContentResolver cr = getContext().getContentResolver();
            final String current = Settings.Secure.getString(
                    cr, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            final boolean enabled = Settings.Secure.getInt(
                    cr, Settings.Secure.ACCESSIBILITY_ENABLED, 0) == 1;
            if (current != null && current.contains("LightService") && enabled) {
                return false;
            }
            final String me = getContext().getPackageName() + "/.LightService";
            final String next = (current == null || current.isEmpty()) ? me : current + ":" + me;
            Settings.Secure.putString(cr, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES, next);
            Settings.Secure.putInt(cr, Settings.Secure.ACCESSIBILITY_ENABLED, 1);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void refreshImage() {
        final int on = Settings.Global.getInt(
                getContext().getContentResolver(), "mogu_led_onoff", 1);
        setImageResource(on == 0
                ? R.drawable.ic_backlight_off_toolbar
                : R.drawable.ic_backlight_on_toolbar);
    }
}
