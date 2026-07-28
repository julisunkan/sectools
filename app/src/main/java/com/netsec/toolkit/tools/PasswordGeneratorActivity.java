package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.CryptoUtils;

public class PasswordGeneratorActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Password Generator"; }
    @Override protected String getCategoryColor() { return "#D50000"; }
    @Override protected String getExecuteLabel() { return "Generate Passwords"; }
    @Override protected String[] getInputHints() { return new String[]{"Length (default 16)", "Options: u=uppercase l=lowercase d=digits s=special (default: ulds)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        int length = 16;
        try { length = Integer.parseInt(inputs[0]); } catch (Exception ignored) {}
        if (length < 4 || length > 128) length = 16;

        String opts = inputs[1].isEmpty() ? "ulds" : inputs[1].toLowerCase();
        boolean upper   = opts.contains("u");
        boolean lower   = opts.contains("l") || opts.isEmpty();
        boolean digits  = opts.contains("d");
        boolean special = opts.contains("s");

        StringBuilder sb = new StringBuilder("Generated Passwords (length=" + length + ")\n\n");
        for (int i = 1; i <= 5; i++) {
            String pwd = CryptoUtils.generatePassword(length, upper, lower, digits, special);
            double entropy = CryptoUtils.passwordEntropy(pwd);
            sb.append(i).append(". ").append(pwd)
              .append("  [").append(String.format("%.0f", entropy)).append(" bits]\n");
        }
        sb.append("\n── Passphrases ─────────────────────\n");
        String[] words = {"correct","horse","battery","staple","mountain","river","cloud","forest",
                          "apple","orange","delta","secure","random","crypto","network","shield"};
        java.security.SecureRandom rng = new java.security.SecureRandom();
        for (int i = 1; i <= 3; i++) {
            StringBuilder passphrase = new StringBuilder();
            for (int j = 0; j < 4; j++) {
                if (j > 0) passphrase.append("-");
                passphrase.append(words[rng.nextInt(words.length)]);
            }
            sb.append(i).append(". ").append(passphrase).append("\n");
        }
        cb.onResult(sb.toString());
    }
}
