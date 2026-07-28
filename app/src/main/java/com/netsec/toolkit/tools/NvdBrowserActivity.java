package com.netsec.toolkit.tools;

import com.netsec.toolkit.api.ApiConfig;
import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.FormatUtils;
import com.netsec.toolkit.utils.HttpUtil;

import java.util.HashMap;
import java.util.Map;

public class NvdBrowserActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "NVD Vulnerability Browser"; }
    @Override protected String getCategoryColor() { return "#DD2C00"; }
    @Override protected String getExecuteLabel() { return "Browse NVD"; }
    @Override protected String[] getInputHints() { return new String[]{"Keyword or product (e.g. apache log4j)", "Severity: CRITICAL/HIGH/MEDIUM/LOW (optional)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String keyword  = inputs[0].trim();
        String severity = inputs[1].trim().toUpperCase();
        String nvdKey   = ApiConfig.get(this).nvd();
        try {
            String url = "https://services.nvd.nist.gov/rest/json/cves/2.0?keywordSearch=" +
                    java.net.URLEncoder.encode(keyword, "UTF-8") + "&resultsPerPage=10";
            if (!severity.isEmpty()) url += "&cvssV3Severity=" + severity;
            Map<String, String> headers = new HashMap<>();
            if (!nvdKey.isEmpty()) headers.put("apiKey", nvdKey);
            String raw = HttpUtil.get(url, headers);
            org.json.JSONObject obj = new org.json.JSONObject(raw);
            int total = obj.optInt("totalResults", 0);
            org.json.JSONArray vulns = obj.getJSONArray("vulnerabilities");
            StringBuilder sb = new StringBuilder("NVD Browser: " + keyword);
            if (!severity.isEmpty()) sb.append(" [").append(severity).append("]");
            sb.append("\nTotal: ").append(total).append("\n\n");
            for (int i = 0; i < vulns.length(); i++) {
                org.json.JSONObject cve = vulns.getJSONObject(i).getJSONObject("cve");
                String id = cve.optString("id","");
                double score = 0;
                String sev = "N/A";
                try { org.json.JSONObject m = cve.getJSONObject("metrics"); score = m.getJSONArray("cvssMetricV31").getJSONObject(0).getJSONObject("cvssData").getDouble("baseScore"); sev = m.getJSONArray("cvssMetricV31").getJSONObject(0).optString("baseSeverity",""); } catch(Exception ignored){}
                String desc = "N/A";
                org.json.JSONArray ds = cve.optJSONArray("descriptions");
                if (ds != null) for (int j=0;j<ds.length();j++) if ("en".equals(ds.getJSONObject(j).optString("lang",""))) desc = ds.getJSONObject(j).optString("value","");
                sb.append(String.format("%-20s [%.1f %s]\n", id, score, sev));
                sb.append("  ").append(desc.length()>120?desc.substring(0,120)+"...":desc).append("\n\n");
            }
            cb.onResult(sb.toString());
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }
}
