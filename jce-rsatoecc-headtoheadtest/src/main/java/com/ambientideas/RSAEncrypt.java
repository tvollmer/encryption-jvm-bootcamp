package com.ambientideas;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class RSAEncrypt
{
    public static final int RSA_BITSTRENGTH_1024 = 1024;
    public static final int RSA_BITSTRENGTH_2048 = 2048;
    public static final int RSA_BITSTRENGTH_3072 = 3072;
    private KeyPair keyPair;

    public static long runExample(int bitStrength, boolean debugOutput) throws Exception {
    	
        RSAEncrypt app = new RSAEncrypt();
        String input = null;

        long beforeRSA = java.lang.System.currentTimeMillis();

        KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
        keygen.initialize(bitStrength);
        app.keyPair = keygen.generateKeyPair();

        if (debugOutput == true) {
            System.out.println("Enter a line: ");
            java.io.InputStreamReader sreader = new java.io.InputStreamReader(System.in);
            java.io.BufferedReader breader = new java.io.BufferedReader(sreader);
            input = breader.readLine();
        } else {
    		    input = HeadToHeadTest.getTextFromFile(HeadToHeadTest.CLEARTEXT_FILENAME);
        }

        if (debugOutput == true) {
            System.out.println("Plaintext = " + input);
        }

        String ciphertext = app.encrypt(input);
        
        if (debugOutput == true) {
            System.out.println("After Encryption Ciphertext = " + ciphertext);
            System.out.println("After Decryption Plaintext = " + app.decrypt(ciphertext));
        }
        
        long afterRSA = java.lang.System.currentTimeMillis();

        return afterRSA - beforeRSA;   
    }

    public String encrypt(String plaintext)  throws Exception
    {
        PublicKey key = keyPair.getPublic();
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] ciphertext = cipher.doFinal(plaintext.getBytes("UTF8"));
        return encodeBASE64(ciphertext);
    }

    public String decrypt(String ciphertext)  throws Exception
    {
        PrivateKey key = keyPair.getPrivate();
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] plaintext = cipher.doFinal(decodeBASE64(ciphertext));
        return new String(plaintext, "UTF8");
    }

    private static String encodeBASE64(byte[] bytes)
    {
        BASE64Encoder b64 = new BASE64Encoder();
        return b64.encode(bytes);
    }

    private static byte[] decodeBASE64(String text) throws Exception
    {
        BASE64Decoder b64 = new BASE64Decoder();
        return b64.decodeBuffer(text);
    }    

    public static void main(String[] args) throws Exception
    {
        runExample(RSA_BITSTRENGTH_1024, false);
        runExample(RSA_BITSTRENGTH_2048, false);
        runExample(RSA_BITSTRENGTH_3072, false);
    }
}