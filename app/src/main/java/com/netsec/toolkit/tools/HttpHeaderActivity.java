package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.HttpUtil;

import java.util.Map;

public class HttpHeaderActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "HTTP Header Inspector"; }
    @Override protected String getCategoryColor() { return "#00BCD4"; }
    @Override protected String getExecuteLabel() { return "Fetch Headers"; }
    @Override protected String[] getInputHints() { return new String[]{"URL (e.g. https://example.com)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String url = ensureScheme(inputs[0]);
        try {
            Map<String, String> headers = HttpUtil.getHeaders(url);
            StringBuilder sb = new StringBuilder("HTTP Headers for: " + url + "\n\n");
            for (Map.Entry<String, String> e : headers.entrySet()) {
                sb.append(String.format("%-35s : %s\n", e.getKey(), e.getValue()));
            }
            cb.onResult(sb.toString());
        } catch (Exception e) {
            cb.onError(e.getMessage());
        }
    }

    private String ensureScheme(String url) {
        if (!url.startsWith("http")) return "https://" + url;
        return url;
    }
}
