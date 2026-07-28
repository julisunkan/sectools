package com.netsec.toolkit.tools;

import android.util.Base64;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.CryptoUtils;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class DigitalSignatureActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Digital Signature Demo"; }
    @Override protected String getCategoryColor() { return "#FF6D00"; }
    @Override protected String getExecuteLabel() { return "Sign / Verify"; }
    @Override protected String[] getInputHints() { return new String[]{"Data to sign OR 'data|signature' to verify", "Private key (Base64) to sign, or Public key (Base64) to verify — blank = generate new keys"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String data = inputs[0];
        String key  = inputs[1].trim();
        try {
            if (key.isEmpty()) {
                // Generate demo key pair and sign
                KeyPair kp = CryptoUtils.generateRsaKeyPair();
                String sig  = CryptoUtils.sign(data, kp.getPrivate());
                boolean verified = CryptoUtils.verify(data, sig, kp.getPublic());
                cb.onResult(
                    "Digital Signature Demo\n\n" +
                    "Data      : " + data + "\n\n" +
                    "── Signature (Base64) ──────────────\n" + sig + "\n\n" +
                    "── Verification ────────────────────\n" + (verified ? "VALID ✓" : "INVALID ✗") + "\n\n" +
                    "── Public Key ──────────────────────\n" + Base64.encodeToString(kp.getPublic().getEncoded(), Base64.NO_WRAP)
                );
            } else if (data.contains("|")) {
                // Verify mode: data|signature
                String[] parts = data.split("\\|", 2);
                byte[] keyBytes = Base64.decode(key, Base64.DEFAULT);
                PublicKey pubKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
                boolean valid = CryptoUtils.verify(parts[0], parts[1], pubKey);
                cb.onResult("Signature Verification\n\nData: " + parts[0] + "\nResult: " + (valid ? "VALID ✓" : "INVALID ✗"));
            } else {
                // Sign mode
                byte[] keyBytes = Base64.decode(key, Base64.DEFAULT);
                PrivateKey privKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
                String sig = CryptoUtils.sign(data, privKey);
                cb.onResult("Data signed successfully.\n\nSignature:\n" + sig);
            }
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }
}
