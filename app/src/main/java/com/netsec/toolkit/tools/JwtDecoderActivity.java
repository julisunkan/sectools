package com.netsec.toolkit.tools;

import android.util.Base64;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.FormatUtils;

import java.nio.charset.StandardCharsets;

public class JwtDecoderActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "JWT Decoder"; }
    @Override protected String getCategoryColor() { return "#00BCD4"; }
    @Override protected String getExecuteLabel() { return "Decode JWT"; }
    @Override protected String[] getInputHints() { return new String[]{"Paste JWT token here"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String token = inputs[0].trim();
        String[] parts = token.split("\\.");
        if (parts.length < 2) { cb.onError("Not a valid JWT (need at least header.payload)"); return; }
        try {
            String header  = decode(parts[0]);
            String payload = decode(parts[1]);
            StringBuilder sb = new StringBuilder("JWT Decoded\n\n");
            sb.append("── HEADER ──────────────────────────\n");
            sb.append(FormatUtils.prettyJson(header)).append("\n");
            sb.append("── PAYLOAD ─────────────────────────\n");
            sb.append(FormatUtils.prettyJson(payload)).append("\n");
            if (parts.length == 3) {
                sb.append("── SIGNATURE ───────────────────────\n");
                sb.append(parts[2]).append("\n\n");
                sb.append("Note: Signature NOT verified. Use JWT Signature Checker to verify.");
            } else {
                sb.append("Note: No signature (unsigned token).");
            }
            cb.onResult(sb.toString());
        } catch (Exception e) { cb.onError("Decode failed: " + e.getMessage()); }
    }

    private String decode(String base64Url) {
        String b = base64Url.replace("-", "+").replace("_", "/");
        switch (b.length() % 4) {
            case 2: b += "=="; break;
            case 3: b += "="; break;
        }
        return new String(Base64.decode(b, Base64.DEFAULT), StandardCharsets.UTF_8);
    }
}
