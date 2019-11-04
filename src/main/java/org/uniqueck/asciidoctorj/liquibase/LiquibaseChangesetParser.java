package org.uniqueck.asciidoctorj.liquibase;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.uniqueck.asciidoctorj.liquibase.lfet.ILiquibaseParseChangelogFile;
import org.uniqueck.asciidoctorj.liquibase.lfet.LiquibaseParseChangelogFileRules;
import org.uniqueck.asciidoctorj.liquibase.model.Column;
import org.uniqueck.asciidoctorj.liquibase.model.Table;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Getter(AccessLevel.PROTECTED)
public class LiquibaseChangesetParser implements ILiquibaseParseChangelogFile {



    public interface LoggingFacade {

        String LOG_MESSAGE_IGNORED_ELEMENT = "skip tag '%s'";
        String LOG_MESSAGE_UNSUPPORTED_ELEMENT = "Unsupported liquibase element '%s' detected";

        void logIgnoredElement(String element);
        void logUnsupportedElement(String element);
        void logParsingError(File inputFile, Exception cause);

    }

    private LoggingFacade loggingFacade;
    public static final String TABLE_NAME = "tableName";
    public static final String COLUMN = "column";
    private static final String COLUMN_NAME = "columnName";
    private final SAXBuilder saxBuilder;
    private final File masterFile;
    @Setter(AccessLevel.PACKAGE)
    private Element currentElement;
    private final Map<String, Table> parsedTables;
    private LiquibaseParseChangelogFileRules decisionTableRules;
    private final String tillTag;
    private boolean finishParsing;

    LiquibaseChangesetParser(File masterFile) {
        this(new HashMap<>(), masterFile, null, new LoggingFacade() {
            @Override
            public void logIgnoredElement(String element) {
                log.info(String.format(LOG_MESSAGE_IGNORED_ELEMENT, element));
            }

            @Override
            public void logUnsupportedElement(String element) {
                log.warn(String.format(LOG_MESSAGE_UNSUPPORTED_ELEMENT, element));
            }

            @Override
            public void logParsingError(File inputFile, Exception cause) {
                log.error("Error on parsing '" +  inputFile.getPath() + "'", cause);
            }
        });
    }

    public LiquibaseChangesetParser(File masterFile, String tillTag, LoggingFacade loggingFacade) {
        this(new HashMap<>(), masterFile, tillTag, loggingFacade);
    }

    private LiquibaseChangesetParser(Map<String, Table> parsedTables, File masterFile, String tillTag, LoggingFacade loggingFacade) {
        this.loggingFacade = loggingFacade;
        this.saxBuilder = new SAXBuilder();
        this.parsedTables = parsedTables;
        this.decisionTableRules = new LiquibaseParseChangelogFileRules();
        this.masterFile = masterFile;
        this.tillTag = tillTag;
    }

    private boolean parse(Element rootElement) {
        List<Element> elementList = rootElement.getChildren();
        for (Element element : elementList) {
            setCurrentElement(element);
            getDecisionTableRules().execute(this);
            if (isFinishParsing()) {
                break;
            }
        }
        return isFinishParsing();
    }

    public Map<String, Table> parse() {
        try {
            Document rootDocument = getSaxBuilder().build(getMasterFile());
            List<Element> listOfElements = rootDocument.getRootElement().getChildren();
            for (Element listOfElement : listOfElements) {
                setCurrentElement(listOfElement);
                getDecisionTableRules().execute(this);
                if (isFinishParsing()) {
                    break;
                }
            }
        } catch (JDOMException | IOException e) {
            getLoggingFacade().logParsingError(getMasterFile(), e);
        }
        return Collections.unmodifiableMap(parsedTables);
    }


    private Table extractTable(Namespace namespace, Element tableElement) {
        String tableName = tableElement.getAttributeValue(TABLE_NAME);
        Table table = new Table(tableName);
        List<Element> columns = tableElement.getChildren(COLUMN, namespace);
        for (Element columnElement : columns) {
            extractColumn(table, namespace, columnElement);
        }
        return table;
    }

    private void extractColumn(Table table, Namespace namespace, Element columnElement) {
        String name = columnElement.getAttributeValue("name");
        String type = columnElement.getAttributeValue("type");
        Column column = table.add(name, type);
        Element constraints = columnElement.getChild("constraints", namespace);
        if (constraints != null) {
            column.setPrimary(Boolean.parseBoolean(constraints.getAttributeValue("primaryKey")));
        }


    }

    private Namespace getNamespace4Liquibase(Element rootElement) {
        return rootElement.getNamespace();
    }

    @Override
    public boolean isElementEqualCreateTable() {
        return isElementNameEqual("createTable");
    }

    @Override
    public boolean isElementEqualInclude() {
        return isElementNameEqual("include");
    }

    @Override
    public boolean isElementEqualChangeSet() {
        return isElementNameEqual("changeSet");
    }

    @Override
    public void doExtractTable() {
        Table table = extractTable(getNamespace4Liquibase(getCurrentElement()), getCurrentElement());
        getParsedTables().put(table.getName(), table);
    }

    @Override
    public void doFollowInclude() {
        String fileName = getCurrentElement().getAttributeValue("file");
        LiquibaseChangesetParser parser = new LiquibaseChangesetParser(getParsedTables(), new File(getMasterFile().getParentFile(), fileName), getTillTag(), getLoggingFacade());
        parser.parse();
        this.finishParsing = parser.isFinishParsing();
    }

