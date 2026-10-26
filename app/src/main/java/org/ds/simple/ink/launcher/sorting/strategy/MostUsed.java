package org.ds.simple.ink.launcher.sorting.strategy;

import org.ds.simple.ink.launcher.apps.ApplicationInfo;
import org.ds.simple.ink.launcher.sorting.UsageStats;

import java.util.Comparator;

public class MostUsed implements Comparator<ApplicationInfo> {

    @Override
    public int compare(final ApplicationInfo appInfo1, final ApplicationInfo appInfo2) {
        final int count1 = UsageStats.get(appInfo1.getFlattenName());
        final int count2 = UsageStats.get(appInfo2.getFlattenName());
        if (count1 != count2) {
            return count2 > count1 ? 1 : -1;
        }
        return appInfo1.getLabel().compareTo(appInfo2.getLabel());
    }
}
