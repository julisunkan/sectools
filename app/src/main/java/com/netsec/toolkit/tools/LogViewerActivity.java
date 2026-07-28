package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;

public class LogViewerActivity extends BaseToolActivity {
    @Override protected String getToolTitle()    { return "Log File Viewer"; }
    @Override protected String getCategoryColor(){ return "#546E7A"; }
    @Override protected String getExecuteLabel() { return "Read Log"; }
    @Override protected String[] getInputHints() { return new String[]{"Log file path (e.g. /proc/kmsg or paste log text below)", "Paste log content here (if no file path)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String path    = inputs[0].trim();
        String content = inputs[1].trim();
        if (!content.isEmpty()) { cb.onResult(analyzeLog(content)); return; }
        if (!path.isEmpty()) {
            try {
                java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(path));
                StringBuilder sb = new StringBuilder();
                String line; int count = 0;
                while ((line = br.readLine()) != null && count++ < 500) sb.append(line).append("\n");
                br.close();
                cb.onResult(analyzeLog(sb.toString()));
            } catch (Exception e) { cb.onError("Cannot read file: " + e.getMessage()); }
        } else cb.onError("Provide a file path or paste log content");
    }

    private String analyzeLog(String log) {
        long errors   = log.lines().filter(l -> l.toLowerCase().contains("error")).count();
        long warnings = log.lines().filter(l -> l.toLowerCase().contains("warn")).count();
        long lines    = log.lines().count();
        return "Log Analysis\n\nTotal lines : " + lines + "\nErrors      : " + errors +
               "\nWarnings    : " + warnings + "\n\n── Content ──────────────────────────\n" +
               (log.length() > 3000 ? log.substring(0, 3000) + "\n[truncated...]" : log);
    }
}
