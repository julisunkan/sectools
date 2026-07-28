package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.HttpUtil;

import java.util.HashMap;
import java.util.Map;

public class CorsInspectorActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "CORS Header Inspector"; }
    @Override protected String getCategoryColor() { return "#00C853"; }
    @Override protected String getExecuteLabel() { return "Inspect CORS"; }
    @Override protected String[] getInputHints() { return new String[]{"URL to inspect", "Origin to test (e.g. https://evil.com)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String url    = inputs[0].startsWith("http") ? inputs[0] : "https://" + inputs[0];
        String origin = inputs[1].isEmpty() ? "https://evil.com" : inputs[1];
        try {
            Map<String, String> reqHeaders = new HashMap<>();
            reqHeaders.put("Origin", origin);
            reqHeaders.put("Access-Control-Request-Method", "GET");
            Map<String, String> respHeaders = HttpUtil.getHeaders(url);

            StringBuilder sb = new StringBuilder("CORS Analysis\n");
            sb.append("URL    : ").append(url).append("\n");
            sb.append("Origin : ").append(origin).append("\n\n");

            String[] corsHeaders = {
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Methods",
                "Access-Control-Allow-Headers",
                "Access-Control-Allow-Credentials",
                "Access-Control-Max-Age",
                "Access-Control-Expose-Headers"
            };
            for (String h : corsHeaders) {
                boolean found = false;
                for (Map.Entry<String, String> e : respHeaders.entrySet()) {
                    if (e.getKey() != null && e.getKey().equalsIgnoreCase(h)) {
                        sb.append(String.format("%-40s : %s\n", h, e.getValue()));
                        if (e.getValue().equals("*")) sb.append("  ⚠ Wildcard – allows any origin!\n");
                        found = true; break;
                    }
                }
                if (!found) sb.append(String.format("%-40s : (not present)\n", h));
            }
            cb.onResult(sb.toString());
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }
}
