package com.netsec.toolkit.tools;

import com.netsec.toolkit.api.ApiConfig;
import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.CryptoUtils;
import com.netsec.toolkit.utils.HttpUtil;

import java.util.HashMap;
import java.util.Map;

public class FileIntegrityActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "File Integrity Checker"; }
    @Override protected String getCategoryColor() { return "#FF6D00"; }
    @Override protected String getExecuteLabel() { return "Check Integrity"; }
    @Override protected String[] getInputHints() { return new String[]{"Text content or SHA-256 hash to check on VirusTotal"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String input = inputs[0].trim();
        StringBuilder sb = new StringBuilder("File Integrity Checker\n\n");

        // Generate hash of input text
        try {
            String sha256 = CryptoUtils.sha256(input);
            String sha1   = CryptoUtils.sha1(input);
            String md5    = CryptoUtils.md5(input);
            sb.append("Input Length : ").append(input.length()).append(" chars\n");
            sb.append("MD5          : ").append(md5).append("\n");
            sb.append("SHA-1        : ").append(sha1).append("\n");
            sb.append("SHA-256      : ").append(sha256).append("\n\n");

            // Check VirusTotal if API key configured and input looks like a hash
            String vtKey = ApiConfig.get(this).virusTotal();
            String hashToCheck = input.matches("[0-9a-fA-F]{32,128}") ? input : sha256;
            if (!vtKey.isEmpty()) {
                sb.append("── VirusTotal ───────────────────────\n");
                try {
                    String url = "https://www.virustotal.com/api/v3/files/" + hashToCheck;
                    Map<String, String> headers = new HashMap<>();
                    headers.put("x-apikey", vtKey);
                    String raw = HttpUtil.get(url, headers);
                    org.json.JSONObject obj = new org.json.JSONObject(raw);
                    org.json.JSONObject attrs = obj.optJSONObject("data") != null ? obj.getJSONObject("data").optJSONObject("attributes") : null;
                    if (attrs != null) {
                        org.json.JSONObject stats = attrs.optJSONObject("last_analysis_stats");
                        if (stats != null) {
                            sb.append("Malicious  : ").append(stats.optInt("malicious",0)).append("\n");
                            sb.append("Suspicious : ").append(stats.optInt("suspicious",0)).append("\n");
                            sb.append("Clean      : ").append(stats.optInt("undetected",0)).append("\n");
                        }
                    } else sb.append("Not found in VirusTotal database.\n");
                } catch (Exception e) { sb.append("VT Error: ").append(e.getMessage()).append("\n"); }
            } else {
                sb.append("Configure VirusTotal API key in Settings to check this hash online.");
            }
        } catch (Exception e) { sb.append("Error: ").append(e.getMessage()); }
        cb.onResult(sb.toString());
    }
}
