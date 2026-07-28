package com.netsec.toolkit.tools;

import com.netsec.toolkit.api.ApiConfig;
import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.FormatUtils;
import com.netsec.toolkit.utils.HttpUtil;

import java.util.HashMap;
import java.util.Map;

public class CveSearchActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "CVE Search App"; }
    @Override protected String getCategoryColor() { return "#DD2C00"; }
    @Override protected String getExecuteLabel() { return "Search CVEs"; }
    @Override protected String[] getInputHints() { return new String[]{"CVE-ID (e.g. CVE-2021-44228) or keyword"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String query = inputs[0].trim();
        String nvdKey = ApiConfig.get(this).nvd();
        try {
            String url;
            if (query.toUpperCase().startsWith("CVE-")) {
                url = "https://services.nvd.nist.gov/rest/json/cves/2.0?cveId=" + query.toUpperCase();
            } else {
                url = "https://services.nvd.nist.gov/rest/json/cves/2.0?keywordSearch=" +
                      java.net.URLEncoder.encode(query, "UTF-8") + "&resultsPerPage=5";
            }
            Map<String, String> headers = new HashMap<>();
            if (!nvdKey.isEmpty()) headers.put("apiKey", nvdKey);
            String raw = HttpUtil.get(url, headers);
            org.json.JSONObject obj = new org.json.JSONObject(raw);
            org.json.JSONArray vulns = obj.optJSONObject("vulnerabilities") != null ? null : obj.optJSONArray("vulnerabilities");
            org.json.JSONArray items = new org.json.JSONArray(obj.optString("vulnerabilities","[]").isEmpty() ? "[]" : obj.toString());
            // Parse properly
            org.json.JSONArray vulnArr = obj.getJSONArray("vulnerabilities");
            StringBuilder sb = new StringBuilder("CVE Search: " + query + "\n");
            sb.append("Total: ").append(obj.optInt("totalResults",0)).append("\n\n");
            for (int i = 0; i < Math.min(vulnArr.length(), 5); i++) {
                org.json.JSONObject cve = vulnArr.getJSONObject(i).getJSONObject("cve");
                String id  = cve.optString("id","");
                String desc = "N/A";
                org.json.JSONArray descs = cve.optJSONArray("descriptions");
                if (descs != null) for (int j=0;j<descs.length();j++) if ("en".equals(descs.getJSONObject(j).optString("lang",""))) desc = descs.getJSONObject(j).optString("value","");
                double score = 0;
                String severity = "N/A";
                try { score = cve.getJSONObject("metrics").getJSONArray("cvssMetricV31").getJSONObject(0).getJSONObject("cvssData").getDouble("baseScore"); severity = cve.getJSONObject("metrics").getJSONArray("cvssMetricV31").getJSONObject(0).optString("baseSeverity",""); } catch (Exception ignored) {}
                sb.append("── ").append(id).append(" ──────────────────────\n");
                sb.append("Score    : ").append(score).append(" (").append(severity).append(")\n");
                sb.append("Desc     : ").append(desc.length()>200?desc.substring(0,200)+"...":desc).append("\n\n");
            }
            cb.onResult(sb.toString());
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }
}
