package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.NetworkUtils;

public class ArpViewerActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "ARP Table Viewer"; }
    @Override protected String getCategoryColor() { return "#7C4DFF"; }
    @Override protected String getExecuteLabel() { return "View ARP Table"; }
    @Override protected String[] getInputHints() { return new String[0]; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String arp = NetworkUtils.readArpTable();
        cb.onResult("ARP Table (/proc/net/arp)\n\n" + arp);
    }
}
