package com.company;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
            Claus claus = new Claus();
            ejercicio1(claus);
            ejercicio2(claus);





    }

        private static void ejercicio2(Claus claus) throws IOException {
                //EJERCICIO2
                System.out.println("EJERCICIO 2: ");
                System.out.println("___________________________________________________________________");
                Path path = Paths.get("textamagat");
                byte[] textenbytes = Files.readAllBytes(path);
                FileReader fileReader = new FileReader("clausA4");
                claus.probarClaves(fileReader, textenbytes);
        }

        private static void ejercicio1(Claus claus) {
                Scanner in = new Scanner(System.in);
                //DATOS:
                int keySize=256;
                SecretKey myKey;
                SecretKey myPassword;
                SecretKey claveUsuario;
                String data="hola soy un ejemplo.";
                String miClauTexto="clave";
                byte[] encryptedData;
                byte[] decrypteData;

                //ejercicio1.1:
                //Priemro generamos nuestras claves:
                myKey=claus.keygenKeyGeneration(keySize);
                //Después ciframos:
                encryptedData = claus.encryptData(myKey,data.getBytes());
                //System.out.println(encryptedData);

                try {
                        decrypteData = claus.decryptData(myKey, encryptedData);
                } catch (BadPaddingException e) {
                        e.printStackTrace();
                }

                System.out.println("EJERCICIO 1.2:");
                System.out.println("___________________________________________________________________");
                //ejercicio1.2:
                //Generamos una clave simetrica AES
                myPassword=claus.passwordKeyGeneration(miClauTexto,keySize);
                //Encriptamos;
                encryptedData = claus.encryptData(myPassword,data.getBytes());
                //System.out.println(encryptedData);

                //Después Pedimos clave al usuario, si no introduce "clave" petará:
                System.out.println("Introduce una Clave, a ver si aciertas que clave he puesto:");
                //encriptamos con la clave que introduce el usuario;
                claveUsuario=claus.passwordKeyGeneration(in.nextLine(),keySize);

                try {
                        decrypteData = claus.decryptData(claveUsuario, encryptedData);
                } catch (BadPaddingException e) {
                        System.out.println("ERROR, ESA NO ES LA CLAVE INTERNA");
                }
        }
}
