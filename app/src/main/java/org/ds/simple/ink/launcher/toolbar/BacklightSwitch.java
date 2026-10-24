package org.ds.simple.ink.launcher.toolbar;

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
        refreshImage();
        setOnClickListener(v -> {
            final Intent i = new Intent(LightService.ACTION)
                    .setPackage(getContext().getPackageName())
                    .putExtra("btn", "led_control");
            getContext().sendBroadcast(i);
            handler.postDelayed(this::refreshImage, 6000);
        });
    }

    private void refreshImage() {
        final int on = Settings.Global.getInt(
                getContext().getContentResolver(), "mogu_led_onoff", 1);
        setImageResource(on == 0
                ? R.drawable.ic_backlight_off_toolbar
                : R.drawable.ic_backlight_on_toolbar);
    }
}
