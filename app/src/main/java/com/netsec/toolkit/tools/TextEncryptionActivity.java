package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.CryptoUtils;

public class TextEncryptionActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Text Encryption Utility"; }
    @Override protected String getCategoryColor() { return "#FF6D00"; }
    @Override protected String getExecuteLabel() { return "Encrypt / Decrypt"; }
    @Override protected String[] getInputHints() { return new String[]{"Text (plaintext or ciphertext)", "Password"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String text = inputs[0];
        String pass = inputs[1];
        if (pass.isEmpty()) { cb.onError("Password is required"); return; }
        StringBuilder sb = new StringBuilder("Text Encryption Utility\n\n");
        // Try decrypt first
        try {
            String dec = CryptoUtils.aesDecrypt(text, pass);
            sb.append("── DECRYPTED ───────────────────────\n").append(dec).append("\n\n");
        } catch (Exception ignored) {
            sb.append("── DECRYPTED ───────────────────────\n(Not valid ciphertext or wrong password)\n\n");
        }
        // Also encrypt
        try {
            String enc = CryptoUtils.aesEncrypt(text, pass);
            sb.append("── ENCRYPTED ───────────────────────\n").append(enc);
        } catch (Exception e) { sb.append("Encrypt error: ").append(e.getMessage()); }
        cb.onResult(sb.toString());
    }
}
