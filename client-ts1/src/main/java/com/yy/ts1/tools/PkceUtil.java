package com.yy.ts1.tools;

import org.apache.commons.codec.binary.Base64;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class PkceUtil {

    /**
     * 生成加密安全的 code_verifier
     * 长度应在43-128字符之间，通常使用32字节随机数生成
     */
    public static String generateCodeVerifier() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] codeVerifier = new byte[32];
        secureRandom.nextBytes(codeVerifier);
        // 使用Base64 URL安全编码（无填充）
        return Base64.encodeBase64URLSafeString(codeVerifier);
    }

    /**
     * 使用S256方法生成code_challenge
     * 对code_verifier进行SHA-256哈希，然后Base64 URL编码
     */
    public static String generateCodeChallenge(String codeVerifier) throws Exception {
        byte[] bytes = codeVerifier.getBytes("US-ASCII");
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(bytes, 0, bytes.length);
        byte[] digest = md.digest();
        return Base64.encodeBase64URLSafeString(digest);
    }

    // 示例用法
    public static void main(String[] args) throws Exception {
        String codeVerifier = generateCodeVerifier();
        String codeChallenge = generateCodeChallenge(codeVerifier);
        
        System.out.println("Code Verifier: " + codeVerifier);
        System.out.println("Code Challenge: " + codeChallenge);
        // 输出示例:
        // Code Verifier: u9wviCi1r4qZZe1O3LrZ5V5ZZgq_vzpG
        // Code Challenge: LCx3bvywQ33l9s0x5c7U0N0ML7T2u060k
    }
}