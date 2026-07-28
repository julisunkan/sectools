package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.HttpUtil;

import java.util.LinkedHashSet;
import java.util.Set;

public class SubdomainEnumActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Passive Subdomain Enumerator"; }
    @Override protected String getCategoryColor() { return "#AA00FF"; }
    @Override protected String getExecuteLabel() { return "Enumerate Subdomains"; }
    @Override protected String[] getInputHints() { return new String[]{"Domain (e.g. example.com)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String domain = inputs[0].trim();
        try {
            String url = "https://crt.sh/?q=%25." + domain + "&output=json";
            String raw = HttpUtil.get(url);
            org.json.JSONArray arr = new org.json.JSONArray(raw);
            Set<String> subs = new LinkedHashSet<>();
            for (int i = 0; i < arr.length(); i++) {
                String name = arr.getJSONObject(i).optString("common_name","");
                String nameValue = arr.getJSONObject(i).optString("name_value","");
                for (String n : (name + "\n" + nameValue).split("\n")) {
                    n = n.trim().toLowerCase();
                    if (n.endsWith("." + domain) || n.equals(domain)) subs.add(n);
                }
            }
            StringBuilder sb = new StringBuilder("Passive Subdomain Enumeration\n");
            sb.append("Domain: ").append(domain).append("\n");
            sb.append("Source: crt.sh (Certificate Transparency)\n");
            sb.append("Found : ").append(subs.size()).append(" unique subdomains\n\n");
            int i = 1;
            for (String sub : subs) sb.append(i++).append(". ").append(sub).append("\n");
            cb.onResult(sb.toString());
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }
}
