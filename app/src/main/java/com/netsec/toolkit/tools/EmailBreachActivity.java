package com.netsec.toolkit.tools;

import com.netsec.toolkit.api.ApiConfig;
import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.FormatUtils;
import com.netsec.toolkit.utils.HttpUtil;

import java.util.HashMap;
import java.util.Map;

public class EmailBreachActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Email Breach Checker"; }
    @Override protected String getCategoryColor() { return "#00BFA5"; }
    @Override protected String getExecuteLabel() { return "Check Breaches"; }
    @Override protected String[] getInputHints() { return new String[]{"Email address to check"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String email = inputs[0].trim();
        String key   = ApiConfig.get(this).hibp();
        if (key.isEmpty()) { cb.onResult("Have I Been Pwned API key not configured.\nGo to Settings → HIBP API Key.\n\nGet a key at: https://haveibeenpwned.com/API/Key"); return; }
        try {
            String url = "https://haveibeenpwned.com/api/v3/breachedaccount/" + email + "?truncateResponse=false";
            Map<String, String> headers = new HashMap<>();
            headers.put("hibp-api-key", key);
            headers.put("User-Agent", "NetSec-Toolkit");
            String raw = HttpUtil.get(url, headers);
            if (raw.trim().startsWith("[")) {
                org.json.JSONArray arr = new org.json.JSONArray(raw);
                StringBuilder sb = new StringBuilder("Breach Check: " + email + "\n\n");
                sb.append("Found in ").append(arr.length()).append(" breach(es):\n\n");
                for (int i = 0; i < arr.length(); i++) {
                    org.json.JSONObject b = arr.getJSONObject(i);
                    sb.append(i+1).append(". ").append(b.optString("Name","")).append(" (").append(b.optString("BreachDate","")).append(")\n");
                    sb.append("   Classes: ").append(b.optJSONArray("DataClasses")).append("\n");
                }
                cb.onResult(sb.toString());
            } else cb.onResult("No breaches found for: " + email);
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }
}
