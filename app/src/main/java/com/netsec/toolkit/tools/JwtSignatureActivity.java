package com.netsec.toolkit.tools;

import android.util.Base64;

import com.netsec.toolkit.base.BaseToolActivity;

import java.nio.charset.StandardCharsets;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class JwtSignatureActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "JWT Signature Checker"; }
    @Override protected String getCategoryColor() { return "#00BCD4"; }
    @Override protected String getExecuteLabel() { return "Verify Signature"; }
    @Override protected String[] getInputHints() { return new String[]{"JWT Token", "Secret (for HS256/HS384/HS512)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String token  = inputs[0].trim();
        String secret = inputs[1];
        String[] parts = token.split("\\.");
        if (parts.length != 3) { cb.onError("JWT must have 3 parts: header.payload.signature"); return; }
        try {
            String headerJson = decodeBase64Url(parts[0]);
            String alg = extractAlg(headerJson);
            String signingInput = parts[0] + "." + parts[1];
            boolean valid = false;

            if (alg.startsWith("HS")) {
                String macAlgo = alg.equals("HS256") ? "HmacSHA256" :
                                 alg.equals("HS384") ? "HmacSHA384" : "HmacSHA512";
                Mac mac = Mac.getInstance(macAlgo);
                mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), macAlgo));
                byte[] expected = mac.doFinal(signingInput.getBytes(StandardCharsets.UTF_8));
                String expectedB64 = Base64.encodeToString(expected, Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP);
                valid = expectedB64.equals(parts[2]);
            }

            cb.onResult("JWT Signature Check\n\n" +
                "Algorithm   : " + alg + "\n" +
                "Header      : " + headerJson + "\n" +
                "Verified    : " + (valid ? "VALID ✓" : "INVALID ✗ (wrong secret or RS/ES algorithm)") + "\n\n" +
                "Note: RSA/ECDSA verification requires the public key.\n" +
                "Only HS256/HS384/HS512 can be verified with a secret.");
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }

    private String decodeBase64Url(String s) {
        String b = s.replace("-","+").replace("_","/");
        switch (b.length() % 4) { case 2: b+="=="; break; case 3: b+="="; break; }
        return new String(Base64.decode(b, Base64.DEFAULT), StandardCharsets.UTF_8);
    }

    private String extractAlg(String json) {
        try { int i = json.indexOf("\"alg\":\""); if (i<0) return "unknown";
              int s = i+7; return json.substring(s, json.indexOf("\"", s)); }
        catch (Exception e) { return "unknown"; }
    }
}
