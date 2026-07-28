package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.NetworkUtils;

public class PingActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Ping Utility"; }
    @Override protected String getCategoryColor() { return "#7C4DFF"; }
    @Override protected String getExecuteLabel() { return "Ping"; }
    @Override protected String[] getInputHints() { return new String[]{"Host or IP address", "Ping count (default 4)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String host = inputs[0];
        int count = 4;
        try { count = Integer.parseInt(inputs[1]); } catch (Exception ignored) {}
        if (count < 1 || count > 20) count = 4;

        StringBuilder sb = new StringBuilder();
        sb.append("PING ").append(host).append("\n\n");
        long total = 0; int reached = 0;
        for (int i = 1; i <= count; i++) {
            long ms = NetworkUtils.ping(host);
            if (ms >= 0) {
                sb.append("Reply from ").append(host).append(": time=").append(ms).append("ms\n");
                total += ms; reached++;
            } else {
                sb.append("Request timeout for icmp_seq ").append(i).append("\n");
            }
        }
        sb.append("\n--- ").append(host).append(" ping statistics ---\n");
        sb.append(count).append(" packets transmitted, ").append(reached).append(" received\n");
        if (reached > 0) sb.append("avg rtt = ").append(total / reached).append(" ms");
        cb.onResult(sb.toString());
    }
}
