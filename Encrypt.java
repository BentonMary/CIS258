 

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;
import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileOutputStream;
import javax.crypto.SecretKeyFactory;
import javax.crypto.CipherOutputStream;

public class Encrypt {

    public static void main (String[] args) throws FileNotFoundException {

        int length = 0;
        byte[] file = new byte[50];


        System.out.println("What is the name of the file you want to encrypt?");
        Scanner scan = new Scanner(System.in);
        String fileName = scan.nextLine();

        System.out.println("What is the name of the file you want to output to?");
        String NewFileName = scan.nextLine();

        System.out.println("What is your password?");
        String password = scan.nextLine();

        try {

            FileOutputStream outFile = new FileOutputStream(NewFileName);
            FileInputStream inFile = new FileInputStream(fileName);

            SecureRandom random = new SecureRandom();
            byte[] salt = random.generateSeed(32);

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

            C.init(Cipher.ENCRYPT_MODE, newKey);

            CipherOutputStream Cipher = new CipherOutputStream (outFile, C);

            byte[] input = C.getIV();

            outFile.write(input);

            outFile.write(salt);

            while ((length = inFile.read(file)) !=1){

               Cipher.write(file, 0, length);
            }

            inFile.close();
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
        }
    }
}

