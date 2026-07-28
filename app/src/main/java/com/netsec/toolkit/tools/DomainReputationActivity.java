package com.netsec.toolkit.tools;

import com.netsec.toolkit.api.ApiConfig;
import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.FormatUtils;
import com.netsec.toolkit.utils.HttpUtil;

import java.util.HashMap;
import java.util.Map;

public class DomainReputationActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Domain Reputation Checker"; }
    @Override protected String getCategoryColor() { return "#00BFA5"; }
    @Override protected String getExecuteLabel() { return "Check Reputation"; }
    @Override protected String[] getInputHints() { return new String[]{"Domain name"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String domain = inputs[0].trim();
        String key    = ApiConfig.get(this).virusTotal();
        if (key.isEmpty()) { cb.onResult("VirusTotal API key not configured.\nGo to Settings → VirusTotal API Key.\n\nGet a free key at: https://www.virustotal.com/gui/join-us"); return; }
        try {
            String url = "https://www.virustotal.com/api/v3/domains/" + domain;
            Map<String, String> headers = new HashMap<>();
            headers.put("x-apikey", key);
            String raw = HttpUtil.get(url, headers);
            cb.onResult("VirusTotal Domain Report: " + domain + "\n\n" + FormatUtils.prettyJson(raw));
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }
}
