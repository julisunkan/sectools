package com.netsec.toolkit.tools;

import android.util.Base64;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.CryptoUtils;

import java.security.SecureRandom;

public class SecureRandomActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Secure Random Generator"; }
    @Override protected String getCategoryColor() { return "#FF6D00"; }
    @Override protected String getExecuteLabel() { return "Generate"; }
    @Override protected String[] getInputHints() { return new String[]{"Number of bytes (default 32)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        int bytes = 32;
        try { bytes = Integer.parseInt(inputs[0]); } catch (Exception ignored) {}
        if (bytes < 1 || bytes > 512) bytes = 32;

        SecureRandom rng = new SecureRandom();
        byte[] data = new byte[bytes];
        rng.nextBytes(data);

        StringBuilder sb = new StringBuilder("Secure Random Generator\n\n");
        sb.append("Source    : java.security.SecureRandom\n");
        sb.append("Bytes     : ").append(bytes).append("\n\n");
        sb.append("── Hex ──────────────────────────────\n");
        sb.append(CryptoUtils.bytesToHex(data)).append("\n\n");
        sb.append("── Base64 ───────────────────────────\n");
        sb.append(Base64.encodeToString(data, Base64.NO_WRAP)).append("\n\n");
        sb.append("── Integers (10 random) ─────────────\n");
        for (int i = 0; i < 10; i++) sb.append(rng.nextInt(Integer.MAX_VALUE)).append("\n");
        sb.append("\n── UUID v4 ──────────────────────────\n");
        sb.append(java.util.UUID.randomUUID().toString()).append("\n");
        sb.append(java.util.UUID.randomUUID().toString()).append("\n");
        sb.append(java.util.UUID.randomUUID().toString());
        cb.onResult(sb.toString());
    }
}
