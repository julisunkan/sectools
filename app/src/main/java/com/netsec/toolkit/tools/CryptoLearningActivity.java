package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;

public class CryptoLearningActivity extends BaseToolActivity {
    @Override protected String getToolTitle()    { return "Cryptography Learning App"; }
    @Override protected String getCategoryColor(){ return "#FF6D00"; }
    @Override protected String getExecuteLabel() { return "Learn"; }
    @Override protected String[] getInputHints() { return new String[]{"Topic: aes / rsa / hash / tls / jwt / pki / all (default: all)"}; }

    private static final String[][] LESSONS = {
        {"AES (Advanced Encryption Standard)","Symmetric block cipher. Uses 128/192/256-bit keys. Modes: ECB (insecure), CBC, GCM. GCM provides authenticated encryption (AEAD). Used in: HTTPS, file encryption, disk encryption."},
        {"RSA (Rivest–Shamir–Adleman)","Asymmetric (public-key) encryption. Based on integer factorization. Key sizes: 2048-bit (minimum), 4096-bit (secure). Uses: key exchange, digital signatures, certificates. Slower than symmetric."},
        {"SHA (Secure Hash Algorithm)","One-way hash functions. SHA-1 (broken), SHA-256, SHA-512 (secure). Properties: deterministic, fast, preimage resistant, collision resistant. Uses: integrity, password hashing, digital signatures."},
        {"TLS (Transport Layer Security)","Protocol securing network communications. TLS 1.2 (acceptable), TLS 1.3 (recommended). Handshake establishes shared secret. Certificate-based server authentication."},
        {"JWT (JSON Web Tokens)","Compact token format: header.payload.signature. Algorithms: HS256 (shared secret), RS256 (RSA). Claims: iss, sub, exp, nbf, iat. Validate signature, expiry, issuer!"},
        {"PKI (Public Key Infrastructure)","Framework for managing digital certificates. CA signs certificates. Chain of trust: Root CA → Intermediate CA → End Entity. Certificate Transparency logs public issuance."},
        {"Password Hashing","Never store plaintext! Use bcrypt, Argon2id, scrypt with salts. PBKDF2 acceptable. MD5/SHA-1 for passwords is insecure. Use high work factor."},
        {"Diffie-Hellman Key Exchange","Allows two parties to establish shared secret over public channel. Foundation of ECDH. Used in TLS handshake for Perfect Forward Secrecy (PFS)."},
        {"Elliptic Curve Cryptography","ECC provides same security as RSA with smaller keys. P-256, P-384, X25519 curves. ECDH for key exchange, ECDSA for signatures. More efficient on mobile."},
        {"Zero Knowledge Proofs","Prove knowledge of a secret without revealing the secret. Used in privacy-preserving authentication, blockchain. zk-SNARKs, zk-STARKs are implementations."}
    };

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String topic = inputs[0].trim().toLowerCase();
        StringBuilder sb = new StringBuilder("📚 Cryptography Learning\n\n");
        for (String[] lesson : LESSONS) {
            if (topic.isEmpty() || topic.equals("all") ||
                lesson[0].toLowerCase().contains(topic)) {
                sb.append("── ").append(lesson[0]).append(" ─────\n");
                sb.append(lesson[1]).append("\n\n");
            }
        }
        if (sb.length() < 50) sb.append("No lesson found for: " + topic + "\nTry: aes, rsa, hash, tls, jwt, pki, all");
        cb.onResult(sb.toString());
    }
}
