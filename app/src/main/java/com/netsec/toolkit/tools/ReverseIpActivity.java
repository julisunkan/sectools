package com.netsec.toolkit.tools;

import com.netsec.toolkit.api.ApiConfig;
import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.HttpUtil;

public class ReverseIpActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Reverse IP Lookup"; }
    @Override protected String getCategoryColor() { return "#00BFA5"; }
    @Override protected String getExecuteLabel() { return "Reverse IP Lookup"; }
    @Override protected String[] getInputHints() { return new String[]{"IP address or domain"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String target = inputs[0].trim();
        try {
            String url = "https://api.hackertarget.com/reverseiplookup/?q=" + target;
            String apiKey = ApiConfig.get(this).hackerTarget();
            if (!apiKey.isEmpty()) url += "&apikey=" + apiKey;
            String raw = HttpUtil.get(url);
            String[] domains = raw.split("\n");
            StringBuilder sb = new StringBuilder("Reverse IP Lookup: " + target + "\n");
            sb.append("Domains on same IP: ").append(domains.length).append("\n\n");
            for (int i = 0; i < domains.length; i++) {
                sb.append(i + 1).append(". ").append(domains[i]).append("\n");
            }
            cb.onResult(sb.toString());
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }
}
