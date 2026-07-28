package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.CryptoUtils;

import java.nio.charset.StandardCharsets;

public class ChecksumVerifierActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Checksum Verifier"; }
    @Override protected String getCategoryColor() { return "#D50000"; }
    @Override protected String getExecuteLabel() { return "Verify / Generate"; }
    @Override protected String[] getInputHints() { return new String[]{"Text or file path to checksum", "Expected checksum (optional — leave blank to generate)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String input    = inputs[0];
        String expected = inputs[1].trim().toLowerCase();
        try {
            byte[] data = input.getBytes(StandardCharsets.UTF_8);
            String md5    = CryptoUtils.hash(input, "MD5");
            String sha1   = CryptoUtils.hash(input, "SHA-1");
            String sha256 = CryptoUtils.hash(input, "SHA-256");
            long   crc32  = CryptoUtils.crc32(data);

            StringBuilder sb = new StringBuilder("Checksum Results\n\n");
            sb.append("Input     : ").append(input.length() > 50 ? input.substring(0,50)+"..." : input).append("\n");
            sb.append("Bytes     : ").append(data.length).append("\n\n");
            sb.append("CRC-32    : ").append(Long.toHexString(crc32).toUpperCase()).append("\n");
            sb.append("MD5       : ").append(md5).append("\n");
            sb.append("SHA-1     : ").append(sha1).append("\n");
            sb.append("SHA-256   : ").append(sha256).append("\n");

            if (!expected.isEmpty()) {
                boolean match = md5.equals(expected) || sha1.equals(expected) || sha256.equals(expected);
                sb.append("\n── Verification ────────────────────\n");
                sb.append("Expected  : ").append(expected).append("\n");
                sb.append("Result    : ").append(match ? "✓ MATCH" : "✗ MISMATCH");
            }
            cb.onResult(sb.toString());
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }
}
