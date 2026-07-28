package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.HttpUtil;

public class DnsPropagationActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "DNS Propagation Checker"; }
    @Override protected String getCategoryColor() { return "#00BFA5"; }
    @Override protected String getExecuteLabel() { return "Check Propagation"; }
    @Override protected String[] getInputHints() { return new String[]{"Domain to check", "Record type (A, AAAA, MX, etc. — default A)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String domain = inputs[0].trim();
        String type   = inputs[1].isEmpty() ? "A" : inputs[1].toUpperCase();
        String[][] resolvers = {
            {"Google",      "https://dns.google/resolve"},
            {"Cloudflare",  "https://cloudflare-dns.com/dns-query"},
            {"Quad9",       "https://dns.quad9.net/dns-query"},
            {"OpenDNS",     "https://doh.opendns.com/dns-query"},
            {"AdGuard",     "https://dns.adguard.com/dns-query"}
        };
        StringBuilder sb = new StringBuilder("DNS Propagation Check\n");
        sb.append("Domain: ").append(domain).append("  Type: ").append(type).append("\n\n");
        for (String[] r : resolvers) {
            sb.append(String.format("%-12s : ", r[0]));
            try {
                java.util.Map<String,String> h = new java.util.HashMap<>();
                h.put("Accept","application/dns-json");
                String raw = HttpUtil.get(r[1]+"?name="+domain+"&type="+type, h);
                org.json.JSONObject obj = new org.json.JSONObject(raw);
                org.json.JSONArray ans = obj.optJSONArray("Answer");
                if (ans == null || ans.length() == 0) { sb.append("(no answer)\n"); continue; }
                StringBuilder vals = new StringBuilder();
                for (int i=0;i<ans.length();i++) { if (i>0) vals.append(", "); vals.append(ans.getJSONObject(i).optString("data","")); }
                sb.append(vals).append("\n");
            } catch (Exception e) { sb.append("error: ").append(e.getMessage()).append("\n"); }
        }
        cb.onResult(sb.toString());
    }
}
