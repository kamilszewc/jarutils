package com.computinglaboratory;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class FileEncryptor {

    public static void encryptFile(Path decryptedFile, Path encryptedFile, String password) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        byte[] data = Files.readAllBytes(decryptedFile);
        byte[] salt = {1,2,3};
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        IvParameterSpec iv = new IvParameterSpec("xxxxxxxxxxxxxxxx".getBytes(StandardCharsets.UTF_8));

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret, iv);
        cipher.update(data);
        byte[] encryptedData = cipher.doFinal();
        Files.write(encryptedFile, encryptedData, StandardOpenOption.CREATE_NEW);
    }

    public static void decryptFile(Path encryptedFile, Path decryptedFile, String password) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException {
        byte[] data = Files.readAllBytes(encryptedFile);

        byte[] salt = {1,2,3};
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        IvParameterSpec iv = new IvParameterSpec("xxxxxxxxxxxxxxxx".getBytes(StandardCharsets.UTF_8));

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret, iv);
        cipher.update(data);
        byte[] decrypted = cipher.doFinal();
        Files.write(decryptedFile, decrypted, StandardOpenOption.CREATE_NEW);
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
