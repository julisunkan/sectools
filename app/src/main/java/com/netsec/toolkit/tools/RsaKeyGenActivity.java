package com.netsec.toolkit.tools;

import android.util.Base64;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.CryptoUtils;

import java.security.KeyPair;

public class RsaKeyGenActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "RSA Key Generator"; }
    @Override protected String getCategoryColor() { return "#FF6D00"; }
    @Override protected String getExecuteLabel() { return "Generate RSA-2048 Key Pair"; }
    @Override protected String[] getInputHints() { return new String[0]; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        try {
            cb.onResult("Generating RSA-2048 key pair...\n(this may take a few seconds)");
            KeyPair kp = CryptoUtils.generateRsaKeyPair();
            String pub  = Base64.encodeToString(kp.getPublic().getEncoded(),  Base64.NO_WRAP);
            String priv = Base64.encodeToString(kp.getPrivate().getEncoded(), Base64.NO_WRAP);
            cb.onResult(
                "RSA-2048 Key Pair Generated\n\n" +
                "Algorithm: " + kp.getPublic().getAlgorithm() + "\n" +
                "Format   : " + kp.getPublic().getFormat() + "\n\n" +
                "── PUBLIC KEY (Base64) ──────────────\n" + pub + "\n\n" +
                "── PRIVATE KEY (Base64) ─────────────\n" +
                "⚠ KEEP PRIVATE — Never share!\n" + priv + "\n\n" +
                "Use RSA Encryption Demo to encrypt/decrypt with these keys."
            );
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }
}
