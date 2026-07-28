package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.HttpUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SitemapActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "sitemap.xml Analyzer"; }
    @Override protected String getCategoryColor() { return "#00C853"; }
    @Override protected String getExecuteLabel() { return "Analyze Sitemap"; }
    @Override protected String[] getInputHints() { return new String[]{"Domain (e.g. example.com)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String host = inputs[0].replace("https://","").replace("http://","").split("/")[0];
        String url  = "https://" + host + "/sitemap.xml";
        try {
            String content = HttpUtil.get(url);
            Matcher urlMatcher = Pattern.compile("<loc>([^<]+)</loc>").matcher(content);
            int urlCount = 0;
            StringBuilder sb = new StringBuilder("sitemap.xml Analysis\n");
            sb.append("URL: ").append(url).append("\n\n");
            while (urlMatcher.find() && urlCount < 30) {
                sb.append(++urlCount).append(". ").append(urlMatcher.group(1)).append("\n");
            }
            // Count total
            int total = 0;
            Matcher tm = Pattern.compile("<loc>").matcher(content);
            while (tm.find()) total++;
            if (total > 30) sb.append("\n... and ").append(total - 30).append(" more URLs");
            sb.append("\n\nTotal URLs: ").append(total);
            boolean isSitemapIndex = content.contains("<sitemapindex");
            if (isSitemapIndex) sb.append("\nType: Sitemap Index");
            cb.onResult(sb.toString());
        } catch (Exception e) { cb.onError("Sitemap fetch failed: " + e.getMessage()); }
    }
}
