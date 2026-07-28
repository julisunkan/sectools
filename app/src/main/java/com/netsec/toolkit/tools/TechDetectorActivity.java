package com.netsec.toolkit.tools;

import com.netsec.toolkit.api.ApiConfig;
import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.HttpUtil;

import java.util.Map;

public class TechDetectorActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Website Technology Detector"; }
    @Override protected String getCategoryColor() { return "#00C853"; }
    @Override protected String getExecuteLabel() { return "Detect Technologies"; }
    @Override protected String[] getInputHints() { return new String[]{"URL (e.g. https://example.com)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String url = inputs[0].startsWith("http") ? inputs[0] : "https://" + inputs[0];
        String wapKey = ApiConfig.get(this).wappalyzer();
        try {
            // Detect from headers heuristically without requiring API
            Map<String, String> headers = HttpUtil.getHeaders(url);
            StringBuilder sb = new StringBuilder("Technology Detection: " + url + "\n\n");
            detectFromHeaders(sb, headers);

            // Also fetch body for meta-tag detection
            try {
                String body = HttpUtil.get(url);
                detectFromBody(sb, body);
            } catch (Exception ignored) {}

            if (!wapKey.isEmpty()) {
                sb.append("\n── Wappalyzer API ──────────────────\n");
                String apiUrl = "https://api.wappalyzer.com/v2/lookup/?urls=" + url;
                Map<String, String> h = new java.util.HashMap<>();
                h.put("x-api-key", wapKey);
                sb.append(HttpUtil.get(apiUrl, h));
            }
            cb.onResult(sb.toString());
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }

    private void detectFromHeaders(StringBuilder sb, Map<String, String> h) {
        sb.append("── Header-based Detection ──────────\n");
        for (Map.Entry<String, String> e : h.entrySet()) {
            if (e.getKey() == null) continue;
            String k = e.getKey().toLowerCase(), v = e.getValue().toLowerCase();
            if (k.equals("x-powered-by")) sb.append("Platform: ").append(e.getValue()).append("\n");
            if (k.equals("server"))       sb.append("Server  : ").append(e.getValue()).append("\n");
            if (k.equals("set-cookie") && v.contains("wordpress")) sb.append("CMS     : WordPress\n");
            if (v.contains("drupal"))     sb.append("CMS     : Drupal\n");
            if (v.contains("joomla"))     sb.append("CMS     : Joomla\n");
            if (v.contains("shopify"))    sb.append("Platform: Shopify\n");
        }
    }

    private void detectFromBody(StringBuilder sb, String body) {
        sb.append("\n── Body-based Detection ────────────\n");
        String b = body.toLowerCase();
        if (b.contains("wp-content") || b.contains("wp-includes")) sb.append("CMS       : WordPress\n");
        if (b.contains("drupal"))  sb.append("CMS       : Drupal\n");
        if (b.contains("joomla"))  sb.append("CMS       : Joomla\n");
        if (b.contains("react"))   sb.append("Frontend  : React\n");
        if (b.contains("angular")) sb.append("Frontend  : Angular\n");
        if (b.contains("vue"))     sb.append("Frontend  : Vue.js\n");
        if (b.contains("jquery"))  sb.append("Library   : jQuery\n");
        if (b.contains("bootstrap")) sb.append("CSS       : Bootstrap\n");
        if (b.contains("google-analytics") || b.contains("gtag")) sb.append("Analytics : Google Analytics\n");
        if (b.contains("cloudflare")) sb.append("CDN/WAF   : Cloudflare\n");
    }
}
