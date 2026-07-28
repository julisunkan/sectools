package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;

public class CidrCalcActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "CIDR/Subnet Calculator"; }
    @Override protected String getCategoryColor() { return "#0091EA"; }
    @Override protected String getExecuteLabel() { return "Calculate"; }
    @Override protected String[] getInputHints() { return new String[]{"CIDR notation (e.g. 10.0.0.0/8)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        try {
            String cidr = inputs[0].trim();
            String[] parts = cidr.split("/");
            String ip = parts[0];
            int prefix = Integer.parseInt(parts[1]);
            long maskLong = prefix == 0 ? 0 : (0xFFFFFFFFL << (32 - prefix)) & 0xFFFFFFFFL;
            long ipLong   = ipToLong(ip);
            long network  = ipLong & maskLong;
            long broadcast = network | (~maskLong & 0xFFFFFFFFL);
            long totalHosts = (long) Math.pow(2, 32 - prefix);
            long usable = totalHosts > 2 ? totalHosts - 2 : 0;

            StringBuilder sb = new StringBuilder();
            sb.append("CIDR Block      : ").append(longToIp(network)).append("/").append(prefix).append("\n");
            sb.append("Network Address : ").append(longToIp(network)).append("\n");
            sb.append("Broadcast Addr  : ").append(longToIp(broadcast)).append("\n");
            sb.append("Subnet Mask     : ").append(longToIp(maskLong)).append("\n");
            sb.append("Wildcard Mask   : ").append(longToIp(~maskLong & 0xFFFFFFFFL)).append("\n");
            sb.append("First Usable IP : ").append(longToIp(network + 1)).append("\n");
            sb.append("Last Usable IP  : ").append(longToIp(broadcast - 1)).append("\n");
            sb.append("Total Addresses : ").append(totalHosts).append("\n");
            sb.append("Usable Hosts    : ").append(usable).append("\n\n");
            sb.append("Supernets:\n");
            for (int p = prefix - 1; p >= Math.max(prefix - 4, 0); p--) {
                long snet = network & ((0xFFFFFFFFL << (32 - p)) & 0xFFFFFFFFL);
                sb.append("  /").append(p).append(" → ").append(longToIp(snet)).append("/").append(p).append("\n");
            }
            cb.onResult(sb.toString());
        } catch (Exception e) {
            cb.onError("Invalid CIDR: " + e.getMessage());
        }
    }

    private long ipToLong(String ip) {
        String[] p = ip.split("\\.");
        return ((long) Integer.parseInt(p[0]) << 24) | ((long) Integer.parseInt(p[1]) << 16) |
               ((long) Integer.parseInt(p[2]) << 8)  | (long) Integer.parseInt(p[3]);
    }

    private String longToIp(long l) {
        return ((l>>24)&0xFF)+"."+((l>>16)&0xFF)+"."+((l>>8)&0xFF)+"."+(l&0xFF);
    }
}
