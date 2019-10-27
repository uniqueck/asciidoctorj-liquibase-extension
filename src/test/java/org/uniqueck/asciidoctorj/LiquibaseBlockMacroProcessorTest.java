package org.uniqueck.asciidoctorj;

import org.junit.jupiter.api.Test;

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
}