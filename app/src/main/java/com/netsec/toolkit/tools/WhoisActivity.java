package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.FormatUtils;
import com.netsec.toolkit.utils.HttpUtil;

public class WhoisActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "WHOIS Lookup"; }
    @Override protected String getCategoryColor() { return "#AA00FF"; }
    @Override protected String getExecuteLabel() { return "WHOIS Lookup"; }
    @Override protected String[] getInputHints() { return new String[]{"Domain name (e.g. example.com)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String domain = inputs[0].trim().toLowerCase();
        try {
            String url = "https://rdap.org/domain/" + domain;
            String raw = HttpUtil.get(url);
            cb.onResult("WHOIS (RDAP) for: " + domain + "\n\n" + FormatUtils.prettyJson(raw));
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }
}
