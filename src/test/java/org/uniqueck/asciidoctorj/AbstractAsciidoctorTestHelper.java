package org.uniqueck.asciidoctorj;

import lombok.AccessLevel;
import lombok.Getter;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Getter(AccessLevel.PROTECTED)
@Disabled
public abstract class AbstractAsciidoctorTestHelper {

    @TempDir
    File tempDir;


    protected File getBaseDir() {
        return new File("src/test/resources");
    }

    private Asciidoctor asciidoctor;

    protected Options createOptions() {
        return OptionsBuilder.options().baseDir(getBaseDir()).inPlace(true).safe(SafeMode.UNSAFE).backend("html5").toFile(false).destinationDir(tempDir.getAbsoluteFile()).get();
    }

    @BeforeEach
    void setup() {
        asciidoctor = Asciidoctor.Factory.create();
        new LiquibaseExtensionRegistry().register(asciidoctor);
    }

    protected String convert(String content) {
        return convert(content, createOptions());
    }

    protected String convert(String content, Options options) {
        return getAsciidoctor().convert(content, options);
    }

    protected String convertFile(String fileName) {
        return getAsciidoctor().convertFile(new File(fileName), createOptions());
    }

    protected void assertTempDirectoryContainsDirectory(String directoryName) {
        assertTrue(new File(tempDir, directoryName).isDirectory());
    }

    protected void assertTempDirectoryDoNotContainsDirectory(String directoryName) {
        assertFalse(new File(tempDir, directoryName).isDirectory());
    }

    protected void assertTempDirectoryContainsFile(String relativeFilePath) {
        assertTrue(new File(tempDir, relativeFilePath).isFile());
    }

}
