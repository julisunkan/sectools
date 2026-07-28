package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.CryptoUtils;

public class PasswordEntropyActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Password Entropy Calculator"; }
    @Override protected String getCategoryColor() { return "#D50000"; }
    @Override protected String getExecuteLabel() { return "Calculate Entropy"; }
    @Override protected String[] getInputHints() { return new String[]{"Password to analyze"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String pwd = inputs[0];
        double entropy = CryptoUtils.passwordEntropy(pwd);
        String crackTime = getCrackTime(entropy);
        cb.onResult(
            "Password Entropy Analysis\n\n" +
            "Password        : " + maskPassword(pwd) + "\n" +
            "Length          : " + pwd.length() + "\n" +
            "Entropy         : " + String.format("%.2f", entropy) + " bits\n" +
            "Security Level  : " + getLevel(entropy) + "\n" +
            "Est. Crack Time : " + crackTime + "\n\n" +
            "── Entropy Scale ───────────────────\n" +
            "< 28 bits   : Very Weak\n" +
            "28–35 bits  : Weak\n" +
            "36–59 bits  : Reasonable\n" +
            "60–127 bits : Strong\n" +
            ">= 128 bits : Very Strong\n\n" +
            "At 10^12 guesses/sec (modern GPU cluster)"
        );
    }

    private String maskPassword(String pwd) {
        if (pwd.length() <= 4) return "****";
        return pwd.charAt(0) + "*".repeat(pwd.length() - 2) + pwd.charAt(pwd.length() - 1);
    }

    private String getLevel(double e) {
        if (e < 28) return "VERY WEAK";
        if (e < 36) return "WEAK";
        if (e < 60) return "REASONABLE";
        if (e < 128) return "STRONG";
        return "VERY STRONG";
    }

    private String getCrackTime(double entropy) {
        double combos = Math.pow(2, entropy);
        double guessPerSec = 1e12;
        double seconds = combos / (2 * guessPerSec);
        if (seconds < 1) return "< 1 second";
        if (seconds < 60) return String.format("%.0f seconds", seconds);
        if (seconds < 3600) return String.format("%.0f minutes", seconds / 60);
        if (seconds < 86400) return String.format("%.1f hours", seconds / 3600);
        if (seconds < 3.15e7) return String.format("%.1f days", seconds / 86400);
        if (seconds < 3.15e9) return String.format("%.1f years", seconds / 3.15e7);
        return String.format("%.2e years", seconds / 3.15e7);
    }
}
