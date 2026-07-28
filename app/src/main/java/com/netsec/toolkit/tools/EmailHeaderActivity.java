package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailHeaderActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Email Header Analyzer"; }
    @Override protected String getCategoryColor() { return "#00BFA5"; }
    @Override protected String getExecuteLabel() { return "Analyze Headers"; }
    @Override protected String[] getInputHints() { return new String[]{"Paste raw email headers here"}; }

    @Override
    protected boolean isInput3MultiLine() { return true; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String raw = inputs[0];
        if (raw.isEmpty()) { cb.onError("Paste email headers to analyze"); return; }
        StringBuilder sb = new StringBuilder("Email Header Analysis\n\n");

        String[] interesting = {"From","To","Subject","Date","Message-ID","X-Originating-IP",
                "Received","Reply-To","Return-Path","DKIM-Signature","Authentication-Results"};
        for (String field : interesting) {
            Pattern p = Pattern.compile("(?i)^" + field + ":(.+?)(?=^[A-Za-z\\-]+:|\\z)",
                    Pattern.MULTILINE | Pattern.DOTALL);
            Matcher m = p.matcher(raw);
            if (m.find()) {
                String val = m.group(1).trim().replaceAll("\\s+", " ");
                sb.append(String.format("%-25s : %s\n", field, val.length() > 100 ? val.substring(0,100)+"..." : val));
            }
        }
        // Received chain
        sb.append("\n── Routing Hops (Received) ─────────\n");
        Pattern rp = Pattern.compile("(?i)^Received: from (.+?)(?=^Received:|^[A-Za-z\\-]+:(?!from)|\\z)",
                Pattern.MULTILINE | Pattern.DOTALL);
        Matcher rm = rp.matcher(raw);
        int hop = 0;
        while (rm.find()) {
            sb.append(++hop).append(". ").append(rm.group(1).trim().replaceAll("\\s+", " ")).append("\n");
        }
        // SPF/DKIM/DMARC
        sb.append("\n── Authentication Results ───────────\n");
        boolean spf  = raw.toLowerCase().contains("spf=pass");
        boolean dkim = raw.toLowerCase().contains("dkim=pass");
        boolean dmarc= raw.toLowerCase().contains("dmarc=pass");
        sb.append("SPF   : ").append(spf  ? "PASS ✓" : "FAIL/MISSING ✗").append("\n");
        sb.append("DKIM  : ").append(dkim ? "PASS ✓" : "FAIL/MISSING ✗").append("\n");
        sb.append("DMARC : ").append(dmarc? "PASS ✓" : "FAIL/MISSING ✗").append("\n");
        cb.onResult(sb.toString());
    }
}
