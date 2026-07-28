package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.HttpUtil;

import java.util.Map;

public class SecurityHeadersActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Security Headers Analyzer"; }
    @Override protected String getCategoryColor() { return "#00BCD4"; }
    @Override protected String getExecuteLabel() { return "Analyze Headers"; }
    @Override protected String[] getInputHints() { return new String[]{"URL (e.g. https://example.com)"}; }

    private static final String[] SECURITY_HEADERS = {
        "Content-Security-Policy", "X-Frame-Options", "X-Content-Type-Options",
        "Strict-Transport-Security", "X-XSS-Protection", "Referrer-Policy",
        "Permissions-Policy", "Cross-Origin-Embedder-Policy",
        "Cross-Origin-Opener-Policy", "Cross-Origin-Resource-Policy"
    };

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String url = inputs[0].startsWith("http") ? inputs[0] : "https://" + inputs[0];
        try {
            Map<String, String> headers = HttpUtil.getHeaders(url);
            StringBuilder sb = new StringBuilder("Security Headers Analysis\n");
            sb.append(url).append("\n\n");
            int passed = 0;
            for (String h : SECURITY_HEADERS) {
                boolean found = false;
                for (String k : headers.keySet()) {
                    if (k.equalsIgnoreCase(h)) { found = true; break; }
                }
                sb.append(found ? "PASS" : "MISS").append("  ").append(h).append("\n");
                if (found) {
                    sb.append("       └─ ").append(headers.get(h)).append("\n");
                    passed++;
                }
            }
            sb.append("\nScore: ").append(passed).append("/").append(SECURITY_HEADERS.length);
            int pct = (passed * 100) / SECURITY_HEADERS.length;
            sb.append(" (").append(pct).append("%) – ")
              .append(pct >= 80 ? "GOOD" : pct >= 50 ? "FAIR" : "NEEDS IMPROVEMENT");
            cb.onResult(sb.toString());
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }
}
