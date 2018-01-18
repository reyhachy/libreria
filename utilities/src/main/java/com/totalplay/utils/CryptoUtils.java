package com.totalplay.utils;

import android.annotation.SuppressLint;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by ReyHac on 17/01/18.
 */

public class CryptoUtils {

    @SuppressLint("GetInstance")
    public static String crypt(String text) {
        String secretKey = "oaguser";
        String base64EncryptedString = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);


            SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] plainTextBytes = text.getBytes("utf-8");
            byte[] buf = cipher.doFinal(plainTextBytes);
            byte[] base64Bytes = Base64.encode(buf, Base64.DEFAULT);
            base64EncryptedString = new String(base64Bytes);

        } catch (Exception ignored) {
        }
        return base64EncryptedString;
    }

    public static String desEncrypt(String textoEncriptado) {
        String base64EncryptedString = "";
        String secretKey = "oaguser";
        if (textoEncriptado != null) {
            byte[] message;
            try {
                message = Base64.decode(textoEncriptado.getBytes("utf-8"), Base64.DEFAULT);

                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
                byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
                SecretKey key = new SecretKeySpec(keyBytes, "DESede");

                Cipher decipher = Cipher.getInstance("DESede");
                decipher.init(Cipher.DECRYPT_MODE, key);

                byte[] plainText = decipher.doFinal(message);

                base64EncryptedString = new String(plainText, "UTF-8");
            } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
                e.printStackTrace();
            }
        } else {
            return "";
        }

        return base64EncryptedString;
    }
}
