package com.netsec.toolkit.model;

import android.content.Intent;
import android.content.Context;

public class ToolEntry {
    public final int number;
    public final String name;
    public final String category;
    public final String color;
    public final Class<?> activityClass;

    public ToolEntry(int number, String name, String category, String color, Class<?> activityClass) {
        this.number = number;
        this.name = name;
        this.category = category;
        this.color = color;
        this.activityClass = activityClass;
    }

    public Intent createIntent(Context ctx) {
        return new Intent(ctx, activityClass);
    }
}
