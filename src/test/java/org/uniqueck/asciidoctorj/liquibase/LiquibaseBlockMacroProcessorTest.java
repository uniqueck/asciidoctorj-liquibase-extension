package org.uniqueck.asciidoctorj.liquibase;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LiquibaseBlockMacroProcessorTest extends AbstractAsciidoctorTestHelper {

    @Test
    void process_SimpleChangeSet() {
        String actualContent = convert("liquibase::simpleChangeSet.xml[]");
        assertNotNull(actualContent);
    }

    @Test
    void process_ComplexChangeSet_TillTag_1_X_FINAL() {
        String actualContent = convert("liquibase::db/db.changelog-master.xml[tillTag=1.X.FINAL]");
        assertNotNull(actualContent);
    }

    @Test
    void process_ComplexChangeSet() {
        String actualContent = convert("liquibase::db/db.changelog-master.xml[]");
        assertNotNull(actualContent);
    }


    @Test
    void generateAsciiDocMarkup() {
        List<String> asciiDocMarkup = new LiquibaseBlockMacroProcessor().generateAsciiDocMarkup(null, new File("src/test/resources/simpleChangeSet.xml"), new HashMap<>());
        assertNotNull(asciiDocMarkup);
        assertFalse(asciiDocMarkup.isEmpty());
        assertTrue(asciiDocMarkup.contains("[plantuml]"));
        assertTrue(asciiDocMarkup.contains("skinparam tabSize 4"));
        assertTrue(asciiDocMarkup.contains("hide circle"));
        assertTrue(asciiDocMarkup.contains("skinparam linetype ortho"));

        assertTrue(asciiDocMarkup.contains("\\t\\tCOLUMN1 : TEXT"));

    }

    @Test
    void generateAsciiDocMarkupWithFileName() {
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("fileName", "diagram");
        List<String> asciiDocMarkup = new LiquibaseBlockMacroProcessor().generateAsciiDocMarkup(null, new File("src/test/resources/simpleChangeSet.xml"), attributes);
        assertNotNull(asciiDocMarkup);
        assertFalse(asciiDocMarkup.isEmpty());
        assertTrue(asciiDocMarkup.contains("[plantuml, diagram]"));
        assertTrue(asciiDocMarkup.contains("skinparam tabSize 4"));
        assertTrue(asciiDocMarkup.contains("hide circle"));
        assertTrue(asciiDocMarkup.contains("skinparam linetype ortho"));

        assertTrue(asciiDocMarkup.contains("\\t\\tCOLUMN1 : TEXT"));

    }

    @Test
    void generateAsciiDocMarkupWithFileNameAndOutputFormat() {
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("fileName", "diagram");
        attributes.put("outputFormat", "svg");
        List<String> asciiDocMarkup = new LiquibaseBlockMacroProcessor().generateAsciiDocMarkup(null, new File("src/test/resources/simpleChangeSet.xml"), attributes);
        assertNotNull(asciiDocMarkup);
        assertFalse(asciiDocMarkup.isEmpty());
        assertTrue(asciiDocMarkup.contains("[plantuml, diagram, svg]"));
        assertTrue(asciiDocMarkup.contains("skinparam tabSize 4"));
        assertTrue(asciiDocMarkup.contains("hide circle"));
        assertTrue(asciiDocMarkup.contains("skinparam linetype ortho"));

        assertTrue(asciiDocMarkup.contains("\\t\\tCOLUMN1 : TEXT"));

    }

    @Test
    void generateAsciiDocMarkupWithoutFileNameAndWithOutputFormat() {
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("outputFormat", "svg");
        List<String> asciiDocMarkup = new LiquibaseBlockMacroProcessor().generateAsciiDocMarkup(null, new File("src/test/resources/simpleChangeSet.xml"), attributes);
        assertNotNull(asciiDocMarkup);
        assertFalse(asciiDocMarkup.isEmpty());
        assertTrue(asciiDocMarkup.contains("[plantuml]"));
        assertTrue(asciiDocMarkup.contains("skinparam tabSize 4"));
        assertTrue(asciiDocMarkup.contains("hide circle"));
        assertTrue(asciiDocMarkup.contains("skinparam linetype ortho"));

        assertTrue(asciiDocMarkup.contains("\\t\\tCOLUMN1 : TEXT"));

    }
}