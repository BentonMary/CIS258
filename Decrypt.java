 

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.util.Scanner;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileOutputStream;
import javax.crypto.SecretKeyFactory;
import javax.crypto.CipherOutputStream;

public class Decrypt{

    public static void main (String[] args){

        int length = 0;
        byte[] file = new byte[50];


        System.out.println("What is the name of the file you want to decrypt?");
        Scanner scan = new Scanner (System.in);
        String fileName = scan.nextLine();

        System.out.println("What is your password?");
        String password = scan.nextLine();

        try {
            FileInputStream EncryptedFile = new FileInputStream(fileName);
            FileOutputStream DecryptedFile = new FileOutputStream(fileName);

            byte[] salt = new byte[32];
            EncryptedFile.read(salt);

            byte[] IV = new byte [16];
            EncryptedFile.read(IV);

            // Creating array of string length
            char[] ch = new char[password.length()];

            // Copy character by character into array
            for (int i = 0; i < password.length(); i++) {
                ch[i] = password.charAt(i);
            }

            PBEKeySpec ks = new PBEKeySpec(ch, salt, 10000, 256);

            SecretKeyFactory secret = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            SecretKey k = secret.generateSecret(ks);

            SecretKey newKey = new SecretKeySpec(k.getEncoded(), "AES");

            Cipher C = Cipher.getInstance("AEC/CBC/PKCS5Padding" );

            C.init(Cipher.DECRYPT_MODE, newKey, new IvParameterSpec(IV));

            CipherOutputStream Cipher = new CipherOutputStream (DecryptedFile, C);


            while ((length = EncryptedFile.read(file)) !=1){

                Cipher.write(file, 0, length);
            }

            EncryptedFile.close();
            Cipher.close();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException ex) {
            ex.printStackTrace();
        }
    }



    }


