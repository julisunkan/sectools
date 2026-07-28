package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;

import java.net.InetAddress;

public class TracerouteActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Traceroute Utility"; }
    @Override protected String getCategoryColor() { return "#7C4DFF"; }
    @Override protected String getExecuteLabel() { return "Trace"; }
    @Override protected String[] getInputHints() { return new String[]{"Host or IP address"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String host = inputs[0];
        StringBuilder sb = new StringBuilder("traceroute to ").append(host).append("\n\n");
        try {
            InetAddress target = InetAddress.getByName(host);
            sb.append("Target: ").append(target.getHostAddress()).append("\n\n");
            for (int ttl = 1; ttl <= 30; ttl++) {
                long start = System.currentTimeMillis();
                try {
                    InetAddress via = InetAddress.getByName(host);
                    boolean reached = via.isReachable(2000);
                    long ms = System.currentTimeMillis() - start;
                    sb.append(String.format("%2d  %s  %dms\n", ttl,
                            reached ? via.getHostAddress() : "*", ms));
                    if (reached) break;
                } catch (Exception e) {
                    sb.append(String.format("%2d  *  (timeout)\n", ttl));
                }
            }
        } catch (Exception e) {
            sb.append("Error: ").append(e.getMessage());
        }
        cb.onResult(sb.toString());
    }
}
