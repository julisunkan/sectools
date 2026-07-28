package com.netsec.toolkit.utils;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.zip.CRC32;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtils {

    // ── Hashing ───────────────────────────────────────────────────────────────

    public static String md5(String input) throws Exception {
        return hash(input, "MD5");
    }

    public static String sha1(String input) throws Exception {
        return hash(input, "SHA-1");
    }

    public static String sha256(String input) throws Exception {
        return hash(input, "SHA-256");
    }

    public static String sha512(String input) throws Exception {
        return hash(input, "SHA-512");
    }

    public static String hash(String input, String algo) throws Exception {
        MessageDigest md = MessageDigest.getInstance(algo);
        byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(digest);
    }

    public static String sha256Bytes(byte[] bytes) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return bytesToHex(md.digest(bytes));
    }

    public static long crc32(byte[] bytes) {
        CRC32 crc = new CRC32();
        crc.update(bytes);
        return crc.getValue();
    }

    // ── AES ───────────────────────────────────────────────────────────────────

    public static String aesEncrypt(String plaintext, String password) throws Exception {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        SecretKey key = deriveKey(password, salt);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] iv = cipher.getIV();
        byte[] encrypted = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        // Format: base64(salt + iv + ciphertext)
        byte[] combined = new byte[salt.length + iv.length + encrypted.length];
        System.arraycopy(salt, 0, combined, 0, salt.length);
        System.arraycopy(iv, 0, combined, salt.length, iv.length);
        System.arraycopy(encrypted, 0, combined, salt.length + iv.length, encrypted.length);
        return Base64.encodeToString(combined, Base64.NO_WRAP);
    }

    public static String aesDecrypt(String ciphertext, String password) throws Exception {
        byte[] combined = Base64.decode(ciphertext, Base64.NO_WRAP);
        byte[] salt = new byte[16];
        byte[] iv   = new byte[16];
        byte[] encrypted = new byte[combined.length - 32];
        System.arraycopy(combined, 0, salt, 0, 16);
        System.arraycopy(combined, 16, iv, 0, 16);
        System.arraycopy(combined, 32, encrypted, 0, encrypted.length);
        SecretKey key = deriveKey(password, salt);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        return new String(cipher.doFinal(encrypted), StandardCharsets.UTF_8);
    }

    private static SecretKey deriveKey(String password, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    // ── RSA ───────────────────────────────────────────────────────────────────

    public static KeyPair generateRsaKeyPair() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        return kpg.generateKeyPair();
    }

    public static String rsaEncrypt(String plaintext, PublicKey pubKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return Base64.encodeToString(cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8)), Base64.NO_WRAP);
    }

    public static String rsaDecrypt(String ciphertext, PrivateKey privKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privKey);
        return new String(cipher.doFinal(Base64.decode(ciphertext, Base64.NO_WRAP)), StandardCharsets.UTF_8);
    }

    // ── Digital Signature ─────────────────────────────────────────────────────

    public static String sign(String data, PrivateKey privKey) throws Exception {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initSign(privKey);
        sig.update(data.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeToString(sig.sign(), Base64.NO_WRAP);
    }

    public static boolean verify(String data, String signature, PublicKey pubKey) throws Exception {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(pubKey);
        sig.update(data.getBytes(StandardCharsets.UTF_8));
        return sig.verify(Base64.decode(signature, Base64.NO_WRAP));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    public static String generatePassword(int length, boolean upper, boolean lower,
                                           boolean digits, boolean special) {
        String chars = "";
        if (upper)   chars += "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        if (lower)   chars += "abcdefghijklmnopqrstuvwxyz";
        if (digits)  chars += "0123456789";
        if (special) chars += "!@#$%^&*()-_=+[]{}|;':\",./<>?";
        if (chars.isEmpty()) chars = "abcdefghijklmnopqrstuvwxyz";
        SecureRandom rng = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) sb.append(chars.charAt(rng.nextInt(chars.length())));
        return sb.toString();
    }

    public static double passwordEntropy(String password) {
        int pool = 0;
        boolean hasLower = false, hasUpper = false, hasDigit = false, hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSpecial = true;
        }
        if (hasLower)   pool += 26;
        if (hasUpper)   pool += 26;
        if (hasDigit)   pool += 10;
        if (hasSpecial) pool += 32;
        if (pool == 0) pool = 26;
        return password.length() * (Math.log(pool) / Math.log(2));
    }

    public static String identifyHash(String hash) {
        String h = hash.trim();
        switch (h.length()) {
            case 32:  return "MD5 (32 hex chars)";
            case 40:  return "SHA-1 (40 hex chars)";
            case 56:  return "SHA-224 (56 hex chars)";
            case 64:  return h.startsWith("$5$") ? "SHA-crypt-256" : "SHA-256 (64 hex chars)";
            case 96:  return "SHA-384 (96 hex chars)";
            case 128: return "SHA-512 (128 hex chars)";
            case 60:  return h.startsWith("$2") ? "bcrypt (60 chars)" : "Unknown (60 chars)";
            default:
                if (h.startsWith("$argon2")) return "Argon2";
                if (h.startsWith("$pbkdf2")) return "PBKDF2";
                return "Unknown hash type (length " + h.length() + ")";
        }
    }
}
