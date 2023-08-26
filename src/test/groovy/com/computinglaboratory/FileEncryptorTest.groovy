package com.computinglaboratory

import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path

class FileEncryptorTest extends Specification {

    def "encryptFile correctly decrypts files"() {
        given:
        Path inputFilePath = Path.of('test.txt')
        Path encryptedFilePath = Path.of('encrypted.txt')

        when:
        FileEncryptor.encryptFile(inputFilePath, encryptedFilePath, "secret")

        then:
        Files.exists(encryptedFilePath)
    }

    def "decryptFile correctly decrypts files"() {
        given:
        Path inputFilePath = Path.of('encrypted.txt')
        Path decryptedFilePath = Path.of('decrypted.txt')

        when:
        FileEncryptor.decryptFile(inputFilePath, decryptedFilePath, "secret")

        then:
        Files.exists(decryptedFilePath)
    }
}
