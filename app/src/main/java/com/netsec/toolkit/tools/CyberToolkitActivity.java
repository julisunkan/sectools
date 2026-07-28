package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;

public class CyberToolkitActivity extends BaseToolActivity {
    @Override protected String getToolTitle()    { return "All-in-One Cybersecurity Toolkit"; }
    @Override protected String getCategoryColor(){ return "#1A237E"; }
    @Override protected String getExecuteLabel() { return "Quick Scan"; }
    @Override protected String[] getInputHints() { return new String[]{"Target domain or IP for quick multi-tool analysis"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String target = inputs[0].trim();
        if (target.isEmpty()) { cb.onResult(getDashboardInfo()); return; }
        StringBuilder sb = new StringBuilder("🔍 Quick Analysis: " + target + "\n\n");
        // GeoIP
        try { sb.append("── GeoIP ──────────────────────────\n"); sb.append(com.netsec.toolkit.utils.FormatUtils.prettyJson(com.netsec.toolkit.utils.HttpUtil.get("http://ip-api.com/json/"+target))).append("\n"); } catch(Exception e) { sb.append("GeoIP: "+e.getMessage()+"\n\n"); }
        // DNS
        try { sb.append("── DNS (A) ────────────────────────\n"); sb.append(com.netsec.toolkit.utils.FormatUtils.prettyJson(com.netsec.toolkit.utils.HttpUtil.get("https://dns.google/resolve?name="+target+"&type=A"))).append("\n"); } catch(Exception e) { sb.append("DNS: "+e.getMessage()+"\n\n"); }
        // Ping
        long ping = com.netsec.toolkit.utils.NetworkUtils.ping(target);
        sb.append("── Ping ───────────────────────────\n");
        sb.append(ping >= 0 ? "ALIVE — " + ping + " ms" : "UNREACHABLE").append("\n\n");
        // Common ports
        sb.append("── Common Open Ports ──────────────\n");
        int[] ports = {80,443,22,21,25,3389,8080};
        for (int p : ports) {
            boolean open = com.netsec.toolkit.utils.NetworkUtils.isTcpPortOpen(target, p, 800);
            if (open) sb.append("  ").append(p).append(" OPEN\n");
        }
        sb.append("\nBack to individual tools for full analysis.");
        cb.onResult(sb.toString());
    }

    private String getDashboardInfo() {
        return "NetSec Toolkit — 100 Security Tools\n\n" +
            "Navigate via the menu (≡) or search to find tools.\n\n" +
            "CATEGORIES:\n" +
            "  1-10   Network Tools\n" +
            "  11-20  DNS & Network Info\n" +
            "  21-30  Web Inspector\n" +
            "  31-40  Web Tools\n" +
            "  41-50  Password & Hashing\n" +
            "  51-60  Encryption Suite\n" +
            "  61-70  OSINT & Recon\n" +
            "  71-80  Reputation & Breach\n" +
            "  81-90  Vulnerability Intel\n" +
            "  91-100 Security Management\n\n" +
            "Enter a domain or IP above for a quick multi-tool analysis.";
    }
}
