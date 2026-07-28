package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.HttpUtil;
import com.netsec.toolkit.utils.FormatUtils;

public class DnsLookupActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "DNS Lookup Tool"; }
    @Override protected String getCategoryColor() { return "#0091EA"; }
    @Override protected String getExecuteLabel() { return "Resolve DNS"; }
    @Override protected String[] getInputHints() { return new String[]{"Domain name", "Record type (A, AAAA, MX, TXT, CNAME, NS — default A)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String domain = inputs[0];
        String type = inputs[1].isEmpty() ? "A" : inputs[1].toUpperCase().trim();
        try {
            String url = "https://dns.google/resolve?name=" + domain + "&type=" + type;
            String raw = HttpUtil.get(url);
            cb.onResult("DNS Lookup: " + domain + " (" + type + ")\n\n" + FormatUtils.prettyJson(raw));
        } catch (Exception e) {
            cb.onError(e.getMessage());
        }
    }
}
