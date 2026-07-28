package com.netsec.toolkit.tools;

import android.app.AlertDialog;
import android.net.TrafficStats;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.netsec.toolkit.base.BaseListActivity;
import com.netsec.toolkit.database.DatabaseHelper;

public class BandwidthMonitorActivity extends BaseListActivity {
    @Override protected String getToolTitle()    { return "Bandwidth Usage Monitor"; }
    @Override protected String getCategoryColor(){ return "#0091EA"; }
    @Override protected String getToolId()       { return "bandwidth_monitor"; }
    @Override protected String getColorTagForItem(DatabaseHelper.ListItem i) { return "#0091EA"; }

    @Override
    protected void onCreate(android.os.Bundle s) {
        super.onCreate(s);
        // Auto-log current stats
        long rx = TrafficStats.getTotalRxBytes();
        long tx = TrafficStats.getTotalTxBytes();
        long rxM = TrafficStats.getMobileRxBytes();
        long txM = TrafficStats.getMobileTxBytes();
        String now = new java.util.Date().toString();
        if (rx != TrafficStats.UNSUPPORTED) {
            db.addItem(getToolId(),
                "Snapshot: " + now.substring(0, 19),
                "Total RX: " + formatBytes(rx) + "  TX: " + formatBytes(tx),
                "Mobile RX: " + formatBytes(rxM) + "  TX: " + formatBytes(txM),
                now, "#0091EA");
            loadItems();
        }
    }

    @Override
    protected void showAddDialog() {
        long rx = TrafficStats.getTotalRxBytes(), tx = TrafficStats.getTotalTxBytes();
        addItem("Snapshot: " + new java.util.Date().toString().substring(0,19),
                "RX: " + formatBytes(rx) + "  TX: " + formatBytes(tx),
                "Mobile RX: " + formatBytes(TrafficStats.getMobileRxBytes()),
                new java.util.Date().toString(), "#0091EA");
    }

    private String formatBytes(long b) {
        if (b < 0) return "N/A";
        if (b < 1024) return b + " B";
        if (b < 1024*1024) return (b/1024) + " KB";
        if (b < 1024*1024*1024L) return (b/1024/1024) + " MB";
        return (b/1024/1024/1024) + " GB";
    }
}
