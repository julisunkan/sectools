package com.netsec.toolkit.tools;

import com.netsec.toolkit.api.ApiConfig;
import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.HttpUtil;

public class MacVendorActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "MAC Address Vendor Lookup"; }
    @Override protected String getCategoryColor() { return "#7C4DFF"; }
    @Override protected String getExecuteLabel() { return "Lookup Vendor"; }
    @Override protected String[] getInputHints() { return new String[]{"MAC address (e.g. 00:1A:2B:3C:4D:5E)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        try {
            String mac = inputs[0].replace(":", "").replace("-", "").replace(".", "");
            if (mac.length() < 6) { cb.onError("Invalid MAC address"); return; }
            String url = ApiConfig.get(this).macVendors() + mac;
            String result = HttpUtil.get(url);
            String macFormatted = formatMac(inputs[0]);
            cb.onResult("MAC Address : " + macFormatted +
                    "\nOUI         : " + mac.substring(0, 6).toUpperCase() +
                    "\nVendor      : " + result.trim());
        } catch (Exception e) {
            cb.onError(e.getMessage());
        }
    }

    private String formatMac(String raw) {
        String clean = raw.replace(":", "").replace("-", "").replace(".", "").toUpperCase();
        if (clean.length() < 12) return raw;
        return clean.substring(0, 2) + ":" + clean.substring(2, 4) + ":" + clean.substring(4, 6) + ":" +
               clean.substring(6, 8) + ":" + clean.substring(8, 10) + ":" + clean.substring(10, 12);
    }
}
