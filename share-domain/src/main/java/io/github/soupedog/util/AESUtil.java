package io.github.soupedog.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

public class AESUtil {
    private static final String SECRET_KEY = "zhetmjiushimiyao";
    private static final Cipher forEncrypt = getCipher(SECRET_KEY, Cipher.ENCRYPT_MODE);
    private static final Cipher forDecrypt = getCipher(SECRET_KEY, Cipher.DECRYPT_MODE);

    private AESUtil() {
    }

    public static Cipher getCipher(String secretKey, int mode) {
        try {
            byte[] key = secretKey.getBytes(StandardCharsets.UTF_8);
            // 信息摘要(hash)计算算法与 JDK 版本有关，不同版本支持的算法不一定相同，如 "SHA-1"、"SHA-256"、"SHA3-256" 等(总之高版本 JDK 会更多)
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 32);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

            Cipher cipherTemp = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipherTemp.init(mode, secretKeySpec);
            return cipherTemp;
        } catch (Exception e) {
            throw new RuntimeException("Fail to init Cipher.", e);
        }
    }

    public static String encrypt(String input) {
        try {
            byte[] inputAfterEncrypt = forEncrypt.doFinal(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().encodeToString(inputAfterEncrypt);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Fail to encrypt [%s].", input), e);
        }
    }

    public static String decrypt(String input) {
        try {
            byte[] inputAfterBase64Decode = Base64.getUrlDecoder().decode(input.getBytes(StandardCharsets.UTF_8));
            byte[] inputAfterDecrypt = forDecrypt.doFinal(inputAfterBase64Decode);
            return new String(inputAfterDecrypt, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Fail to decrypt [%s].", input), e);
        }
    }
}