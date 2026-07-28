package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.FormatUtils;
import com.netsec.toolkit.utils.HttpUtil;

public class DomainInfoActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Domain Information Viewer"; }
    @Override protected String getCategoryColor() { return "#AA00FF"; }
    @Override protected String getExecuteLabel() { return "Get Domain Info"; }
    @Override protected String[] getInputHints() { return new String[]{"Domain name"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String domain = inputs[0].trim().toLowerCase();
        try {
            StringBuilder sb = new StringBuilder("Domain Info: " + domain + "\n\n");
            // RDAP
            try {
                String rdap = HttpUtil.get("https://rdap.org/domain/" + domain);
                sb.append("── RDAP ─────────────────────────────\n");
                sb.append(FormatUtils.prettyJson(rdap)).append("\n");
            } catch (Exception e) { sb.append("RDAP: ").append(e.getMessage()).append("\n\n"); }
            // DNS A record
            try {
                String dns = HttpUtil.get("https://dns.google/resolve?name=" + domain + "&type=A");
                sb.append("── DNS (A Record) ───────────────────\n");
                sb.append(FormatUtils.prettyJson(dns)).append("\n");
            } catch (Exception e) { sb.append("DNS: ").append(e.getMessage()).append("\n"); }
            cb.onResult(sb.toString());
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }
}
