package com.netsec.toolkit.tools;

import android.util.Base64;

import com.netsec.toolkit.base.BaseToolActivity;

import java.nio.charset.StandardCharsets;

public class Base64Activity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Base64 Encoder / Decoder"; }
    @Override protected String getCategoryColor() { return "#00BCD4"; }
    @Override protected String getExecuteLabel() { return "Encode / Decode"; }
    @Override protected String[] getInputHints() { return new String[]{"Text to encode OR base64 to decode"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String input = inputs[0];
        StringBuilder sb = new StringBuilder();
        // Try decode first
        try {
            byte[] decoded = Base64.decode(input, Base64.DEFAULT);
            String decodedStr = new String(decoded, StandardCharsets.UTF_8);
            sb.append("── DECODED (Base64 → Text) ─────────\n");
            sb.append(decodedStr).append("\n\n");
        } catch (Exception ignored) {
            sb.append("── DECODED ─────────────────────────\n");
            sb.append("(Not valid Base64)\n\n");
        }
        // Also encode
        String encoded = Base64.encodeToString(input.getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP);
        sb.append("── ENCODED (Text → Base64) ─────────\n");
        sb.append(encoded).append("\n\n");
        // URL-safe
        String urlSafe = Base64.encodeToString(input.getBytes(StandardCharsets.UTF_8), Base64.URL_SAFE | Base64.NO_WRAP);
        sb.append("── URL-SAFE Base64 ─────────────────\n");
        sb.append(urlSafe);
        cb.onResult(sb.toString());
    }
}
