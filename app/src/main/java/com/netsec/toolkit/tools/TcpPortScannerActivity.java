package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.NetworkUtils;

public class TcpPortScannerActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "TCP Port Scanner"; }
    @Override protected String getCategoryColor() { return "#7C4DFF"; }
    @Override protected String getExecuteLabel() { return "Scan Ports"; }
    @Override protected String[] getInputHints() { return new String[]{"Host or IP", "Port range (e.g. 1-1024 or 80,443,8080)"}; }

    private static final int[] COMMON_PORTS = {
        21,22,23,25,53,80,110,111,135,139,143,443,445,993,995,
        1723,3306,3389,5900,8080,8443,8888
    };

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String host = inputs[0];
        String portSpec = inputs[1].trim();
        int[] ports = parsePorts(portSpec);

        StringBuilder sb = new StringBuilder();
        sb.append("TCP Port Scan: ").append(host).append("\n");
        sb.append("Scanning ").append(ports.length).append(" ports...\n\n");
        sb.append(String.format("%-8s %-10s %s\n", "PORT", "STATE", "SERVICE"));
        sb.append("─────────────────────────────\n");

        int open = 0;
        for (int port : ports) {
            boolean isOpen = NetworkUtils.isTcpPortOpen(host, port, 1000);
            if (isOpen) {
                sb.append(String.format("%-8d %-10s %s\n", port, "OPEN", getServiceName(port)));
                open++;
            }
        }
        sb.append("\n─────────────────────────────\n");
        sb.append(open).append(" open port(s) found.");
        cb.onResult(sb.toString());
    }

    private int[] parsePorts(String spec) {
        if (spec.isEmpty()) return COMMON_PORTS;
        try {
            if (spec.contains("-")) {
                String[] parts = spec.split("-");
                int from = Integer.parseInt(parts[0].trim());
                int to   = Integer.parseInt(parts[1].trim());
                to = Math.min(to, from + 500);
                int[] ports = new int[to - from + 1];
                for (int i = 0; i < ports.length; i++) ports[i] = from + i;
                return ports;
            } else if (spec.contains(",")) {
                String[] parts = spec.split(",");
                int[] ports = new int[parts.length];
                for (int i = 0; i < parts.length; i++) ports[i] = Integer.parseInt(parts[i].trim());
                return ports;
            } else {
                return new int[]{Integer.parseInt(spec)};
            }
        } catch (Exception e) { return COMMON_PORTS; }
    }

    private String getServiceName(int port) {
        switch (port) {
            case 21: return "FTP"; case 22: return "SSH"; case 23: return "Telnet";
            case 25: return "SMTP"; case 53: return "DNS"; case 80: return "HTTP";
            case 110: return "POP3"; case 143: return "IMAP"; case 443: return "HTTPS";
            case 445: return "SMB"; case 3306: return "MySQL"; case 3389: return "RDP";
            case 5900: return "VNC"; case 8080: return "HTTP-Alt"; case 8443: return "HTTPS-Alt";
            default: return "";
        }
    }
}
