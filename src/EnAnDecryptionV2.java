package com.pwmanager.pwmanager;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import org.mindrot.jbcrypt.BCrypt;

public class EnAnDecryptionV2 {

    public EnAnDecryptionV2(){

    }

    private SecretKeySpec secretKey;
    private byte[] key;
    private SecretKey doneKey;
    byte[] salt = new byte[16];
    boolean saltSet = false;

    public SecretKey getKey(final String password, byte[] salt){
        if(doneKey != null) return doneKey;
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            doneKey = new SecretKeySpec(tmp.getEncoded(), "AES");
            return doneKey;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Pw is used to encrypt s
    public String encrypt(String pw, String s){
        try{
            byte[] iv = new byte[16];
            SecureRandom random = new SecureRandom();
            if(!saltSet) random.nextBytes(salt);
            random.nextBytes(iv);

            SecretKey key = getKey(pw, salt);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));

            byte[] encrypted = cipher.doFinal(s.getBytes());

            byte[] result = new byte[salt.length + iv.length + encrypted.length];

            System.arraycopy(salt, 0, result, 0, 16);
            System.arraycopy(iv, 0, result, 16, 16);
            System.arraycopy(encrypted, 0, result, 32, encrypted.length);

            return Base64.getEncoder().encodeToString(result);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //pw is uesed to decrypt s
    public String decrypt(String pw, String s){
        try{
            byte[] data = Base64.getDecoder().decode(s);

            byte[] iv = new byte[16];
            byte[] cipherText = new byte[data.length - 32];

            System.arraycopy(data, 0, salt, 0, 16);
            System.arraycopy(data, 16, iv, 0, 16);
            System.arraycopy(data, 32, cipherText, 0, cipherText.length);
            saltSet =  true;

            SecretKey key = getKey(pw, salt);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));

            byte[] decrypted = cipher.doFinal(cipherText);

            return new String(decrypted);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String newPwHash(String s){
        try{
            String hash = BCrypt.hashpw(s, BCrypt.gensalt(12));
            return hash;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkPw(String pw, String hash){
        try{
            return BCrypt.checkpw(pw, hash);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

}
