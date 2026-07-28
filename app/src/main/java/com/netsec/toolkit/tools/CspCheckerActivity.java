package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.HttpUtil;

import java.util.Map;

public class CspCheckerActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "CSP Header Checker"; }
    @Override protected String getCategoryColor() { return "#00C853"; }
    @Override protected String getExecuteLabel() { return "Check CSP"; }
    @Override protected String[] getInputHints() { return new String[]{"URL (e.g. https://example.com)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String url = inputs[0].startsWith("http") ? inputs[0] : "https://" + inputs[0];
        try {
            Map<String, String> headers = HttpUtil.getHeaders(url);
            String csp = null;
            for (Map.Entry<String, String> e : headers.entrySet()) {
                if (e.getKey() != null && e.getKey().equalsIgnoreCase("Content-Security-Policy")) {
                    csp = e.getValue(); break;
                }
            }
            if (csp == null) { cb.onResult("No Content-Security-Policy header found.\nThis is a security risk!"); return; }

            StringBuilder sb = new StringBuilder("CSP Analysis: " + url + "\n\n");
            sb.append("Raw CSP:\n").append(csp).append("\n\n");
            sb.append("── Directives ──────────────────────\n");
            for (String directive : csp.split(";")) {
                directive = directive.trim();
                if (!directive.isEmpty()) {
                    String[] parts = directive.split("\\s+", 2);
                    sb.append(String.format("%-30s : %s\n", parts[0], parts.length > 1 ? parts[1] : "(none)"));
                    if (directive.contains("unsafe-inline")) sb.append("  ⚠ unsafe-inline is dangerous\n");
                    if (directive.contains("unsafe-eval"))   sb.append("  ⚠ unsafe-eval is dangerous\n");
                    if (directive.contains("*"))             sb.append("  ⚠ Wildcard (*) is too permissive\n");
                }
            }
            cb.onResult(sb.toString());
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }
}
