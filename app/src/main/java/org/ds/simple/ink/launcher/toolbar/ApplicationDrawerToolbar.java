package org.ds.simple.ink.launcher.toolbar;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;

import org.ds.simple.ink.launcher.LauncherSettingsActivity;
import org.ds.simple.ink.launcher.R;
import org.ds.simple.ink.launcher.drawer.ApplicationDrawer;
import org.ds.simple.ink.launcher.settings.ApplicationSettings;
import org.ds.simple.ink.launcher.common.ViewCache;

import lombok.val;

public class ApplicationDrawerToolbar extends RelativeLayout implements ApplicationDrawer.OnTotalItemCountChangeListener,
                                                                        ApplicationSettings.OnWifiSwitchEnabledChangeListener,
                                                                        ApplicationSettings.OnBacklightSwitchEnabledChangeListener {

    private static final int MIN_COLUMNS = 2;
    private static final int MAX_COLUMNS = 6;
    private static final int DEFAULT_COLUMNS = 3;
    private static final String PREFS = "icon_scale";
    private static final String KEY = "columns";

    private final ViewCache children = new ViewCache(this);

    public ApplicationDrawerToolbar(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.app_drawer_toolbar, this);
        initLauncherSettingsButton();
        initIconButtons();
    }

    private void initLauncherSettingsButton() {
        val launcherSettings = children.getView(R.id.open_launcher_settings);
        launcherSettings.setOnClickListener(new OnLauncherSettingsListener());
    }

    private void initIconButtons() {
        children.getView(R.id.ico_minus).setOnClickListener(v -> changeColumns(1));
        children.getView(R.id.ico_plus).setOnClickListener(v -> changeColumns(-1));
    }

    private int getColumns() {
        return getContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .getInt(KEY, DEFAULT_COLUMNS);
    }

    private void changeColumns(final int delta) {
        final int next = Math.max(MIN_COLUMNS, Math.min(MAX_COLUMNS, getColumns() + delta));
        getContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit().putInt(KEY, next).apply();
        applyColumns(next);
    }

    private void applyColumns(final int columns) {
        if (getParent() instanceof View) {
            final View grid = ((View) getParent()).findViewById(R.id.apps_grid);
            if (grid instanceof GridView) {
                ((GridView) grid).setNumColumns(columns);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        applyColumns(getColumns());
    }

    public void showWifiSwitch(final boolean visible) {
        val visibility = getVisibilityForState(visible);
        children.getView(R.id.wifi_switch).setVisibility(visibility);
    }

    public void showBacklightSwitch(final boolean visible) {
        val visibility = getVisibilityForState(visible);
        children.getView(R.id.backlight_switch).setVisibility(visibility);
    }

    private int getVisibilityForState(final boolean visible) {
        return visible ? View.VISIBLE : View.GONE;
    }

    public void setTotalItemsCount(final int itemsCount) {
        val text = getResources().getString(R.string.total_item_count_label, itemsCount);
        children.getTextView(R.id.total_items_count).setText(text);
    }

    @Override
    public void totalCountChanged(final int newCount) {
        setTotalItemsCount(newCount);
    }

    @Override
    public void wifiSwitchEnabled(final boolean whetherEnabled) {
        showWifiSwitch(whetherEnabled);
    }

    @Override
    public void backlightSwitchEnabled(final boolean whetherEnabled) {
        showBacklightSwitch(whetherEnabled);
    }

    private class OnLauncherSettingsListener implements View.OnClickListener {

        @Override
        public void onClick(final View launchSettingsButton) {
            val myIntent = new Intent(getContext(), LauncherSettingsActivity.class);
            getContext().startActivity(myIntent);
        }
    }
}
