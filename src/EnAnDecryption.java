package com.pwmanager.pwmanager;
import javax.crypto.Cipher;
import javax.crypto.spec.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;

import static com.pwmanager.pwmanager.Main.*;

public class EnAnDecryption {

    public EnAnDecryption(){

    }

    private SecretKeySpec secretKey;
    private byte[] key;

    public void setKey(final String myKey){
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            //System.out.println(new String(key,"UTF-8")+key.length);
            key = sha.digest(key);
            //System.out.println(new String(key,"UTF-8")+key.length);
            key = Arrays.copyOf(key, 16);
            //System.out.println(new String(key,"UTF-8")+key.length);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Pw is used to encrypt s
    public String encrypt(String Pw, String s){
        try{
            setKey(Pw);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE,secretKey);
            //System.out.println("secretkey: "+new String(secretKey.getEncoded(),"UTF-8"));
            //System.out.println("pw: "+Pw+", clear: "+s+", encoded: "+Base64.getEncoder().encodeToString(cipher.doFinal(s.getBytes("UTF-8"))));
            return Base64.getEncoder().encodeToString(cipher.doFinal(s.getBytes("UTF-8")));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //pw is uesed to decrypt s
    public String decrypt(String Pw, String s){
        try{
            setKey(Pw);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE,secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(s)));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String doSha256(String s){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte [] byteArray= md.digest(s.getBytes(StandardCharsets.UTF_8));
            BigInteger number = new BigInteger(1, byteArray);
            StringBuilder hexString = new StringBuilder(number.toString(16));
            while (hexString.length() < 64)
            {
                hexString.insert(0, '0');
            }
            String ret = hexString.toString();
            return ret;
        }
        catch(Exception e){
            e.printStackTrace();
        }



        return null;
    }
}
