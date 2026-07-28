package com.netsec.toolkit.tools;

import com.netsec.toolkit.api.ApiConfig;
import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.FormatUtils;
import com.netsec.toolkit.utils.HttpUtil;

import java.util.HashMap;
import java.util.Map;

public class ThreatFeedActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Threat Intelligence Feed Reader"; }
    @Override protected String getCategoryColor() { return "#546E7A"; }
    @Override protected String getExecuteLabel() { return "Load Threat Feed"; }
    @Override protected String[] getInputHints() { return new String[]{"Indicator (IP/domain/hash) to look up in OTX"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String indicator = inputs[0].trim();
        String otxKey    = ApiConfig.get(this).otx();

        if (otxKey.isEmpty()) {
            cb.onResult("AlienVault OTX API key not configured.\nGo to Settings → AlienVault OTX API Key.\n\nSign up free at: https://otx.alienvault.com/");
            return;
        }

        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("X-OTX-API-KEY", otxKey);

            String type = indicator.matches("\\d+\\.\\d+\\.\\d+\\.\\d+") ? "IPv4" :
                          indicator.matches("[0-9a-fA-F]{32,128}") ? "file" : "domain";

            String url = "https://otx.alienvault.com/api/v1/indicators/" + type + "/" + indicator + "/general";
            String raw = HttpUtil.get(url, headers);
            cb.onResult("OTX Threat Intelligence\nIndicator: " + indicator + " (" + type + ")\n\n" +
                FormatUtils.prettyJson(raw));
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }
}
