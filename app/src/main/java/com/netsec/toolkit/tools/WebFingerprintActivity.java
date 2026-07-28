package com.netsec.toolkit.tools;

import com.netsec.toolkit.api.ApiConfig;
import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.HttpUtil;
import com.netsec.toolkit.utils.FormatUtils;

public class WebFingerprintActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Web Fingerprinting Tool"; }
    @Override protected String getCategoryColor() { return "#00C853"; }
    @Override protected String getExecuteLabel() { return "Fingerprint"; }
    @Override protected String[] getInputHints() { return new String[]{"Domain (e.g. example.com)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String host = inputs[0].replace("https://","").replace("http://","").split("/")[0];
        String bwKey = ApiConfig.get(this).builtWith();
        StringBuilder sb = new StringBuilder("Web Fingerprint: " + host + "\n\n");
        try {
            if (!bwKey.isEmpty()) {
                String url = "https://api.builtwith.com/v21/api.json?KEY=" + bwKey + "&LOOKUP=" + host;
                sb.append("── BuiltWith API ────────────────────\n");
                sb.append(FormatUtils.prettyJson(HttpUtil.get(url))).append("\n");
            } else {
                sb.append("BuiltWith API key not configured.\n");
                sb.append("Configure it in Settings → BuiltWith API Key.\n\n");
                sb.append("Without an API key, basic detection is performed via HTTP headers.\n\n");
                // Fallback: header analysis
                java.util.Map<String, String> headers = HttpUtil.getHeaders("https://" + host);
                for (java.util.Map.Entry<String, String> e : headers.entrySet()) {
                    if (e.getKey() != null) sb.append(e.getKey()).append(": ").append(e.getValue()).append("\n");
                }
            }
        } catch (Exception e) { sb.append("Error: ").append(e.getMessage()); }
        cb.onResult(sb.toString());
    }
}
