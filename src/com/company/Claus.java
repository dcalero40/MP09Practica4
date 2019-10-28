package com.company;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Scanner;

public class Claus {

    public SecretKey keygenKeyGeneration(int keySize) {
        SecretKey sKey = null;
        if ((keySize == 128)||(keySize == 192)||(keySize == 256)) {
            try {
                KeyGenerator kgen = KeyGenerator.getInstance("AES");
                kgen.init(keySize);
                sKey = kgen.generateKey();

            } catch (NoSuchAlgorithmException ex) {
                System.err.println("Generador no disponible.");
            }
        }
        return sKey;
    }

    public SecretKey passwordKeyGeneration(String text, int keySize) {
        SecretKey sKey = null;
        if ((keySize == 128)||(keySize == 192)||(keySize == 256)) {
            try {
                byte[] data = text.getBytes("UTF-8");
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hash = md.digest(data);
                byte[] key = Arrays.copyOf(hash, keySize/8);
                sKey = new SecretKeySpec(key, "AES");
            } catch (Exception ex) {
                System.err.println("Error generant la clau:" + ex);
            }
        }
        return sKey;
    }

    public byte[] encryptData(SecretKey sKey, byte[] data) {
        byte[] encryptedData = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, sKey);
            encryptedData =  cipher.doFinal(data);
        } catch (Exception  ex) {
            System.err.println("Error xifrant les dades: " + ex);
        }
        return encryptedData;
    }

    public byte[] decryptData(SecretKey sKey, byte[] data) throws BadPaddingException {
        byte[] decryptedData = null;
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, sKey);
            decryptedData =  cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return decryptedData;
    }
    public void probarClaves(FileReader fileReader, byte[] data) throws IOException {
        Scanner opnScaner = new Scanner(fileReader);
        String linea="";
        while(opnScaner.hasNext()) {
            linea+=opnScaner.nextLine()+",";
        }

        String[] claves = linea.split(",");
        boolean adivinar=false;
        int count=-1;
        byte[] decryptedDataPrueba = new byte[]{};
        while (!adivinar){
            count++;
            SecretKey keyPrueba = passwordKeyGeneration(claves[count], 128);
            try {
                decryptedDataPrueba = decryptData(keyPrueba, data);
                adivinar=true;
            } catch (BadPaddingException e) {
                //System.out.println("no es "+claves[i]);

                //e.printStackTrace();
            }
        }
        fileReader.close();
        System.out.println("La clave correcta es: "+claves[count]);
        System.out.println(new String(decryptedDataPrueba));
    }

}
