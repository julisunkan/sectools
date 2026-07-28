package com.netsec.toolkit.tools;

import com.netsec.toolkit.api.ApiConfig;
import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.FormatUtils;
import com.netsec.toolkit.utils.HttpUtil;

import java.util.HashMap;
import java.util.Map;

public class IpReputationActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "IP Reputation Checker"; }
    @Override protected String getCategoryColor() { return "#00BFA5"; }
    @Override protected String getExecuteLabel() { return "Check Reputation"; }
    @Override protected String[] getInputHints() { return new String[]{"IP address to check"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String ip  = inputs[0].trim();
        String key = ApiConfig.get(this).abuseIpDb();
        if (key.isEmpty()) { cb.onResult("AbuseIPDB API key not configured.\nGo to Settings → AbuseIPDB API Key.\n\nSign up free at: https://www.abuseipdb.com/api"); return; }
        try {
            String url = "https://api.abuseipdb.com/api/v2/check?ipAddress=" + ip + "&maxAgeInDays=90&verbose";
            Map<String, String> headers = new HashMap<>();
            headers.put("Key", key);
            headers.put("Accept", "application/json");
            String raw = HttpUtil.get(url, headers);
            cb.onResult("AbuseIPDB Report: " + ip + "\n\n" + FormatUtils.prettyJson(raw));
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }
}
