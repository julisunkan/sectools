package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.FormatUtils;
import com.netsec.toolkit.utils.HttpUtil;

public class PassiveReconActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Passive Recon Dashboard"; }
    @Override protected String getCategoryColor() { return "#AA00FF"; }
    @Override protected String getExecuteLabel() { return "Run Recon"; }
    @Override protected String[] getInputHints() { return new String[]{"Domain or IP to investigate"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String target = inputs[0].trim();
        boolean isIp  = target.matches("\\d+\\.\\d+\\.\\d+\\.\\d+");
        StringBuilder sb = new StringBuilder("Passive Recon Dashboard\nTarget: " + target + "\n\n");

        // DNS
        sb.append("── DNS Lookup ───────────────────────\n");
        try { sb.append(FormatUtils.prettyJson(HttpUtil.get("https://dns.google/resolve?name=" + target + "&type=A"))).append("\n"); }
        catch (Exception e) { sb.append("DNS error: " + e.getMessage() + "\n\n"); }

        // GeoIP
        sb.append("── GeoIP ────────────────────────────\n");
        try { sb.append(FormatUtils.prettyJson(HttpUtil.get("http://ip-api.com/json/" + target))).append("\n"); }
        catch (Exception e) { sb.append("GeoIP error: " + e.getMessage() + "\n\n"); }

        // RDAP (domain only)
        if (!isIp) {
            sb.append("── RDAP/WHOIS ───────────────────────\n");
            try { String rdap = HttpUtil.get("https://rdap.org/domain/" + target); sb.append(FormatUtils.prettyJson(rdap)).append("\n"); }
            catch (Exception e) { sb.append("RDAP error: " + e.getMessage() + "\n\n"); }

            // Cert Transparency
            sb.append("── Certificate Transparency ─────────\n");
            try {
                String ct = HttpUtil.get("https://crt.sh/?q=" + target + "&output=json");
                org.json.JSONArray arr = new org.json.JSONArray(ct);
                sb.append("Found ").append(arr.length()).append(" certificates\n\n");
            } catch (Exception e) { sb.append("CT error: " + e.getMessage() + "\n\n"); }
        }

        cb.onResult(sb.toString());
    }
}
