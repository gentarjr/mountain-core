package com.mountain.library.unittest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES128 {

    private static String iv = "fedcba9876543210";
    private static final String HEXINDEX = "0123456789abcdef ABCDEF";


    public static byte[] encryptToByte(String plainText, String key) throws Exception {
        plainText = AESKeyVerifier(plainText, null);
        key = AESKeyVerifier(key, "0");


        SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());


        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
        byte[] encrypted = cipher.doFinal(plainText.getBytes());


        return encrypted;
    }


    public static String encryptToString(String plainText, String key) throws Exception {
        plainText = AESKeyVerifier(plainText, null);
        key = AESKeyVerifier(key, "0");


        SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());


        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
        byte[] encrypted = cipher.doFinal(plainText.getBytes());


        return bytesToHex(encrypted);
    }


    public static String decrypt(String encrypted, String key) throws Exception {
        key = AESKeyVerifier(key, "0");


        SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());


        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");


        cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
        byte[] decrypted = cipher.doFinal(hexToByte(encrypted));


        return new String(decrypted);
    }


    public static String bytesToHex(byte[] data) {


        if (data == null) {
            return null;
        } else {
            int len = data.length;
            String str = "";


            for (int i = 0; i < len; i++) {
                if ((data[i] & 0xFF) < 16) {
                    str = str + "0" + Integer.toHexString(data[i] & 0xFF);
                } else {
                    str = str + Integer.toHexString(data[i] & 0xFF);
                }
            }


            return str;
        }
    }


    public static byte[] hexToByte(String s) {
        int l = s.length() / 2;
        byte data[] = new byte[l];
        int j = 0;


        for (int i = 0; i < l; i++) {
            char c = s.charAt(j++);
            int n, b;


            n = HEXINDEX.indexOf(c);
            b = (n & 0xf) << 4;
            c = s.charAt(j++);
            n = HEXINDEX.indexOf(c);
            b += (n & 0xf);
            data[i] = (byte) b;
        }
        return data;
    }


    public static String AESKeyVerifier(String key, String pad) {
        if(pad == null)
            pad = " ";
        int keyLength = key.length();
        int factor = (int) Math.ceil((double) keyLength / 16);
        if (factor == 0) {
            factor = 1;
        }
        for (int i = keyLength; i < factor * 16; i++) {
            key += pad;
        }
        return key;
    }


    public static void main(String[] args) throws Exception {
        String password = AES128.encryptToString("Mountain123Simple", "shared123");
        System.out.println(password);
    }
}
