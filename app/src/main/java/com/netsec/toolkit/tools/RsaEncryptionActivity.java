package com.netsec.toolkit.tools;

import android.util.Base64;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.CryptoUtils;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RsaEncryptionActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "RSA Encryption Demo"; }
    @Override protected String getCategoryColor() { return "#FF6D00"; }
    @Override protected String getExecuteLabel() { return "Encrypt / Decrypt"; }
    @Override protected String[] getInputHints() { return new String[]{"Message (plaintext or ciphertext)", "Key (Base64 public key to encrypt, or private key to decrypt)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String msg = inputs[0];
        String key = inputs[1].trim();
        if (key.isEmpty()) { cb.onError("Paste a Base64 RSA key"); return; }
        StringBuilder sb = new StringBuilder("RSA Encryption Demo\n\n");
        try {
            // Try public key (encrypt)
            try {
                byte[] keyBytes = Base64.decode(key, Base64.DEFAULT);
                PublicKey pubKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
                String encrypted = CryptoUtils.rsaEncrypt(msg, pubKey);
                sb.append("── ENCRYPTED (with public key) ─────\n").append(encrypted);
                cb.onResult(sb.toString()); return;
            } catch (Exception ignored) {}
            // Try private key (decrypt)
            try {
                byte[] keyBytes = Base64.decode(key, Base64.DEFAULT);
                PrivateKey privKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
                String decrypted = CryptoUtils.rsaDecrypt(msg, privKey);
                sb.append("── DECRYPTED (with private key) ────\n").append(decrypted);
                cb.onResult(sb.toString()); return;
            } catch (Exception ignored) {}
            cb.onError("Could not parse key. Use Base64-encoded public or private key from RSA Key Generator.");
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }
}
