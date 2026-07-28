package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPv6CalcActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "IPv6 Calculator"; }
    @Override protected String getCategoryColor() { return "#0091EA"; }
    @Override protected String getExecuteLabel() { return "Analyze"; }
    @Override protected String[] getInputHints() { return new String[]{"IPv6 address (e.g. 2001:db8::1)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        try {
            InetAddress addr = InetAddress.getByName(inputs[0]);
            if (!(addr instanceof Inet6Address)) {
                cb.onError("Not a valid IPv6 address");
                return;
            }
            Inet6Address v6 = (Inet6Address) addr;
            String full = expandIPv6(v6.getAddress());
            cb.onResult(
                "Input            : " + inputs[0] + "\n" +
                "Expanded         : " + full + "\n" +
                "Compressed       : " + v6.getHostAddress() + "\n\n" +
                "Is Loopback      : " + v6.isLoopbackAddress() + "\n" +
                "Is Link Local    : " + v6.isLinkLocalAddress() + "\n" +
                "Is Site Local    : " + v6.isSiteLocalAddress() + "\n" +
                "Is Multicast     : " + v6.isMulticastAddress() + "\n" +
                "Is Any Local     : " + v6.isAnyLocalAddress() + "\n\n" +
                "Address Type     : " + getAddressType(v6)
            );
        } catch (UnknownHostException e) {
            cb.onError("Invalid IPv6 address: " + e.getMessage());
        }
    }

    private String expandIPv6(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i += 2) {
            if (i > 0) sb.append(":");
            sb.append(String.format("%02x%02x", bytes[i] & 0xFF, bytes[i+1] & 0xFF));
        }
        return sb.toString();
    }

    private String getAddressType(Inet6Address addr) {
        if (addr.isLoopbackAddress()) return "Loopback (::1)";
        if (addr.isLinkLocalAddress()) return "Link-Local (fe80::/10)";
        if (addr.isSiteLocalAddress()) return "Site-Local (fec0::/10)";
        if (addr.isMulticastAddress()) return "Multicast (ff00::/8)";
        byte[] b = addr.getAddress();
        if ((b[0] & 0xE0) == 0x20) return "Global Unicast (2000::/3)";
        return "Unknown";
    }
}
