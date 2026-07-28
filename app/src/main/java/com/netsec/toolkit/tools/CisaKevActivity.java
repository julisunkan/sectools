package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.HttpUtil;

public class CisaKevActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "CISA KEV Browser"; }
    @Override protected String getCategoryColor() { return "#DD2C00"; }
    @Override protected String getExecuteLabel() { return "Load CISA KEV"; }
    @Override protected String[] getInputHints() { return new String[]{"Filter keyword (optional)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String filter = inputs[0].trim().toLowerCase();
        try {
            String raw = HttpUtil.get("https://www.cisa.gov/sites/default/files/feeds/known_exploited_vulnerabilities.json");
            org.json.JSONObject obj = new org.json.JSONObject(raw);
            org.json.JSONArray vulns = obj.getJSONArray("vulnerabilities");
            int total = vulns.length();
            StringBuilder sb = new StringBuilder("CISA Known Exploited Vulnerabilities\n");
            sb.append("Total entries: ").append(total).append("\n\n");
            int shown = 0;
            for (int i = vulns.length()-1; i >= 0 && shown < 20; i--) {
                org.json.JSONObject v = vulns.getJSONObject(i);
                String id      = v.optString("cveID","");
                String vendor  = v.optString("vendorProject","");
                String product = v.optString("product","");
                String vuln    = v.optString("vulnerabilityName","");
                String dueDate = v.optString("dueDate","");
                if (!filter.isEmpty() && !id.toLowerCase().contains(filter) &&
                    !vendor.toLowerCase().contains(filter) && !product.toLowerCase().contains(filter) &&
                    !vuln.toLowerCase().contains(filter)) continue;
                sb.append(String.format("%-20s  Due: %-12s  %s – %s\n", id, dueDate, vendor, product));
                shown++;
            }
            if (shown == 0) sb.append("No matching entries.");
            else if (shown >= 20) sb.append("\n[Showing 20 most recent matching entries]");
            cb.onResult(sb.toString());
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }
}
