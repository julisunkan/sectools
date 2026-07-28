package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.HttpUtil;

public class HttpStatusActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "HTTP Status Code Tester"; }
    @Override protected String getCategoryColor() { return "#00C853"; }
    @Override protected String getExecuteLabel() { return "Test Status"; }
    @Override protected String[] getInputHints() { return new String[]{"URL to test"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String url = inputs[0].startsWith("http") ? inputs[0] : "https://" + inputs[0];
        try {
            int code = HttpUtil.getStatusCode(url);
            String meaning = getStatusMeaning(code);
            cb.onResult("HTTP Status Test\n\nURL    : " + url +
                "\nStatus : " + code + " " + meaning + "\nClass  : " + getStatusClass(code));
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }

    private String getStatusMeaning(int code) {
        switch (code) {
            case 200: return "OK"; case 201: return "Created"; case 204: return "No Content";
            case 301: return "Moved Permanently"; case 302: return "Found"; case 304: return "Not Modified";
            case 400: return "Bad Request"; case 401: return "Unauthorized"; case 403: return "Forbidden";
            case 404: return "Not Found"; case 405: return "Method Not Allowed"; case 429: return "Too Many Requests";
            case 500: return "Internal Server Error"; case 502: return "Bad Gateway"; case 503: return "Service Unavailable";
            default: return "See RFC 9110";
        }
    }

    private String getStatusClass(int code) {
        if (code < 200) return "1xx Informational";
        if (code < 300) return "2xx Success";
        if (code < 400) return "3xx Redirection";
        if (code < 500) return "4xx Client Error";
        return "5xx Server Error";
    }
}
