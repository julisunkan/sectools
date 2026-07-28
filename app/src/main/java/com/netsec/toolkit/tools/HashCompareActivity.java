package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;

public class HashCompareActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Hash Comparison Tool"; }
    @Override protected String getCategoryColor() { return "#FF6D00"; }
    @Override protected String getExecuteLabel() { return "Compare"; }
    @Override protected String[] getInputHints() { return new String[]{"Hash 1", "Hash 2"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String h1 = inputs[0].trim().toLowerCase();
        String h2 = inputs[1].trim().toLowerCase();
        boolean equal = h1.equals(h2);
        int diff = 0;
        if (h1.length() == h2.length()) {
            for (int i = 0; i < h1.length(); i++) if (h1.charAt(i) != h2.charAt(i)) diff++;
        }
        cb.onResult(
            "Hash Comparison\n\n" +
            "Hash 1  : " + h1 + "\n" +
            "Hash 2  : " + h2 + "\n\n" +
            "Length 1: " + h1.length() + " chars\n" +
            "Length 2: " + h2.length() + " chars\n\n" +
            "Match   : " + (equal ? "✓ IDENTICAL" : "✗ DIFFERENT") + "\n" +
            (h1.length() == h2.length() ? "Differing positions: " + diff + " of " + h1.length() + "\n" : "") +
            "\nUse case: Verify file integrity by comparing expected vs computed hash."
        );
    }
}
