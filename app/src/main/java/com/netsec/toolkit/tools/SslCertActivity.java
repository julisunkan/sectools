package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.CryptoUtils;

import java.security.cert.X509Certificate;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SslCertActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "SSL Certificate Viewer"; }
    @Override protected String getCategoryColor() { return "#00BCD4"; }
    @Override protected String getExecuteLabel() { return "View Certificate"; }
    @Override protected String[] getInputHints() { return new String[]{"Domain (e.g. google.com)", "Port (default 443)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String host = inputs[0].replace("https://","").replace("http://","").split("/")[0];
        int port = inputs[1].isEmpty() ? 443 : Integer.parseInt(inputs[1]);
        try {
            SSLSocketFactory f = (SSLSocketFactory) SSLSocketFactory.getDefault();
            try (SSLSocket s = (SSLSocket) f.createSocket(host, port)) {
                s.startHandshake();
                java.security.cert.Certificate[] certs = s.getSession().getPeerCertificates();
                StringBuilder sb = new StringBuilder();
                sb.append("SSL Certificate Chain for: ").append(host).append(":").append(port).append("\n");
                sb.append("Total certificates: ").append(certs.length).append("\n\n");
                for (int i = 0; i < certs.length; i++) {
                    X509Certificate cert = (X509Certificate) certs[i];
                    sb.append("── Certificate ").append(i + 1).append(" ──────────────────────\n");
                    sb.append("Subject  : ").append(cert.getSubjectDN().getName()).append("\n");
                    sb.append("Issuer   : ").append(cert.getIssuerDN().getName()).append("\n");
                    sb.append("Serial   : ").append(cert.getSerialNumber().toString(16).toUpperCase()).append("\n");
                    sb.append("Not Before: ").append(cert.getNotBefore()).append("\n");
                    sb.append("Not After : ").append(cert.getNotAfter()).append("\n");
                    sb.append("Sig Algo : ").append(cert.getSigAlgName()).append("\n");
                    sb.append("Version  : V").append(cert.getVersion()).append("\n");
                    try {
                        String fp = CryptoUtils.sha256Bytes(cert.getEncoded());
                        sb.append("SHA-256 Fingerprint:\n  ").append(fp).append("\n");
                    } catch (Exception ignored) {}
                    sb.append("\n");
                }
                cb.onResult(sb.toString());
            }
        } catch (Exception e) {
            cb.onError(e.getMessage());
        }
    }
}
