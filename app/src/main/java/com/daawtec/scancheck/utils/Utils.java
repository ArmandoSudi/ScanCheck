package com.daawtec.scancheck.utils;

import android.util.Base64;
import android.util.Log;

import com.daawtec.scancheck.entites.AffectationMacaronAS;
import com.daawtec.scancheck.entites.AirsSante;
import com.daawtec.scancheck.entites.RelaisCommunautaire;
import com.daawtec.scancheck.entites.ZoneSante;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class Utils {

    // For the FVN hashing algorithms
    private static final long FNV_64_INIT = 0xcbf29ce484222325L;
    private static final long FNV_64_PRIME = 0x100000001b3L;

    private static Cipher ecipher;
    private static Cipher dcipher;

    private static SecretKey key;

    private static final String TAG = "Utils";

    public static List<AffectationMacaronAS> getAffectations(){
        Date date = new Date();
        SimpleDateFormat sp = new SimpleDateFormat("dd/mm/yyyy");
        String dateValue = sp.format(date);

        List<AffectationMacaronAS> affectations = new ArrayList<>();
        affectations.add(new AffectationMacaronAS("101", "000001", dateValue, dateValue));
        affectations.add(new AffectationMacaronAS("102", "000002", dateValue, dateValue));
        affectations.add(new AffectationMacaronAS("103", "000003", dateValue, dateValue));
        affectations.add(new AffectationMacaronAS("104", "000004", dateValue, dateValue));
        return affectations;
    }

    public static String getTimeStamp(){
        return new SimpleDateFormat("yyMMddHHmmss").format(new Date());
    }

    public static int stringToInt(String value){
        if (value .equals("")){
            return 0;
        }else {
            return Integer.parseInt(value);
        }
    }

    // TODO : implement the algorithm to compute the number of mild
    public static int computeMildNumber(int tailleMenage){
        return 2;
    }

    public static String hash(String plainTextPassword){
        String encrypted = "";
        try{
            DESKeySpec keySpec = new DESKeySpec("qwertykey".getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);
            byte[] cleartext = plainTextPassword.getBytes();

            Cipher cipher = Cipher.getInstance("DES"); // cipher is not thread safe
            cipher.init(Cipher.ENCRYPT_MODE, key);
            encrypted = Base64.encodeToString(cipher.doFinal(cleartext), Base64.DEFAULT);

            Cipher decipher = Cipher.getInstance("DES"); // cipher is not thread safe
            decipher.init(Cipher.DECRYPT_MODE, key);
            String decrypted = Base64.encodeToString(decipher.doFinal(Base64.decode(encrypted,Base64.DEFAULT)), Base64.DEFAULT);

            Log.d(TAG, plainTextPassword + " " + " of : " + encrypted + " : " + decrypted);

        }catch (Exception e){

        }
        return encrypted;
    }

    /**
     * Fowler–Noll–Vo hash function
     * @param value
     * @return
     */
//    public static String encryptString(String value){
//        try {
//
//            // generate secret key using DES algorithm
//            key = KeyGenerator.getInstance("DES").generateKey();
//
//            ecipher = Cipher.getInstance("DES");
//            dcipher = Cipher.getInstance("DES");
//
//            // initialize the ciphers with the given key
//
//            ecipher.init(Cipher.ENCRYPT_MODE, key);
//
//            dcipher.init(Cipher.DECRYPT_MODE, key);
//
//            String encrypted = encrypt("This is a classified message!");
//
//            String decrypted = decrypt(encrypted);
//
//            System.out.println("Decrypted: " + decrypted);
//
//        }
//        catch (NoSuchAlgorithmException e) {
//            System.out.println("No Such Algorithm:" + e.getMessage());
//        }
//        catch (NoSuchPaddingException e) {
//            System.out.println("No Such Padding:" + e.getMessage());
//        }
//        catch (InvalidKeyException e) {
//            System.out.println("Invalid Key:" + e.getMessage());
//        }
//    }
//
//    public static String encrypt(String str) {
//
//        try {
//            // encode the string into a sequence of bytes using the named charset
//            // storing the result into a new byte array.
//            byte[] utf8 = str.getBytes("UTF8");
//            byte[] enc = ecipher.doFinal(utf8);
//            // encode to base64
//            enc = BASE64EncoderStream.encode(enc);
//            return new String(enc);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static String decrypt(String str) {
//        try {
//            // decode with base64 to get bytes
//            byte[] dec = BASE64DecoderStream.decode(str.getBytes());
//            byte[] utf8 = dcipher.doFinal(dec);
//            // create new string based on the specified charset
//            return new String(utf8, "UTF8");
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

}
