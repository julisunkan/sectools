package com.netsec.toolkit.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class FormatUtils {

    public static String prettyJson(String raw) {
        try {
            raw = raw.trim();
            if (raw.startsWith("{")) {
                return prettyObject(new JSONObject(raw), 0);
            } else if (raw.startsWith("[")) {
                return prettyArray(new JSONArray(raw), 0);
            }
            return raw;
        } catch (Exception e) {
            return raw;
        }
    }

    private static String prettyObject(JSONObject obj, int depth) throws Exception {
        StringBuilder sb = new StringBuilder();
        String indent = repeat("  ", depth);
        Iterator<String> keys = obj.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object val = obj.get(key);
            sb.append(indent).append(key).append(": ");
            if (val instanceof JSONObject) {
                sb.append("\n").append(prettyObject((JSONObject) val, depth + 1));
            } else if (val instanceof JSONArray) {
                sb.append("\n").append(prettyArray((JSONArray) val, depth + 1));
            } else {
                sb.append(val).append("\n");
            }
        }
        return sb.toString();
    }

    private static String prettyArray(JSONArray arr, int depth) throws Exception {
        StringBuilder sb = new StringBuilder();
        String indent = repeat("  ", depth);
        for (int i = 0; i < arr.length(); i++) {
            Object val = arr.get(i);
            if (val instanceof JSONObject) {
                sb.append(indent).append("- \n").append(prettyObject((JSONObject) val, depth + 1));
            } else {
                sb.append(indent).append("- ").append(val).append("\n");
            }
        }
        return sb.toString();
    }

    private static String repeat(String s, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(s);
        return sb.toString();
    }

    public static String timeAgo(long timestamp) {
        long now = System.currentTimeMillis() / 1000L;
        long diff = now - timestamp;
        if (diff < 60) return diff + "s ago";
        if (diff < 3600) return (diff / 60) + "m ago";
        if (diff < 86400) return (diff / 3600) + "h ago";
        return new SimpleDateFormat("MMM d", Locale.US).format(new Date(timestamp * 1000L));
    }

    public static String formatTimestamp(long ts) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date(ts * 1000L));
    }
}
