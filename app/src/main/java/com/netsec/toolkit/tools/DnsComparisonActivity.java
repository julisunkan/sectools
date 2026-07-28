package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.HttpUtil;

import java.net.InetAddress;

public class DnsComparisonActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Public DNS Comparison"; }
    @Override protected String getCategoryColor() { return "#00BFA5"; }
    @Override protected String getExecuteLabel() { return "Compare DNS Servers"; }
    @Override protected String[] getInputHints() { return new String[]{"Domain to resolve"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String domain = inputs[0].trim();
        String[][] servers = {
            {"Google DNS",      "https://dns.google/resolve?name="},
            {"Cloudflare DNS",  "https://cloudflare-dns.com/dns-query?name="},
            {"Quad9",           "https://dns.quad9.net/dns-query?name="}
        };
        StringBuilder sb = new StringBuilder("DNS Server Comparison: " + domain + "\n\n");
        for (String[] srv : servers) {
            sb.append("── ").append(srv[0]).append(" ──────────────────────\n");
            try {
                java.util.Map<String, String> h = new java.util.HashMap<>();
                h.put("Accept", "application/dns-json");
                String url = srv[1] + domain + "&type=A";
                String raw = HttpUtil.get(url, h);
                org.json.JSONObject obj = new org.json.JSONObject(raw);
                org.json.JSONArray answers = obj.optJSONArray("Answer");
                if (answers != null) {
                    for (int i = 0; i < answers.length(); i++) {
                        org.json.JSONObject a = answers.getJSONObject(i);
                        int type = a.optInt("type", 0);
                        if (type == 1 || type == 28) sb.append("  ").append(a.optString("data")).append("\n");
                    }
                } else sb.append("  (no answer)\n");
            } catch (Exception e) { sb.append("  Error: ").append(e.getMessage()).append("\n"); }
        }
        cb.onResult(sb.toString());
    }
}
