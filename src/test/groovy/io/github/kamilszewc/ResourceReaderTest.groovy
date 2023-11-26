package io.github.kamilszewc

import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path

class ResourceReaderTest extends Specification {

    def "copyResourceFile copies correctly data from resource"() {
        given:
        Path localFilePath = Path.of('test.txt');

        when:
        ResourceReader.copyResourceFile('/sample.txt', localFilePath)

        then:
        Files.exists(localFilePath)
    }
}
