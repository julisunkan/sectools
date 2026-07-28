package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.FormatUtils;
import com.netsec.toolkit.utils.HttpUtil;

public class DomainExpiryActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Domain Expiration Tracker"; }
    @Override protected String getCategoryColor() { return "#00BFA5"; }
    @Override protected String getExecuteLabel() { return "Check Expiry"; }
    @Override protected String[] getInputHints() { return new String[]{"Domain name"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String domain = inputs[0].trim().toLowerCase();
        try {
            String raw = HttpUtil.get("https://rdap.org/domain/" + domain);
            org.json.JSONObject obj = new org.json.JSONObject(raw);
            StringBuilder sb = new StringBuilder("Domain Expiry: " + domain + "\n\n");

            // Extract events
            org.json.JSONArray events = obj.optJSONArray("events");
            if (events != null) {
                for (int i = 0; i < events.length(); i++) {
                    org.json.JSONObject e = events.getJSONObject(i);
                    String action = e.optString("eventAction", "");
                    String date   = e.optString("eventDate", "").substring(0, Math.min(10, e.optString("eventDate","").length()));
                    sb.append(String.format("%-15s : %s\n", action, date));
                }
            } else sb.append(FormatUtils.prettyJson(raw));

            // Status
            org.json.JSONArray status = obj.optJSONArray("status");
            if (status != null) {
                sb.append("\nStatus: ");
                for (int i = 0; i < status.length(); i++) sb.append(status.getString(i)).append(", ");
            }
            cb.onResult(sb.toString());
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }
}
