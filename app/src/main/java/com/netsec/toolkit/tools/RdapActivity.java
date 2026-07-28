package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.FormatUtils;
import com.netsec.toolkit.utils.HttpUtil;

public class RdapActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "RDAP Client"; }
    @Override protected String getCategoryColor() { return "#AA00FF"; }
    @Override protected String getExecuteLabel() { return "RDAP Query"; }
    @Override protected String[] getInputHints() { return new String[]{"Domain or IP", "Type: domain / ip / autnum (default: domain)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String target = inputs[0].trim();
        String type   = inputs[1].isEmpty() ? "domain" : inputs[1].trim().toLowerCase();
        try {
            String url = "https://rdap.org/" + type + "/" + target;
            String raw = HttpUtil.get(url);
            cb.onResult("RDAP Query: " + type + " / " + target + "\nURL: " + url + "\n\n" + FormatUtils.prettyJson(raw));
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }
}
