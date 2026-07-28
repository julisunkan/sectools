package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.HttpUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecurityBulletinActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Security Bulletin Reader"; }
    @Override protected String getCategoryColor() { return "#DD2C00"; }
    @Override protected String getExecuteLabel() { return "Load CISA Bulletins"; }
    @Override protected String[] getInputHints() { return new String[0]; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        try {
            String rss = HttpUtil.get("https://www.cisa.gov/uscert/ncas/alerts.xml");
            StringBuilder sb = new StringBuilder("CISA Security Bulletins\n\n");
            Pattern titlePat = Pattern.compile("<title>([^<]+)</title>");
            Pattern linkPat  = Pattern.compile("<link>([^<]+)</link>");
            Pattern datePat  = Pattern.compile("<pubDate>([^<]+)</pubDate>");
            Matcher titles = titlePat.matcher(rss);
            Matcher links  = linkPat.matcher(rss);
            Matcher dates  = datePat.matcher(rss);
            int count = 0;
            titles.find(); // skip feed title
            links.find();  // skip feed link
            while (titles.find() && count < 20) {
                String title = titles.group(1).trim();
                String link  = links.find() ? links.group(1).trim() : "";
                String date  = dates.find() ? dates.group(1).trim().substring(0,16) : "";
                sb.append(++count).append(". ").append(title).append("\n");
                if (!date.isEmpty()) sb.append("   Date: ").append(date).append("\n");
                if (!link.isEmpty()) sb.append("   Link: ").append(link).append("\n");
                sb.append("\n");
            }
            cb.onResult(sb.toString());
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }
}
