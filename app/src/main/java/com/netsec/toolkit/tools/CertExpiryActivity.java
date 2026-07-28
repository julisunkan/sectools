package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;

import java.security.cert.X509Certificate;
import java.util.Date;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class CertExpiryActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "Certificate Expiry Monitor"; }
    @Override protected String getCategoryColor() { return "#00BCD4"; }
    @Override protected String getExecuteLabel() { return "Check Expiry"; }
    @Override protected String[] getInputHints() { return new String[]{"Domain (e.g. example.com)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String host = inputs[0].replace("https://","").replace("http://","").split("/")[0];
        try {
            SSLSocketFactory f = (SSLSocketFactory) SSLSocketFactory.getDefault();
            try (SSLSocket s = (SSLSocket) f.createSocket(host, 443)) {
                s.startHandshake();
                X509Certificate cert = (X509Certificate) s.getSession().getPeerCertificates()[0];
                Date expiry = cert.getNotAfter();
                long daysLeft = (expiry.getTime() - System.currentTimeMillis()) / 86400000L;
                String status = daysLeft > 30 ? "VALID" : daysLeft > 0 ? "EXPIRING SOON" : "EXPIRED";
                cb.onResult(
                    "Domain       : " + host + "\n" +
                    "Subject      : " + cert.getSubjectDN().getName() + "\n" +
                    "Issuer       : " + cert.getIssuerDN().getName() + "\n" +
                    "Not Before   : " + cert.getNotBefore() + "\n" +
                    "Not After    : " + expiry + "\n" +
                    "Days Left    : " + daysLeft + "\n" +
                    "Status       : " + status
                );
            }
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }
}
