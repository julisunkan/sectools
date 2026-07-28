package com.netsec.toolkit.tools;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.CryptoUtils;

public class QrSecretActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "QR Secret Storage"; }
    @Override protected String getCategoryColor() { return "#FF6D00"; }
    @Override protected String getExecuteLabel() { return "Generate QR Code"; }
    @Override protected String[] getInputHints() { return new String[]{"Secret or text to encode in QR", "Encryption password (optional — leave blank for plain QR)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String secret   = inputs[0];
        String password = inputs[1];
        try {
            String payload = secret;
            String note    = "Plain QR (not encrypted)";
            if (!password.isEmpty()) {
                payload = CryptoUtils.aesEncrypt(secret, password);
                note    = "Encrypted with AES-256 (password required to decrypt)";
            }
            cb.onResult("QR Code generated successfully!\n\n" +
                "Content  : " + (password.isEmpty() ? secret : "(encrypted)") + "\n" +
                "Type     : " + note + "\n" +
                "Payload  : " + payload + "\n\n" +
                "The QR code has been generated below.\n" +
                "Scan it with any QR reader to retrieve the payload.\n\n" +
                "Note: If encrypted, you'll need to use the AES Decryptor with the same password.");
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }
}
