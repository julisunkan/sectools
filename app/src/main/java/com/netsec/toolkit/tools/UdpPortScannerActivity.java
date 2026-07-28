package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpPortScannerActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "UDP Port Scanner"; }
    @Override protected String getCategoryColor() { return "#7C4DFF"; }
    @Override protected String getExecuteLabel() { return "Scan UDP"; }
    @Override protected String[] getInputHints() { return new String[]{"Host or IP", "Ports (e.g. 53,67,123,161,514)"}; }

    private static final int[] UDP_COMMON = {53,67,68,69,123,161,162,514,520,1194,1900,5353};

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String host = inputs[0];
        int[] ports = parseUdpPorts(inputs[1]);

        StringBuilder sb = new StringBuilder();
        sb.append("UDP Port Scan: ").append(host).append("\n");
        sb.append("Note: UDP scanning is limited on Android.\n\n");
        sb.append(String.format("%-8s %-14s %s\n", "PORT", "STATE", "SERVICE"));
        sb.append("─────────────────────────────────\n");

        for (int port : ports) {
            String state = probeUdp(host, port);
            sb.append(String.format("%-8d %-14s %s\n", port, state, getUdpService(port)));
        }
        cb.onResult(sb.toString());
    }

    private String probeUdp(String host, int port) {
        try (DatagramSocket s = new DatagramSocket()) {
            s.setSoTimeout(1500);
            byte[] probe = new byte[1];
            InetAddress addr = InetAddress.getByName(host);
            DatagramPacket pkt = new DatagramPacket(probe, 1, addr, port);
            s.send(pkt);
            byte[] buf = new byte[256];
            DatagramPacket response = new DatagramPacket(buf, buf.length);
            s.receive(response);
            return "open|filtered";
        } catch (java.net.SocketTimeoutException e) {
            return "open|filtered";
        } catch (Exception e) {
            return "closed";
        }
    }

    private int[] parseUdpPorts(String spec) {
        if (spec.isEmpty()) return UDP_COMMON;
        try {
            String[] parts = spec.split(",");
            int[] ports = new int[parts.length];
            for (int i = 0; i < parts.length; i++) ports[i] = Integer.parseInt(parts[i].trim());
            return ports;
        } catch (Exception e) { return UDP_COMMON; }
    }

    private String getUdpService(int port) {
        switch (port) {
            case 53: return "DNS"; case 67: return "DHCP-Server"; case 68: return "DHCP-Client";
            case 69: return "TFTP"; case 123: return "NTP"; case 161: return "SNMP";
            case 162: return "SNMP-Trap"; case 514: return "Syslog"; case 1194: return "OpenVPN";
            case 1900: return "UPnP"; case 5353: return "mDNS";
            default: return "";
        }
    }
}
