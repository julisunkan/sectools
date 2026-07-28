package com.netsec.toolkit.tools;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.NetworkUtils;

public class GatewayDnsActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Gateway & DNS Inspector"; }
    @Override protected String getCategoryColor() { return "#0091EA"; }
    @Override protected String getExecuteLabel() { return "Inspect Network"; }
    @Override protected String[] getInputHints() { return new String[0]; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        StringBuilder sb = new StringBuilder("Network Configuration\n\n");
        try {
            WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wm != null) {
                WifiInfo wi = wm.getConnectionInfo();
                DhcpInfo di = wm.getDhcpInfo();
                sb.append("── Wi-Fi ─────────────────────────────\n");
                sb.append("SSID         : ").append(wi.getSSID()).append("\n");
                sb.append("BSSID        : ").append(wi.getBSSID()).append("\n");
                sb.append("IP Address   : ").append(NetworkUtils.ipIntToString(wi.getIpAddress())).append("\n");
                sb.append("Link Speed   : ").append(wi.getLinkSpeed()).append(" Mbps\n");
                sb.append("Signal Level : ").append(wi.getRssi()).append(" dBm\n\n");
                sb.append("── DHCP ──────────────────────────────\n");
                sb.append("Gateway      : ").append(NetworkUtils.ipIntToString(di.gateway)).append("\n");
                sb.append("DNS 1        : ").append(NetworkUtils.ipIntToString(di.dns1)).append("\n");
                sb.append("DNS 2        : ").append(NetworkUtils.ipIntToString(di.dns2)).append("\n");
                sb.append("Subnet Mask  : ").append(NetworkUtils.ipIntToString(di.netmask)).append("\n");
                sb.append("DHCP Server  : ").append(NetworkUtils.ipIntToString(di.serverAddress)).append("\n");
                sb.append("Lease Time   : ").append(di.leaseDuration).append(" seconds\n");
            } else {
                sb.append("WiFi manager not available");
            }
            sb.append("\n── Network Interfaces ───────────────\n");
            for (String line : NetworkUtils.getLocalIpAddresses()) sb.append(line).append("\n");
        } catch (Exception e) { sb.append("Error: ").append(e.getMessage()); }
        cb.onResult(sb.toString());
    }
}
