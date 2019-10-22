package org.uniqueck.asciidoctorj.liquibase;

import org.junit.jupiter.api.Test;
import org.uniqueck.asciidoctorj.liquibase.model.Table;

import java.io.File;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LiquibaseChangesetParserTest {

    @Test
    void testParse() {
        Map<String, Table> parsedTables = new LiquibaseChangesetParser().parse(new File("src/test/resources/simpleChangeSet.xml"));
        assertNotNull(parsedTables);
        assertEquals(1, parsedTables.size());
        Table expectedTable = new Table("TablesAndTables");
        expectedTable.add("COLUMN1", "TEXT");
        assertEquals(expectedTable, parsedTables.get("TablesAndTables"));
        assertEquals(1, parsedTables.get("TablesAndTables").getColumns().size());

    }

}