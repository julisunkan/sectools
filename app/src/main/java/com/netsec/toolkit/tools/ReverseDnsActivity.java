package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;

import java.net.InetAddress;

public class ReverseDnsActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Reverse DNS Lookup"; }
    @Override protected String getCategoryColor() { return "#0091EA"; }
    @Override protected String getExecuteLabel() { return "Reverse Lookup"; }
    @Override protected String[] getInputHints() { return new String[]{"IP address"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        try {
            InetAddress addr = InetAddress.getByName(inputs[0]);
            String canonical = addr.getCanonicalHostName();
            String hostname  = addr.getHostName();
            cb.onResult("IP Address     : " + inputs[0] +
                    "\nHostname       : " + hostname +
                    "\nCanonical Name : " + canonical +
                    "\nIs Loopback    : " + addr.isLoopbackAddress() +
                    "\nIs Site Local  : " + addr.isSiteLocalAddress() +
                    "\nIs Multicast   : " + addr.isMulticastAddress());
        } catch (Exception e) {
            cb.onError(e.getMessage());
        }
    }
}
