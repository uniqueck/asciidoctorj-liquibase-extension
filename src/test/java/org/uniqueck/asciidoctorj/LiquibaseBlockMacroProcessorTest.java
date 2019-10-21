package org.uniqueck.asciidoctorj;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LiquibaseBlockMacroProcessorTest extends AbstractAsciidoctorTestHelper {

    @Test
    void process() {
        String actualContent = convert("liquibase::simpleChangeSet.xml[]");
        assertNotNull(actualContent);
    }
}