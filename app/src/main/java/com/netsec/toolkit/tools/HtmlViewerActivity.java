package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.HttpUtil;

public class HtmlViewerActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "HTML Source Viewer"; }
    @Override protected String getCategoryColor() { return "#00C853"; }
    @Override protected String getExecuteLabel() { return "Fetch HTML"; }
    @Override protected String[] getInputHints() { return new String[]{"URL to fetch"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String url = inputs[0].startsWith("http") ? inputs[0] : "https://" + inputs[0];
        try {
            String html = HttpUtil.get(url);
            if (html.length() > 5000) html = html.substring(0, 5000) + "\n\n[...truncated at 5000 chars]";
            cb.onResult(html);
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }
}
