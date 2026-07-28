package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.CryptoUtils;

public class HashIdentifierActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Hash Identifier"; }
    @Override protected String getCategoryColor() { return "#D50000"; }
    @Override protected String getExecuteLabel() { return "Identify Hash"; }
    @Override protected String[] getInputHints() { return new String[]{"Hash string to identify"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String hash = inputs[0].trim();
        String identified = CryptoUtils.identifyHash(hash);
        boolean isHex = hash.matches("[0-9a-fA-F]+");
        cb.onResult(
            "Hash Identifier\n\n" +
            "Input   : " + hash + "\n" +
            "Length  : " + hash.length() + " characters\n" +
            "Is Hex  : " + (isHex ? "Yes" : "No") + "\n\n" +
            "Type    : " + identified + "\n\n" +
            "── Reference ───────────────────────\n" +
            "MD5      = 32 hex chars\n" +
            "SHA-1    = 40 hex chars\n" +
            "SHA-224  = 56 hex chars\n" +
            "SHA-256  = 64 hex chars\n" +
            "SHA-384  = 96 hex chars\n" +
            "SHA-512  = 128 hex chars\n" +
            "bcrypt   = 60 chars, starts with $2a$/$2b$\n" +
            "Argon2   = starts with $argon2\n" +
            "PBKDF2   = starts with $pbkdf2"
        );
    }
}
