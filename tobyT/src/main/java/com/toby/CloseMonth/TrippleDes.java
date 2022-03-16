/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.CloseMonth;

import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

/**
 *
 * @author WIN7
 */
public class TrippleDes {
    private static final String UNICODE_FORMAT = "UTF8";
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private final KeySpec ks;
    private final SecretKeyFactory skf;
    private final Cipher cipher;
    byte[] arrayBytes;
    private final String myEncryptionKey;
    private final String myEncryptionScheme;
    SecretKey key;

    public TrippleDes() throws Exception {
        myEncryptionKey = "123456789123456789123546"; //Should be 24 Char.
        myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
        arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
        ks = new DESedeKeySpec(arrayBytes);
        skf = SecretKeyFactory.getInstance(myEncryptionScheme);
        cipher = Cipher.getInstance(myEncryptionScheme);
        key = skf.generateSecret(ks);
    }

    public String encrypt(String unencryptedString) {
        String encryptedString = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);
            encryptedString = new String(Base64.encodeBase64(encryptedText));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedString;
    }

    public String decrypt(String encryptedString) {
        String decryptedText = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedText = Base64.decodeBase64(encryptedString);
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText = new String(plainText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedText;
    }

    public static void main(String args[]) throws Exception {

        String jsonStr = "{         \"dataArray\": [{\"x\":1,\"y\":2,\"z\":3},{\"x\":2,\"y\":4,\"z\":6},{\"x\":3,\"y\":6,\"z\":9},{\"x\":4,\"y\":8,\"z\":12},{\"x\":5,\"y\":10,\"z\":15},{\"x\":6,\"y\":12,\"z\":18},{\"x\":7,\"y\":14,\"z\":21},{\"x\":8,\"y\":16,\"z\":24},{\"x\":9,\"y\":18,\"z\":27},{\"x\":10,\"y\":20,\"z\":30}]      }";

        JSONObject jsonObj = new JSONObject(jsonStr);

        System.out.println("jsonObj: " + jsonObj);

        JSONArray c = jsonObj.getJSONArray("dataArray");

        List<Object> rows = new ArrayList<>();

        List<Object> colmns = new ArrayList<>();
        colmns.add("x");
        colmns.add("y");
        colmns.add("z");

//        System.out.println(colmns);

        rows.add(colmns);

//        System.out.println(rows);
//
//        System.out.println("c: " + c);
        for (int i = 0; i < c.length(); i++) {
            colmns = new ArrayList<>();
            JSONObject obj = c.getJSONObject(i);
            Object A = obj.get("x");
            Object B = obj.get("y");
            Object C = obj.get("z");

            colmns.add(A);
            colmns.add(B);
            colmns.add(C);

            rows.add(colmns);

        }

        System.out.println("rows");
        System.out.println(rows);
//
        for (Object row : rows) {
            System.out.println(row);
        }
//        TrippleDes td = new TrippleDes();
//
//        String target = "pass";
//        String encrypted = td.encrypt(target);
//        String decrypted = td.decrypt(encrypted);
//
//        System.out.println("String To Encrypt: " + target);
//        System.out.println("Encrypted String: " + encrypted);
//        System.out.println("Decrypted String: " + decrypted);
    }
}
