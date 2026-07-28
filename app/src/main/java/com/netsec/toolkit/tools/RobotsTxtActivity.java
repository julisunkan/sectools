package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.HttpUtil;

public class RobotsTxtActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "robots.txt Viewer"; }
    @Override protected String getCategoryColor() { return "#00C853"; }
    @Override protected String getExecuteLabel() { return "Fetch robots.txt"; }
    @Override protected String[] getInputHints() { return new String[]{"Domain (e.g. example.com)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String host = inputs[0].replace("https://","").replace("http://","").split("/")[0];
        String url = "https://" + host + "/robots.txt";
        try {
            String content = HttpUtil.get(url);
            int disallowCount = 0;
            for (String line : content.split("\n")) {
                if (line.toLowerCase().startsWith("disallow")) disallowCount++;
            }
            cb.onResult("robots.txt for: " + host + "\n" +
                "URL: " + url + "\n" +
                "Disallow rules: " + disallowCount + "\n\n" +
                "──────────────────────────────────\n" +
                content);
        } catch (Exception e) {
            cb.onError("Could not fetch robots.txt: " + e.getMessage());
        }
    }
}
