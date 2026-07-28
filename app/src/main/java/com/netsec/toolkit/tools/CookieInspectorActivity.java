package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.HttpUtil;

import java.util.Map;

public class CookieInspectorActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Cookie Inspector"; }
    @Override protected String getCategoryColor() { return "#00BCD4"; }
    @Override protected String getExecuteLabel() { return "Inspect Cookies"; }
    @Override protected String[] getInputHints() { return new String[]{"URL (e.g. https://example.com)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String url = inputs[0].startsWith("http") ? inputs[0] : "https://" + inputs[0];
        try {
            Map<String, String> headers = HttpUtil.getHeaders(url);
            StringBuilder sb = new StringBuilder("Cookie Analysis: " + url + "\n\n");
            int cookieCount = 0;
            for (Map.Entry<String, String> e : headers.entrySet()) {
                if (e.getKey() != null && e.getKey().equalsIgnoreCase("Set-Cookie")) {
                    cookieCount++;
                    String raw = e.getValue();
                    sb.append("── Cookie ").append(cookieCount).append(" ──────────────────────\n");
                    sb.append("Raw: ").append(raw).append("\n\n");
                    analyzeCookie(sb, raw);
                    sb.append("\n");
                }
            }
            if (cookieCount == 0) sb.append("No Set-Cookie headers found.");
            else sb.append("Total cookies: ").append(cookieCount);
            cb.onResult(sb.toString());
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }

    private void analyzeCookie(StringBuilder sb, String raw) {
        String lower = raw.toLowerCase();
        sb.append("Secure    : ").append(lower.contains("secure") ? "YES ✓" : "NO ✗").append("\n");
        sb.append("HttpOnly  : ").append(lower.contains("httponly") ? "YES ✓" : "NO ✗").append("\n");
        if (lower.contains("samesite=strict")) sb.append("SameSite  : Strict ✓\n");
        else if (lower.contains("samesite=lax")) sb.append("SameSite  : Lax\n");
        else if (lower.contains("samesite=none")) sb.append("SameSite  : None ✗\n");
        else sb.append("SameSite  : Not set ✗\n");
        if (lower.contains("expires=")) sb.append("Persistent: YES\n");
        if (lower.contains("max-age=")) sb.append("Max-Age   : " + extractAttr(raw, "max-age") + "\n");
        if (lower.contains("path=")) sb.append("Path      : " + extractAttr(raw, "path") + "\n");
        if (lower.contains("domain=")) sb.append("Domain    : " + extractAttr(raw, "domain") + "\n");
    }

    private String extractAttr(String raw, String attr) {
        String lower = raw.toLowerCase();
        int idx = lower.indexOf(attr + "=");
        if (idx < 0) return "";
        int start = idx + attr.length() + 1;
        int end = raw.indexOf(";", start);
        return end < 0 ? raw.substring(start) : raw.substring(start, end);
    }
}
