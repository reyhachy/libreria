package com.totalplay.utils.NewCrypt;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class UtilCryptoGS {
    private static final String FORMAT_CHARACTER_ENCODING = "UTF-8";
    private static final String CIPHER_TRANSFORM_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM_ENCRYPT_METHOD = "AES";
    //private static final String LLAVE_ENCRIPCION = "mIxrMpXTuqoAQ4DlXW2rZSTq2FPpi5I=";//llave encripcion produccion
    private static final String LLAVE_ENCRIPCION = "eJG7R3O/Z+8a/FzQb4X7zv5Um++9tHo=";//llave encripcion desarrollo

    private static String encrypt(String plainText, String key) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        BASE64Encoder encoder = new BASE64Encoder();
        byte[] plainTextbytes = plainText.getBytes(FORMAT_CHARACTER_ENCODING);
        byte[] keyBytes = getKeyBytes(key);
        return encoder.encode(encrypt(plainTextbytes, keyBytes, keyBytes)).replaceAll("\r\n", "");
    }

    public static String crypto(String text) {
        try {
            return UtilCryptoGS.encrypt(text, LLAVE_ENCRIPCION);
        } catch (UnsupportedEncodingException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String descrypt(String text) {
        try {
            return decrypt(text, LLAVE_ENCRIPCION);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String decrypt(String encryptedText, String key) throws GeneralSecurityException, IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] cipheredBytes = decoder.decodeBuffer(encryptedText);
        byte[] keyBytes = getKeyBytes(key);
        return new String(decrypt(cipheredBytes, keyBytes, keyBytes), FORMAT_CHARACTER_ENCODING).replaceAll("\n", "");
    }

    private static byte[] getKeyBytes(String key) throws UnsupportedEncodingException {
        byte[] keyBytes = new byte[16];
        byte[] parameterKeyBytes = key.getBytes(FORMAT_CHARACTER_ENCODING);
        System.arraycopy(parameterKeyBytes, 0, keyBytes, 0, Math.min(parameterKeyBytes.length, keyBytes.length));
        return keyBytes;
    }

    private static byte[] decrypt(byte[] cipherText, byte[] key, byte[] initialVector) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORM_ALGORITHM);
        SecretKeySpec secretKeySpecy = new SecretKeySpec(key, ALGORITHM_ENCRYPT_METHOD);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpecy, ivParameterSpec);
        cipherText = cipher.doFinal(cipherText);
        return cipherText;
    }

    private static byte[] encrypt(byte[] plainText, byte[] key, byte[] initialVector) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORM_ALGORITHM);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, ALGORITHM_ENCRYPT_METHOD);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        plainText = cipher.doFinal(plainText);
        return plainText;
    }

}
