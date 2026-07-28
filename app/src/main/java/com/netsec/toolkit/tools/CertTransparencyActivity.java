package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.FormatUtils;
import com.netsec.toolkit.utils.HttpUtil;

public class CertTransparencyActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Certificate Transparency Search"; }
    @Override protected String getCategoryColor() { return "#AA00FF"; }
    @Override protected String getExecuteLabel() { return "Search CT Logs"; }
    @Override protected String[] getInputHints() { return new String[]{"Domain (e.g. example.com)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String domain = inputs[0].trim();
        try {
            String url = "https://crt.sh/?q=" + domain + "&output=json";
            String raw = HttpUtil.get(url);
            org.json.JSONArray arr = new org.json.JSONArray(raw);
            StringBuilder sb = new StringBuilder("Certificate Transparency: " + domain + "\n");
            sb.append("Certificates found: ").append(arr.length()).append("\n\n");
            int show = Math.min(arr.length(), 20);
            for (int i = 0; i < show; i++) {
                org.json.JSONObject obj = arr.getJSONObject(i);
                sb.append(String.format("%-40s  issued: %s  by: %s\n",
                    obj.optString("common_name", ""),
                    obj.optString("not_before", "").substring(0, 10),
                    obj.optString("issuer_name", "").replaceAll(".*O=([^,]+).*", "$1")));
            }
            if (arr.length() > 20) sb.append("\n...and ").append(arr.length()-20).append(" more");
            cb.onResult(sb.toString());
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }
}
