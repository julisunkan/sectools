package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;

public class UsernameSearchActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Username Search Manager"; }
    @Override protected String getCategoryColor() { return "#00BFA5"; }
    @Override protected String getExecuteLabel() { return "Generate Search URLs"; }
    @Override protected String[] getInputHints() { return new String[]{"Username to search"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String username = inputs[0].trim();
        String[][] platforms = {
            {"GitHub",      "https://github.com/"},
            {"Twitter/X",   "https://twitter.com/"},
            {"Instagram",   "https://instagram.com/"},
            {"Reddit",      "https://reddit.com/u/"},
            {"TikTok",      "https://tiktok.com/@"},
            {"YouTube",     "https://youtube.com/@"},
            {"LinkedIn",    "https://linkedin.com/in/"},
            {"Pinterest",   "https://pinterest.com/"},
            {"Twitch",      "https://twitch.tv/"},
            {"Telegram",    "https://t.me/"},
            {"Medium",      "https://medium.com/@"},
            {"Dev.to",      "https://dev.to/"},
            {"Gitlab",      "https://gitlab.com/"},
            {"HackerNews",  "https://news.ycombinator.com/user?id="},
            {"Steam",       "https://steamcommunity.com/id/"},
        };
        StringBuilder sb = new StringBuilder("Username Search: " + username + "\n\n");
        sb.append("Open these URLs to check if the username exists:\n\n");
        for (String[] p : platforms) {
            sb.append(String.format("%-12s : %s%s\n", p[0], p[1], username));
        }
        sb.append("\nNote: Copy links and open in browser to verify each profile.");
        cb.onResult(sb.toString());
    }
}
