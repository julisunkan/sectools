package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.CryptoUtils;

public class PasswordStrengthActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Password Strength Checker"; }
    @Override protected String getCategoryColor() { return "#D50000"; }
    @Override protected String getExecuteLabel() { return "Check Strength"; }
    @Override protected String[] getInputHints() { return new String[]{"Enter password to analyze"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String pwd = inputs[0];
        if (pwd.isEmpty()) { cb.onError("Please enter a password"); return; }

        int score = 0;
        StringBuilder feedback = new StringBuilder();

        boolean hasUpper   = !pwd.equals(pwd.toLowerCase());
        boolean hasLower   = !pwd.equals(pwd.toUpperCase());
        boolean hasDigit   = pwd.matches(".*\\d.*");
        boolean hasSpecial = pwd.matches(".*[!@#$%^&*()_+\\-=\\[\\]{}|;':\",./<>?].*");
        boolean hasSpace   = pwd.contains(" ");
        int length = pwd.length();

        if (length >= 8)  { score++; } else feedback.append("✗ Too short (< 8 chars)\n");
        if (length >= 12) { score++; } else feedback.append("✗ Recommended: 12+ chars\n");
        if (length >= 16)   score++;
        if (hasUpper)   { score++; } else feedback.append("✗ Add uppercase letters\n");
        if (hasLower)   { score++; } else feedback.append("✗ Add lowercase letters\n");
        if (hasDigit)   { score++; } else feedback.append("✗ Add digits\n");
        if (hasSpecial) { score++; } else feedback.append("✗ Add special characters\n");
        if (!hasSpace)    score++;

        double entropy = CryptoUtils.passwordEntropy(pwd);
        String strength = score <= 2 ? "VERY WEAK" : score <= 4 ? "WEAK" : score <= 6 ? "MODERATE" : "STRONG";

        cb.onResult(
            "Password Strength Analysis\n\n" +
            "Length    : " + length + " characters\n" +
            "Uppercase : " + (hasUpper   ? "✓" : "✗") + "\n" +
            "Lowercase : " + (hasLower   ? "✓" : "✗") + "\n" +
            "Digits    : " + (hasDigit   ? "✓" : "✗") + "\n" +
            "Special   : " + (hasSpecial ? "✓" : "✗") + "\n" +
            "Score     : " + score + "/8\n" +
            "Entropy   : " + String.format("%.1f", entropy) + " bits\n" +
            "Strength  : " + strength + "\n\n" +
            "Suggestions:\n" + (feedback.length() == 0 ? "✓ Excellent password!" : feedback.toString())
        );
    }
}
