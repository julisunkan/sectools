package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.CryptoUtils;

public class AesEncryptorActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "AES File Encryptor"; }
    @Override protected String getCategoryColor() { return "#FF6D00"; }
    @Override protected String getExecuteLabel() { return "Encrypt"; }
    @Override protected String[] getInputHints() { return new String[]{"Plaintext to encrypt", "Password / Key"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        if (inputs[1].isEmpty()) { cb.onError("Password is required"); return; }
        try {
            String encrypted = CryptoUtils.aesEncrypt(inputs[0], inputs[1]);
            cb.onResult("AES-256-CBC Encryption\n\n" +
                "Algorithm  : AES-256-CBC with PBKDF2 key derivation\n" +
                "Iterations : 65536\n\n" +
                "── Ciphertext (Base64) ─────────────\n" + encrypted + "\n\n" +
                "Save this encrypted output. Use the AES Decryptor with the same password to decrypt.");
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }
}
