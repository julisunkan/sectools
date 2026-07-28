package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;

public class NetworkInterfaceActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Network Interface Information"; }
    @Override protected String getCategoryColor() { return "#0091EA"; }
    @Override protected String getExecuteLabel() { return "Get Interfaces"; }
    @Override protected String[] getInputHints() { return new String[0]; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        StringBuilder sb = new StringBuilder("Network Interfaces\n\n");
        try {
            Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface iface : Collections.list(ifaces)) {
                sb.append("── ").append(iface.getName()).append(" ──────────────────────\n");
                sb.append("Display Name : ").append(iface.getDisplayName()).append("\n");
                sb.append("Up           : ").append(iface.isUp()).append("\n");
                sb.append("Loopback     : ").append(iface.isLoopback()).append("\n");
                sb.append("VirtIface    : ").append(iface.isVirtual()).append("\n");
                sb.append("MTU          : ").append(iface.getMTU()).append("\n");
                byte[] mac = iface.getHardwareAddress();
                if (mac != null) {
                    StringBuilder ms = new StringBuilder();
                    for (byte b : mac) ms.append(String.format("%02X:", b));
                    sb.append("MAC          : ").append(ms.substring(0, ms.length()-1)).append("\n");
                }
                for (java.net.InterfaceAddress ia : iface.getInterfaceAddresses()) {
                    sb.append("Address      : ").append(ia.getAddress().getHostAddress())
                      .append("/").append(ia.getNetworkPrefixLength()).append("\n");
                }
                sb.append("\n");
            }
        } catch (Exception e) { sb.append("Error: ").append(e.getMessage()); }
        cb.onResult(sb.toString());
    }
}
