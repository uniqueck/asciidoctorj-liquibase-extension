package org.uniqueck.asciidoctorj.liquibase;

import com.sun.istack.internal.NotNull;
import org.junit.jupiter.api.Test;
import org.uniqueck.asciidoctorj.liquibase.model.Column;
import org.uniqueck.asciidoctorj.liquibase.model.Table;

import java.io.File;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LiquibaseChangesetParserTest {

    @Test
    void testParseSimpleChangeSetWithOneTableAndOneColumn() {
        Map<String, Table> parsedTables = new LiquibaseChangesetParser(new File("src/test/resources/simpleChangeSet.xml")).parse();
        assertNotNull(parsedTables);
        assertEquals(1, parsedTables.size());
        Table expectedTable = new Table("TablesAndTables");
        expectedTable.add("COLUMN1", "TEXT");
        assertEquals(expectedTable, parsedTables.get("TablesAndTables"));
        assertEquals(1, parsedTables.get("TablesAndTables").getColumns().size());

    }

    @Test
    void testParseDBChangeLog() {
        Map<String, Table> parsedTables = new LiquibaseChangesetParser(new File("src/test/resources/db/db.changelog-master.xml")).parse();
        assertNotNull(parsedTables);
        assertEquals(7, parsedTables.size());
        assertFileSheriffTable(parsedTables);
        assertDeploymentJobTable(parsedTables);
        assertDeploymentTriggerTable(parsedTables);
        assertDeploymentUmgebungTable(parsedTables);
        assertDeploymentlogTable(parsedTables);
        assertDeploymentAnmeldedatenTable(parsedTables);
        assertDeploymentServerTable(parsedTables);

    }

    @Test
    void testParseDBChangeLog_TillTag_1_X_FINAL() {
        Map<String, Table> parsedTables = new LiquibaseChangesetParser(new File("src/test/resources/db/db.changelog-master.xml"), "1.X.FINAL").parse();
        assertNotNull(parsedTables);
        assertEquals(6, parsedTables.size());
        assertFileSheriffTable_TillTag_1_X_FINAL(parsedTables);
        assertDeploymentJobTable_TillTag_1_X_FINAL(parsedTables);
        assertDeploymentTriggerTable_TillTag_1_X_FINAL(parsedTables);
        assertDeploymentUmgebungTable_TillTag_1_X_FINAL(parsedTables);
        assertDeploymentlogTable_TillTag_1_X_FINAL(parsedTables);
        assertDeploymentLogTable_TillTag_1_X_FINAL(parsedTables);
    }

    private void assertDeploymentTriggerTable_TillTag_1_X_FINAL(Map<String, Table> parsedTables) {
        Table table = assertTable(parsedTables, "DEPLOYMENT_TRIGGER");
        Map<String, Column> columns = assertColumnSize(table, 12);

        assertColumn("ID", "number", true, columns);
        assertColumn("ZEITPUNKT", "datetime(6)", false, columns);
        assertColumn("BEAUFTRAGENDER_USER", "varchar(100)", false, columns);
        assertColumn("BEAUFTRAGENDER_USERNAME", "varchar(100)", false, columns);
        assertColumn("BEAUFTRAGENDER_EMAIL", "varchar(150)", false, columns);
        assertColumn("FILESHERIFF_ID", "numeric", false, new Column("FILE_SHERIFF", "ID", "number"),columns);
        assertColumn("DEPLOYMENTUMGEBUNG_ID", "numeric", false, new Column("DEPLOYMENT_UMGEBUNG", "ID", "number"),columns);
        assertColumn("STATUS", "varchar(20)", false, columns);
        assertColumn("REFERENZ_ART", "varchar(20)", false, columns);
        assertColumn("REFERENZ_ID", "varchar(20)", false, columns);
        assertColumn("FEHLERCODE", "varchar(25)", false, columns);
        assertColumn("FEHLERMESSAGE", "varchar(255)", false, columns);
    }

    private void assertDeploymentLogTable_TillTag_1_X_FINAL(Map<String, Table> parsedTables) {
        Table table = assertTable(parsedTables, "DEPLOYMENT_LOG");
        Map<String, Column> columns = assertColumnSize(table, 4);

        assertColumn("ID", "number", true, columns);
        assertColumn("DEPLOYMENTTRIGGER_ID", "number", false, new Column("DEPLOYMENT_TRIGGER", "ID", "number"), columns);
        assertColumn("ZEITPUNKT", "datetime(6)", false, columns);
        assertColumn("DEPLOYMENTLOG_ID", "number", false, new Column(table.getName(), "ID", "number"), columns);
    }

    private void assertDeploymentlogTable_TillTag_1_X_FINAL(Map<String, Table> parsedTables) {
        assertDeploymentlogTable(parsedTables);
    }

    private void assertDeploymentUmgebungTable_TillTag_1_X_FINAL(Map<String, Table> parsedTables) {
        assertDeploymentUmgebungTable(parsedTables);
    }

    private void assertDeploymentJobTable_TillTag_1_X_FINAL(Map<String, Table> parsedTables) {
        assertDeploymentJobTable(parsedTables);
    }

    private void assertDeploymentServerTable(Map<String, Table> parsedTables) {
        Table table = parsedTables.get("DEPLOYMENT_SERVER");
        assertNotNull(table);
        assertEquals("DEPLOYMENT_SERVER", table.getName());
        assertNotNull(table.getColumns());
        Map<String, Column> columns = table.getColumnsAsMap();
        assertEquals(4, columns.size());

        assertColumn("ID", "number", true, columns);
        assertColumn("NAME", "varchar(100)", false, columns);
        assertColumn("KURZBESCHREIBUNG", "varchar(255)", false, columns);
        assertColumn("STAGE", "varchar(40)", false, columns);
    }

    private void assertDeploymentAnmeldedatenTable(Map<String, Table> parsedTables) {
        Table table = parsedTables.get("DEPLOYMENT_ANMELDEDATEN");
        assertNotNull(table);
        assertEquals("DEPLOYMENT_ANMELDEDATEN", table.getName());
        assertNotNull(table.getColumns());
        Map<String, Column> columns = table.getColumnsAsMap();
        assertEquals(5, columns.size());

        assertColumn("ID", "number", true, columns);
        assertColumn("NAME", "varchar(100)", false, columns);
        assertColumn("SERVER_ID", "number", false, new Column("DEPLOYMENT_SERVER", "ID", "number"), columns);
        assertColumn("AUTH_TYPE", "varchar(3)", false, columns);
        assertColumn("AUTH_TOKEN", "clob", false, columns);
    }

    private Map<String, Column> assertColumnSize(Table table, int expectedColumnSize) {
        assertNotNull(table.getColumns(), "Table '" + table.getName() + "':");
        assertEquals(expectedColumnSize, table.getColumns().size(), "Table '" + table.getName() + "' column size:");
        return table.getColumnsAsMap();
    }

    private Table assertTable(Map<String, Table> parsedTables, String tableName) {
        Table table = parsedTables.get(tableName);
        assertNotNull(table, "Table '" + tableName + "':");
        return table;
    }

    private void assertDeploymentTriggerTable(Map<String, Table> parsedTables) {
        Table table = assertTable(parsedTables, "DEPLOYMENT_TRIGGER");
        Map<String, Column> columns = assertColumnSize(table, 15);

        assertColumn("ID", "number", true, columns);
        assertColumn("SCHEDULE_TIMESTAMP", "datetime(6)", false, columns);
        assertColumn("BEAUFTRAGENDER_USER", "varchar(100)", false, columns);
        assertColumn("BEAUFTRAGENDER_USERNAME", "varchar(100)", false, columns);
        assertColumn("BEAUFTRAGENDER_EMAIL", "varchar(150)", false, columns);
        assertColumn("FILESHERIFF_ID", "numeric", false, new Column("FILE_SHERIFF", "ID", "number"),columns);
        assertColumn("DEPLOYMENTUMGEBUNG_ID", "numeric", false, new Column("DEPLOYMENT_UMGEBUNG", "ID", "number"),columns);
        assertColumn("STATUS", "varchar(20)", false, columns);
        assertColumn("REFERENZ_ART", "varchar(20)", false, columns);
        assertColumn("REFERENZ_ID", "varchar(20)", false, columns);
        assertColumn("FEHLERCODE", "varchar(25)", false, columns);
        assertColumn("FEHLERMESSAGE", "varchar(255)", false, columns);
        assertColumn("OUTPUT", "clob", false, columns);
        assertColumn("EXECUTION_TIMESTAMP", "datetime(6)", false, columns);
        assertColumn("TRIGGER_ID_LASTDEPLOYMENT", "number", false, new Column(table.getName(), "ID", "number"),columns);
    }

    private void assertDeploymentUmgebungTable(Map<String, Table> parsedTables) {

        Table table = parsedTables.get("DEPLOYMENT_UMGEBUNG");
        assertNotNull(table);
        assertEquals("DEPLOYMENT_UMGEBUNG", table.getName());
        assertNotNull(table.getColumns());
        Map<String, Column> columns = table.getColumnsAsMap();
        assertEquals(6, columns.size());

        assertColumn("ID", "number", true, columns);
        assertColumn("DEPLOYMENTJOB_ID", "number", false,new Column("DEPLOYMENT_JOB", "ID", "number"), columns);
        assertColumn("SUFFIX", "varchar(20)", false, columns);
        assertColumn("STAGE", "varchar(40)", false, columns);
        assertColumn("CONFIG", "clob", false, columns);
        assertColumn("STATUS", "varchar(10)", false, columns);

    }

    private void assertDeploymentlogTable(Map<String, Table> parsedTables) {
        Table table = parsedTables.get("DEPLOYMENTLOG");
        assertNotNull(table);
        assertEquals("DEPLOYMENTLOG", table.getName());
        assertNotNull(table.getColumns());
        Map<String, Column> columns = table.getColumnsAsMap();
        assertEquals(11, columns.size());

        assertColumn("ID", "number", true, columns);
        assertColumn("PROJEKTNAME", "varchar(100)", false, columns);
        assertColumn("VERSION", "varchar(20)", false, columns);
        assertColumn("CVSVERSIONSTAG", "varchar(100)", false, columns);
        assertColumn("REFERENZID", "varchar(100)", false, columns);
        assertColumn("BENUTZER", "varchar(20)", false, columns);
        assertColumn("TIMESTAMP", "date", false, columns);
        assertColumn("SRCSERVER", "varchar(50)", false, columns);
        assertColumn("DESTSERVER", "varchar(50)", false, columns);
        assertColumn("ZIELUMGEBUNG", "varchar(50)", false, columns);
        assertColumn("VORHERIGEVERSION", "number", false, columns);
    }

    private void assertDeploymentJobTable(@NotNull Map<String, Table> parsedTables) {
        Table table = parsedTables.get("DEPLOYMENT_JOB");
        assertNotNull(table);
        assertEquals("DEPLOYMENT_JOB", table.getName());
        assertNotNull(table.getColumns());
        Map<String, Column> columns = table.getColumnsAsMap();
        assertEquals(5, columns.size());

        assertColumn("ID", "number", true, columns);
        assertColumn("NAME", "varchar(100)", false, columns);
        assertColumn("PROJEKTNAME", "varchar(100)", false, columns);
        assertColumn("DEPLOYMENTART", "varchar(20)", false, columns);
        assertColumn("STATUS", "varchar(10)", false, columns);

    }

    private void assertFileSheriffTable(@NotNull  Map<String, Table> parsedTables) {
        Table table = parsedTables.get("FILE_SHERIFF");
        assertNotNull(table);
        assertEquals("FILE_SHERIFF", table.getName());
        assertNotNull(table.getColumns());
        assertEquals(8, table.getColumns().size());
        Map<String, Column> columns = table.getColumnsAsMap();

        assertColumn("ID", "number", true, columns);
        assertColumn("MD5", "varchar(32)", false, columns);
        assertColumn("FILENAME", "varchar(150)", false, columns);
        assertColumn("TIMESTAMP", "datetime(6)", false, columns);
        assertColumn("PROJEKTNAME", "varchar(100)", false, columns);
        assertColumn("TAGCVS", "varchar(200)", false, columns);
        assertColumn("VERSION", "varchar(20)", false, columns);
        assertColumn("STATUS", "varchar(25)", false, columns);

    }

    private void assertFileSheriffTable_TillTag_1_X_FINAL(@NotNull  Map<String, Table> parsedTables) {
        Table table = parsedTables.get("FILE_SHERIFF");
        assertNotNull(table);
        assertEquals("FILE_SHERIFF", table.getName());
        assertNotNull(table.getColumns());
        assertEquals(8, table.getColumns().size());
        Map<String, Column> columns = table.getColumnsAsMap();

        assertColumn("ID", "number", true, columns);
        assertColumn("MD5", "varchar(32)", false, columns);
        assertColumn("FILENAME", "varchar(150)", false, columns);
        assertColumn("TIMESTAMP", "datetime(6)", false, columns);
        assertColumn("PROJEKTNAME", "varchar(100)", false, columns);
        assertColumn("TAGCVS", "varchar(200)", false, columns);
        assertColumn("VERSION", "varchar(20)", false, columns);
        assertColumn("BUILD_ON", "varchar(50)", false, columns);

    }


    private void assertPrimaryKey(Column column) {
        assertTrue(column.isPrimary(), "Column '" + column.getName() + "' is not a primary key");
    }

    private void assertNotPrimaryKey(Column column) {
        assertFalse(column.isPrimary(), "Column '" + column.getName() + "' is a primary key");
    }

    private void assertColumn(String columnName, String type, boolean primaryKey, Map<String, Column> columns) {
        assertColumn(columnName, type, primaryKey, null, columns);
    }

    private void assertColumn(String columnName, String type, boolean primaryKey, Column referencedColumn, Map<String, Column> columns) {
        Column column = columns.get(columnName);
        assertNotNull(column, "Column '" + columnName + "':");
        assertEquals(columnName, column.getName());
        assertEquals(type, column.getType(), "Column '" + columnName +"' type: ");
        if (primaryKey) {
            assertPrimaryKey(column);
        } else {
            assertNotPrimaryKey(column);
        }
        assertEquals(referencedColumn, column.getForeignKeyColumn(), "Column '" + columnName + "' foreignKeyColumn:");
    }

}