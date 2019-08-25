package com.icar.launcher.content;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppListContent {
    public static final List<AppListContent.AppItem> ITEMS = new ArrayList<AppListContent.AppItem>();
    public static final Map<String, AppListContent.AppItem> ITEM_MAP = new HashMap<String, AppListContent.AppItem>();

    public static class AppItem {
        public final String name;
        public final String packageName;
        public final String version;
        public final Drawable image;

        public AppItem(String name, String packageName, String version, Drawable image) {
            this.name = name;
            this.packageName = packageName;
            this.version = version;
            this.image = image;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
