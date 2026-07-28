package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.HttpUtil;

public class RedirectCheckerActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Website Redirect Checker"; }
    @Override protected String getCategoryColor() { return "#00C853"; }
    @Override protected String getExecuteLabel() { return "Check Redirects"; }
    @Override protected String[] getInputHints() { return new String[]{"URL to check"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String url = inputs[0].startsWith("http") ? inputs[0] : "http://" + inputs[0];
        try {
            String chain = HttpUtil.followRedirects(url);
            cb.onResult("Redirect Chain for:\n" + url + "\n\n" + chain);
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }
}
