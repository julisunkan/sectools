package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;

import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.net.URL;

public class HttpsCheckerActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "HTTPS Security Checker"; }
    @Override protected String getCategoryColor() { return "#00BCD4"; }
    @Override protected String getExecuteLabel() { return "Check HTTPS"; }
    @Override protected String[] getInputHints() { return new String[]{"Domain or URL (e.g. example.com)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String host = inputs[0].replace("https://","").replace("http://","").split("/")[0];
        try {
            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            try (SSLSocket socket = (SSLSocket) factory.createSocket(host, 443)) {
                socket.startHandshake();
                String protocol = socket.getSession().getProtocol();
                String cipher   = socket.getSession().getCipherSuite();
                java.security.cert.Certificate[] certs = socket.getSession().getPeerCertificates();
                X509Certificate cert = (X509Certificate) certs[0];

                StringBuilder sb = new StringBuilder();
                sb.append("HTTPS Security Analysis: ").append(host).append("\n\n");
                sb.append("TLS Protocol    : ").append(protocol).append(isTlsGood(protocol) ? " ✓" : " ✗").append("\n");
                sb.append("Cipher Suite    : ").append(cipher).append("\n");
                sb.append("Cert Subject    : ").append(cert.getSubjectDN().getName()).append("\n");
                sb.append("Cert Issuer     : ").append(cert.getIssuerDN().getName()).append("\n");
                sb.append("Valid From      : ").append(cert.getNotBefore()).append("\n");
                sb.append("Valid Until     : ").append(cert.getNotAfter()).append("\n");
                sb.append("Supported Protos: ").append(String.join(", ", socket.getEnabledProtocols())).append("\n\n");

                java.util.Date expiry = cert.getNotAfter();
                long daysLeft = (expiry.getTime() - System.currentTimeMillis()) / 86400000L;
                sb.append("Days until expiry: ").append(daysLeft);
                cb.onResult(sb.toString());
            }
        } catch (Exception e) {
            cb.onError("HTTPS check failed: " + e.getMessage());
        }
    }

    private boolean isTlsGood(String proto) { return proto.startsWith("TLSv1.2") || proto.startsWith("TLSv1.3"); }
}
