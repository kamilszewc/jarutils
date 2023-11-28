package io.github.kamilszewc.jarutils;

import javax.crypto.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class ResourceReader {

    public static void copyResourceFile(String resourceFilename, Path localFilename) throws IOException {
        InputStream inputStream = ResourceReader.class.getResourceAsStream(resourceFilename);
        Files.copy(inputStream, localFilename, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void decryptAndCopyResourceFile(String resourceFilename, Path localFilename, String password) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException {
        InputStream inputStream = ResourceReader.class.getResourceAsStream(resourceFilename);
        Path tempFile = Files.createTempFile("encrypted-", "");
        Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
        FileEncryptor.decryptFile(tempFile, localFilename, password);
        Files.delete(tempFile);
    }
}
