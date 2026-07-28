package com.netsec.toolkit.tools;

import com.netsec.toolkit.api.ApiConfig;
import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.FormatUtils;
import com.netsec.toolkit.utils.HttpUtil;

import java.util.HashMap;
import java.util.Map;

public class AsnLookupActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "ASN Lookup"; }
    @Override protected String getCategoryColor() { return "#AA00FF"; }
    @Override protected String getExecuteLabel() { return "Lookup ASN"; }
    @Override protected String[] getInputHints() { return new String[]{"IP address or ASN (e.g. 8.8.8.8 or AS15169)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String target = inputs[0].trim();
        String token  = ApiConfig.get(this).ipInfo();
        try {
            String url = "https://ipinfo.io/" + target + "/json";
            Map<String, String> headers = new HashMap<>();
            if (!token.isEmpty()) headers.put("Authorization", "Bearer " + token);
            String raw = HttpUtil.get(url, headers);
            cb.onResult("ASN Lookup: " + target + "\n\n" + FormatUtils.prettyJson(raw));
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }
}
