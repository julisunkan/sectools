package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;

import java.net.URI;

public class UrlParserActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "URL Parser"; }
    @Override protected String getCategoryColor() { return "#00C853"; }
    @Override protected String getExecuteLabel() { return "Parse URL"; }
    @Override protected String[] getInputHints() { return new String[]{"Full URL to parse"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        try {
            URI uri = new URI(inputs[0]);
            String query = uri.getQuery();
            StringBuilder sb = new StringBuilder("URL Analysis\n\n");
            sb.append("Scheme     : ").append(uri.getScheme()).append("\n");
            sb.append("Host       : ").append(uri.getHost()).append("\n");
            sb.append("Port       : ").append(uri.getPort() == -1 ? "(default)" : uri.getPort()).append("\n");
            sb.append("Path       : ").append(uri.getPath()).append("\n");
            sb.append("Query      : ").append(query != null ? query : "(none)").append("\n");
            sb.append("Fragment   : ").append(uri.getFragment() != null ? uri.getFragment() : "(none)").append("\n");
            sb.append("UserInfo   : ").append(uri.getUserInfo() != null ? uri.getUserInfo() : "(none)").append("\n");
            sb.append("Authority  : ").append(uri.getAuthority()).append("\n\n");
            if (query != null && !query.isEmpty()) {
                sb.append("── Query Parameters ────────────────\n");
                for (String param : query.split("&")) {
                    String[] kv = param.split("=", 2);
                    sb.append(String.format("  %-20s = %s\n",
                        kv[0], kv.length > 1 ? kv[1] : "(empty)"));
                }
            }
            cb.onResult(sb.toString());
        } catch (Exception e) { cb.onError("Invalid URL: " + e.getMessage()); }
    }
}
