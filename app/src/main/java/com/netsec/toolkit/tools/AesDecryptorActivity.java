package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.CryptoUtils;

public class AesDecryptorActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "AES File Decryptor"; }
    @Override protected String getCategoryColor() { return "#FF6D00"; }
    @Override protected String getExecuteLabel() { return "Decrypt"; }
    @Override protected String[] getInputHints() { return new String[]{"Ciphertext (Base64 from AES Encryptor)", "Password / Key"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        if (inputs[1].isEmpty()) { cb.onError("Password is required"); return; }
        try {
            String decrypted = CryptoUtils.aesDecrypt(inputs[0], inputs[1]);
            cb.onResult("AES-256-CBC Decryption\n\n" +
                "── Decrypted Plaintext ─────────────\n" + decrypted);
        } catch (Exception e) { cb.onError("Decryption failed: wrong password or corrupted data"); }
    }
}
