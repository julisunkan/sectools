package com.netsec.toolkit.tools;

import com.netsec.toolkit.api.ApiConfig;
import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.FormatUtils;
import com.netsec.toolkit.utils.HttpUtil;

public class IpLookupActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "IP Address Lookup"; }
    @Override protected String getCategoryColor() { return "#7C4DFF"; }
    @Override protected String getExecuteLabel() { return "Lookup"; }
    @Override protected String[] getInputHints() { return new String[]{"IP address or domain (blank = your IP)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        try {
            String target = inputs[0].isEmpty() ? "" : inputs[0];
            String url = ApiConfig.get(this).ipApi() + target;
            String raw = HttpUtil.get(url);
            cb.onResult(FormatUtils.prettyJson(raw));
        } catch (Exception e) {
            cb.onError(e.getMessage());
        }
    }
}
