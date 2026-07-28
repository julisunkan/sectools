package com.netsec.toolkit.tools;

import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.netsec.toolkit.base.BaseListActivity;
import com.netsec.toolkit.database.DatabaseHelper;
import com.netsec.toolkit.utils.NetworkUtils;

public class LatencyTrackerActivity extends BaseListActivity {
    @Override protected String getToolTitle()    { return "Latency History Tracker"; }
    @Override protected String getCategoryColor(){ return "#0091EA"; }
    @Override protected String getToolId()       { return "latency_tracker"; }
    @Override protected String getColorTagForItem(DatabaseHelper.ListItem i) { return "#0091EA"; }

    @Override
    protected void showAddDialog() {
        EditText etHost = new EditText(this);
        etHost.setHint("Host to ping (e.g. 8.8.8.8)");
        new AlertDialog.Builder(this).setTitle("Measure Latency")
            .setView(etHost)
            .setPositiveButton("Ping", (d,w) -> {
                String host = etHost.getText().toString().trim(); if(host.isEmpty()) return;
                new Thread(() -> {
                    long ms = NetworkUtils.ping(host);
                    String color = ms < 0 ? "#FF5252" : ms < 50 ? "#00E676" : ms < 150 ? "#FFD740" : "#FF6D00";
                    db.addItem(getToolId(), host + " — " + (ms<0?"timeout":ms+" ms"),
                        "Ping: " + (ms<0?"UNREACHABLE":ms+" ms"), "",
                        new java.util.Date().toString(), color);
                    runOnUiThread(this::loadItems);
                }).start();
            }).setNegativeButton("Cancel",null).show();
    }
}
