package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;

public class MetadataInspectorActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Metadata Inspector"; }
    @Override protected String getCategoryColor() { return "#00BFA5"; }
    @Override protected String getExecuteLabel() { return "Inspect"; }
    @Override protected String[] getInputHints() { return new String[]{"Paste text content to inspect for metadata"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String content = inputs[0];
        StringBuilder sb = new StringBuilder("Metadata Inspector\n\n");
        sb.append("── Text Statistics ─────────────────\n");
        sb.append("Length     : ").append(content.length()).append(" characters\n");
        sb.append("Lines      : ").append(content.split("\n").length).append("\n");
        sb.append("Words      : ").append(content.trim().isEmpty() ? 0 : content.trim().split("\\s+").length).append("\n\n");
        // Look for common metadata patterns
        sb.append("── Detected Metadata ────────────────\n");
        String lc = content.toLowerCase();
        if (lc.contains("author:") || lc.contains("creator:")) sb.append("Author field found\n");
        if (lc.contains("created:") || lc.contains("date:")) sb.append("Date field found\n");
        if (lc.contains("version:")) sb.append("Version field found\n");
        if (lc.contains("copyright:")) sb.append("Copyright field found\n");
        if (lc.contains("gps") || lc.contains("latitude") || lc.contains("longitude")) sb.append("GPS/Location data found!\n");
        if (lc.contains("windows") || lc.contains("mac os") || lc.contains("linux")) sb.append("OS reference found\n");
        if (lc.matches(".*\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}.*")) sb.append("IP address pattern found\n");
        if (lc.matches(".*[a-z0-9._%+\\-]+@[a-z0-9.\\-]+\\.[a-z]{2,}.*")) sb.append("Email address pattern found\n");
        sb.append("\n── First 500 chars ─────────────────\n");
        sb.append(content.substring(0, Math.min(500, content.length())));
        cb.onResult(sb.toString());
    }
}
