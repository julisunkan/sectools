package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.FormatUtils;
import com.netsec.toolkit.utils.HttpUtil;

public class DnsRecordActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "DNS Record Viewer"; }
    @Override protected String getCategoryColor() { return "#AA00FF"; }
    @Override protected String getExecuteLabel() { return "Fetch DNS Records"; }
    @Override protected String[] getInputHints() { return new String[]{"Domain name"}; }

    private static final String[] TYPES = {"A","AAAA","MX","NS","TXT","CNAME","SOA","CAA","PTR"};

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String domain = inputs[0].trim();
        StringBuilder sb = new StringBuilder("DNS Records for: " + domain + "\n\n");
        for (String type : TYPES) {
            try {
                String url = "https://dns.google/resolve?name=" + domain + "&type=" + type;
                String raw = HttpUtil.get(url);
                sb.append("── ").append(type).append(" ─────────────────────────\n");
                sb.append(FormatUtils.prettyJson(raw)).append("\n");
            } catch (Exception e) {
                sb.append("── ").append(type).append(": ").append(e.getMessage()).append("\n\n");
            }
        }
        cb.onResult(sb.toString());
    }
}
