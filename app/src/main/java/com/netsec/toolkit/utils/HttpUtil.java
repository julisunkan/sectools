package com.netsec.toolkit.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HttpUtil {
    private static final int TIMEOUT = 15000;

    public static String get(String urlStr) throws Exception {
        return get(urlStr, null);
    }

    public static String get(String urlStr, Map<String, String> headers) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(TIMEOUT);
        conn.setReadTimeout(TIMEOUT);
        conn.setRequestProperty("User-Agent", "NetSec-Toolkit/1.0");
        if (headers != null) {
            for (Map.Entry<String, String> e : headers.entrySet()) {
                conn.setRequestProperty(e.getKey(), e.getValue());
            }
        }
        conn.connect();
        int code = conn.getResponseCode();
        BufferedReader br = new BufferedReader(
                new InputStreamReader(code < 400 ? conn.getInputStream() : conn.getErrorStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line).append("\n");
        br.close();
        conn.disconnect();
        return sb.toString().trim();
    }

    public static Map<String, String> getHeaders(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(TIMEOUT);
        conn.setReadTimeout(TIMEOUT);
        conn.setRequestProperty("User-Agent", "NetSec-Toolkit/1.0");
        conn.connect();
        Map<String, String> result = new LinkedHashMap<>();
        result.put("Status", conn.getResponseCode() + " " + conn.getResponseMessage());
        for (Map.Entry<String, List<String>> e : conn.getHeaderFields().entrySet()) {
            if (e.getKey() != null) {
                result.put(e.getKey(), String.join(", ", e.getValue()));
            }
        }
        conn.disconnect();
        return result;
    }

    public static int getStatusCode(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("HEAD");
        conn.setConnectTimeout(TIMEOUT);
        conn.setReadTimeout(TIMEOUT);
        conn.setInstanceFollowRedirects(false);
        conn.connect();
        int code = conn.getResponseCode();
        conn.disconnect();
        return code;
    }

    public static String followRedirects(String urlStr) throws Exception {
        StringBuilder chain = new StringBuilder();
        String current = urlStr;
        int hops = 0;
        while (hops++ < 15) {
            URL url = new URL(current);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(TIMEOUT);
            conn.setReadTimeout(TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            int code = conn.getResponseCode();
            chain.append(hops).append(". [").append(code).append("] ").append(current).append("\n");
            if (code >= 300 && code < 400) {
                String loc = conn.getHeaderField("Location");
                conn.disconnect();
                if (loc == null || loc.equals(current)) break;
                current = loc;
            } else {
                conn.disconnect();
                break;
            }
        }
        return chain.toString().trim();
    }
}
