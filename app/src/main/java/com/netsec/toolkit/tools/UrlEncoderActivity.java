package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class UrlEncoderActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "URL Encoder / Decoder"; }
    @Override protected String getCategoryColor() { return "#00C853"; }
    @Override protected String getExecuteLabel() { return "Encode / Decode"; }
    @Override protected String[] getInputHints() { return new String[]{"Text to encode OR URL-encoded string to decode"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String input = inputs[0];
        StringBuilder sb = new StringBuilder();
        try {
            String encoded = URLEncoder.encode(input, "UTF-8").replace("+", "%20");
            sb.append("── URL ENCODED ─────────────────────\n").append(encoded).append("\n\n");
        } catch (Exception e) { sb.append("Encode error: ").append(e.getMessage()).append("\n\n"); }
        try {
            String decoded = URLDecoder.decode(input, "UTF-8");
            sb.append("── URL DECODED ─────────────────────\n").append(decoded).append("\n\n");
        } catch (Exception e) { sb.append("Decode error: ").append(e.getMessage()).append("\n\n"); }
        // Double encode
        try {
            String d2 = URLEncoder.encode(URLEncoder.encode(input, "UTF-8"), "UTF-8");
            sb.append("── DOUBLE ENCODED ──────────────────\n").append(d2);
        } catch (Exception ignored) {}
        cb.onResult(sb.toString());
    }
}
