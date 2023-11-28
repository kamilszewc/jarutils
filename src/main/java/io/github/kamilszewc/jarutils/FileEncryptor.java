package io.github.kamilszewc.jarutils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class FileEncryptor {

    public static void encryptFile(Path decryptedFile, Path encryptedFile, String password) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        FileInputStream inFile = new FileInputStream(decryptedFile.toFile());
        FileOutputStream outFile = new FileOutputStream(encryptedFile.toFile());

        Cipher cipher = produceCipher(password.toCharArray(), Cipher.ENCRYPT_MODE);

        byte[] input = new byte[64];
        int bytesRead;
        while ((bytesRead = inFile.read(input)) != -1) {
            byte[] output = cipher.update(input, 0, bytesRead);
            if (output != null) {
                outFile.write(output);
            }
        }

        byte[] output = cipher.doFinal();
        if (output != null) {
            outFile.write(output);
        }

        inFile.close();
        outFile.flush();
        outFile.close();
    }

    public static void decryptFile(Path encryptedFile, Path decryptedFile, String password) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException {
        FileInputStream inFile = new FileInputStream(encryptedFile.toFile());
        FileOutputStream outFile = new FileOutputStream(decryptedFile.toFile());

        Cipher cipher = produceCipher(password.toCharArray(), Cipher.DECRYPT_MODE);

        byte[] in = new byte[64];
        int read;
        while ((read = inFile.read(in)) != -1) {
            byte[] output = cipher.update(in, 0, read);
            if (output != null) {
                outFile.write(output);
            }
        }

        byte[] output = cipher.doFinal();
        if (output != null) {
            outFile.write(output);
        }

        inFile.close();
        outFile.flush();
        outFile.close();
    }

    private static Cipher produceCipher(char[] password, int mode) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        final byte[] salt = new String("euihewujsdsfi").getBytes();
        final int iterationCount = 10000;
        final int keyLength = 128;
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(password, salt, iterationCount, keyLength);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        IvParameterSpec iv = new IvParameterSpec("rgeerrtghfdhfgfg".getBytes(StandardCharsets.UTF_8));
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(mode, secret, iv);
        return cipher;
    }

    public static void main(String[] args) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeySpecException, NoSuchAlgorithmException, IOException, BadPaddingException, InvalidKeyException {
        if (args.length != 4) {
            System.err.println("Wrong arguments");
            return;
        }

        Path inputFilename = Paths.get(args[1]);
        Path outputFilename = Paths.get(args[2]);
        String password = args[3];

        if (args[0].equals("encrypt")) {
            encryptFile(inputFilename, outputFilename, password);
        } else {
            decryptFile(inputFilename, outputFilename, password);
        }

    }
}
