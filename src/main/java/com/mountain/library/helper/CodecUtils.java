package com.mountain.library.helper;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.UUID;

public class CodecUtils {

    public static String randomPassword() {
        return RandomStringUtils.randomAlphanumeric(6);
    }

    public String getHashSHA256(String input){
        String enkripsi = null;
        try{
            MessageDigest messageDigest = DigestUtils.getSha256Digest();
            messageDigest.reset();
            messageDigest.update(input.getBytes("UTF-8"));
            Formatter formatter = new Formatter();
            for(byte b : messageDigest.digest()){
                formatter.format("%20x", b);
            }
            enkripsi = formatter.toString();
        }catch (UnsupportedEncodingException e){

        }
        return enkripsi;
    }

    public static String encodedBCrypt(String str, int strength){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(strength);
        return encoder.encode(str);
    }

    public static String encodeBcrypt(String str){
        return encodedBCrypt(str, 10);
    }

    public static boolean isPasswordMatch(String rawPassword, String encodePassword){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        return passwordEncoder.matches(rawPassword, encodePassword);
    }

    public static String generateUUID(){
        return UUID.randomUUID().toString();
    }

    public String getHashSHA1(String input){
        String enkripsi = null;
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.reset();
            messageDigest.update(input.getBytes("UTF-8"));
            Formatter formatter = new Formatter();
            for(byte b : messageDigest.digest()){
                formatter.format("%20x", b);
            }
            enkripsi = formatter.toString();
        }catch (NoSuchAlgorithmException | UnsupportedEncodingException e){

        }
        return enkripsi;
    }
}
