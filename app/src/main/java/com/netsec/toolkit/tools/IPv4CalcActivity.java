package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;

public class IPv4CalcActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "IPv4 Subnet Calculator"; }
    @Override protected String getCategoryColor() { return "#0091EA"; }
    @Override protected String getExecuteLabel() { return "Calculate"; }
    @Override protected String[] getInputHints() { return new String[]{"IP address (e.g. 192.168.1.100)", "Subnet mask or prefix (e.g. 255.255.255.0 or /24)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        try {
            String ip = inputs[0].trim();
            String mask = inputs[1].trim();
            int prefix;
            long maskLong;

            if (mask.startsWith("/")) {
                prefix = Integer.parseInt(mask.substring(1));
                maskLong = prefix == 0 ? 0 : (0xFFFFFFFFL << (32 - prefix)) & 0xFFFFFFFFL;
            } else {
                maskLong = ipToLong(mask);
                prefix = Long.bitCount(maskLong);
            }

            long ipLong = ipToLong(ip);
            long network = ipLong & maskLong;
            long broadcast = network | (~maskLong & 0xFFFFFFFFL);
            long hosts = broadcast - network - 1;
            long firstHost = network + 1;
            long lastHost  = broadcast - 1;

            cb.onResult(
                "IP Address      : " + ip + "\n" +
                "Subnet Mask     : " + longToIp(maskLong) + " (/" + prefix + ")\n" +
                "Network Address : " + longToIp(network) + "\n" +
                "Broadcast Addr  : " + longToIp(broadcast) + "\n" +
                "First Host      : " + longToIp(firstHost) + "\n" +
                "Last Host       : " + longToIp(lastHost) + "\n" +
                "Usable Hosts    : " + (hosts > 0 ? hosts : 0) + "\n" +
                "CIDR Notation   : " + longToIp(network) + "/" + prefix + "\n" +
                "Wildcard Mask   : " + longToIp(~maskLong & 0xFFFFFFFFL) + "\n" +
                "IP Class        : " + getIpClass(ipLong)
            );
        } catch (Exception e) {
            cb.onError("Invalid input: " + e.getMessage());
        }
    }

    private long ipToLong(String ip) {
        String[] parts = ip.split("\\.");
        return ((long) Integer.parseInt(parts[0]) << 24) |
               ((long) Integer.parseInt(parts[1]) << 16) |
               ((long) Integer.parseInt(parts[2]) << 8)  |
               (long)  Integer.parseInt(parts[3]);
    }

    private String longToIp(long l) {
        return ((l >> 24) & 0xFF) + "." + ((l >> 16) & 0xFF) + "." + ((l >> 8) & 0xFF) + "." + (l & 0xFF);
    }

    private String getIpClass(long ip) {
        if ((ip & 0x80000000L) == 0) return "A (1.0.0.0 – 126.255.255.255)";
        if ((ip & 0xC0000000L) == 0x80000000L) return "B (128.0.0.0 – 191.255.255.255)";
        if ((ip & 0xE0000000L) == 0xC0000000L) return "C (192.0.0.0 – 223.255.255.255)";
        if ((ip & 0xF0000000L) == 0xE0000000L) return "D – Multicast";
        return "E – Reserved";
    }
}
