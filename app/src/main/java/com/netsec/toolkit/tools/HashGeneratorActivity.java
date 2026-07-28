package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.CryptoUtils;

public class HashGeneratorActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Hash Generator"; }
    @Override protected String getCategoryColor() { return "#D50000"; }
    @Override protected String getExecuteLabel() { return "Generate Hashes"; }
    @Override protected String[] getInputHints() { return new String[]{"Text to hash"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String text = inputs[0];
        try {
            cb.onResult(
                "Hash Generator Results\n\n" +
                "Input   : " + text + "\n" +
                "Length  : " + text.length() + " chars\n\n" +
                "MD5     : " + CryptoUtils.md5(text) + "\n" +
                "SHA-1   : " + CryptoUtils.sha1(text) + "\n" +
                "SHA-256 : " + CryptoUtils.sha256(text) + "\n" +
                "SHA-512 : " + CryptoUtils.sha512(text)
            );
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }
}