    @Override
    public void doLogUnsupportedElement() {
        getLoggingFacade().logUnsupportedElement(getCurrentElement().getAttributeValue("tag"));
    }

    @Override
    public void doTrace(String dtName, String version, int rules, int rule) {
        log.trace("DecisionTable.doTrace," +dtName +"," + version + "," + rules + ","+rule);
    }

    @Override
    public void doTraceAfterRule(String dtName, String version, int rules, int rule) {
        log.trace("DecisionTable.doTraceAfterRule," +dtName +"," + version + "," + rules + ","+rule);
    }

    @Override
    public void doParseChangeSet() {
        this.finishParsing = new LiquibaseChangesetParser(getParsedTables(), getMasterFile(), getTillTag(), getLoggingFacade()).parse(getCurrentElement());
    }

    @Override
    public boolean isElementEqualAddPrimaryKey() {
        return isElementNameEqual("addPrimaryKey");
    }

    @Override
    public void doSetPrimaryKeyForColumn() {
        String tableName = getCurrentElement().getAttributeValue(TABLE_NAME);
        String columnName = getCurrentElement().getAttributeValue("columnNames");
        getParsedTables().get(tableName).getColumns().stream().filter(c -> c.getName().equals(columnName)).findFirst().ifPresent(c -> c.setPrimary(true));
    }

    @Override
    public boolean isElementEqualDropTable() {
        return isElementNameEqual("dropTable");
    }

    private boolean isElementNameEqual(String elementName) {
        return getCurrentElement().getName().equals(elementName);
    }

    @Override
    public void doDropTable() {
        String tableName = getCurrentElement().getAttributeValue(TABLE_NAME);
        getParsedTables().remove(tableName);
    }

    @Override
    public boolean isElementEqualPreConditions() {
        return isElementNameEqual("preConditions");
    }

    @Override
    public void doIgnoreElement() {
        log.debug("Ignore liquibase element '" + getCurrentElement().getName() +"'");
    }

    @Override
    public boolean isElementEqualSql() {
        return isElementNameEqual("sql");
    }

    @Override
    public boolean isElementEqualCreateSequence() {
        return isElementNameEqual("createSequence");
    }

    @Override
    public boolean isElementEqualCreateProcedure() {
        return isElementNameEqual("createProcedure");
    }

    @Override
    public boolean isElementEqualRenameColumn() {
        return isElementNameEqual("renameColumn");
    }

    @Override
    public boolean isElementEqualComment() {
        return isElementNameEqual("comment");
    }

    @Override
    public void doRenameColumn() {
        String oldColumnName = getCurrentElement().getAttributeValue("oldColumnName");
        String newColumnName = getCurrentElement().getAttributeValue("newColumnName");
        getTableBasedOnCurrentElementAttributeValue(TABLE_NAME).getColumnsAsMap().get(oldColumnName).setName(newColumnName);
    }


    private Table getTableBasedOnCurrentElementAttributeValue(String attributeName) {
        return getParsedTables().get(getCurrentElement().getAttributeValue(attributeName));
    }
    @Override
    public boolean isElementEqualAddColumn() {
        return isElementNameEqual("addColumn");
    }

    @Override
    public boolean isElementEqualDropColumn() {
        return isElementNameEqual("dropColumn");
    }

    @Override
    public void doAddColumn() {
        Table table = getTableBasedOnCurrentElementAttributeValue(TABLE_NAME);
        List<Element> columns = getCurrentElement().getChildren(COLUMN, getCurrentElement().getNamespace());
        for (Element column : columns) {
            extractColumn(table, getCurrentElement().getNamespace(), column);
        }
    }

    @Override
    public void doDropColumn() {
        Table table = getTableBasedOnCurrentElementAttributeValue(TABLE_NAME);
        String columnName = getCurrentElement().getAttributeValue(COLUMN_NAME);
        table.removeColumn(columnName);
    }

    @Override
    public boolean isElementEqualRollback() {
        return isElementNameEqual("rollback");
    }

    @Override
    public boolean isElementEqualDropSequence() {
        return isElementNameEqual("dropSequence");
    }

    @Override
    public boolean isElementEqualAddUniqueConstraint() {
        return isElementNameEqual("addUniqueConstraint");
    }

    @Override
    public boolean isElementEqualAddForeignKeyConstraint() {
        return isElementNameEqual("addForeignKeyConstraint");
    }


    @Override
    public void doAddForeignKeyConstraint() {

        Table baseTable = getTableBasedOnCurrentElementAttributeValue("baseTableName");
        Column baseColumn = baseTable.getColumnsAsMap().get(getCurrentElement().getAttributeValue("baseColumnNames"));

        Table referencedTable = getTableBasedOnCurrentElementAttributeValue("referencedTableName");
        Column referencedColumn = referencedTable.getColumnsAsMap().get(getCurrentElement().getAttributeValue("referencedColumnNames"));

        baseColumn.setForeignKeyColumn(referencedColumn);

    }

    @Override
    public boolean isElementEqualTagDatabase() {
        return isElementNameEqual("tagDatabase");
    }

    @Override
    public boolean isTagEqualTillTag() {
        return getTillTag() != null && getTillTag().equals(getCurrentElement().getAttributeValue("tag"));
    }

    @Override
    public void doSkipTag() {
        this.finishParsing = false;
        getLoggingFacade().logIgnoredElement(getCurrentElement().getAttributeValue("tag"));

    }

    @Override
    public void doFinishParsing() {
        this.finishParsing = true;
    }
}
