package org.uniqueck.asciidoctorj.liquibase;

import com.uniqueck.asciidoctorj.extension.support.RequireLibraryAsciidoctorDiagram;
import org.asciidoctor.ast.StructuralNode;
import org.asciidoctor.extension.BlockMacroProcessor;
import org.asciidoctor.extension.Name;
import org.asciidoctor.log.LogRecord;
import org.asciidoctor.log.Severity;
import org.uniqueck.asciidoctorj.liquibase.model.Column;
import org.uniqueck.asciidoctorj.liquibase.model.Table;
import org.uniqueck.asciidoctorj.liquibase.parser.LiquibaseChangesetParser;
import org.uniqueck.asciidoctorj.liquibase.parser.LoggingFacade;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Name(LiquibaseBlockMacroProcessor.NAME)
@RequireLibraryAsciidoctorDiagram
public class LiquibaseBlockMacroProcessor extends BlockMacroProcessor {

    static final String NAME = "liquibase";

    List<String> generateAsciiDocMarkup(@SuppressWarnings("unused") StructuralNode parent, File sourceFile, Map<String, Object> attributes) {
        List<String>  content = new ArrayList<>();
        Map<String, Table> parsedTables = LiquibaseChangesetParser.xmlParser(new LoggingFacade() {
            @Override
            public void logIgnoredElement(String element) {
                log(new LogRecord(Severity.DEBUG, String.format(LOG_MESSAGE_IGNORED_ELEMENT, element)));
            }

            @Override
            public void logUnsupportedElement(String element) {
                log(new LogRecord(Severity.WARN, String.format(LOG_MESSAGE_UNSUPPORTED_ELEMENT, element)));
            }

            @Override
            public void logParsingError(File inputFile, Exception cause) {
                log(new LogRecord(Severity.FATAL, String.format(LOG_ERROR_ON_PARSING_FILE, inputFile.getPath(), cause.getMessage())));
            }
        }).parse(sourceFile, getTillTag(attributes));

        content.add("[plantuml]");
        content.add("----");

        content.add("'hide the spot");
        content.add("hide circle");
        content.add("skinparam tabSize 4");

        content.add("' avoid problems with angled crows feet");
        content.add("skinparam linetype ortho");

        content.add("'entities");
        for (Table table : parsedTables.values()) {
            content.add("Entity \"" + table.getName()+  "\" {");
            for (Column column : table.getPrimarykeyColumns()) {
                content.add("<<PK>>\\t" + column.getName() + " : " + column.getType());
            }
            content.add("--");
            for (Column column : table.getNonPrimarykeyColumns()) {
                if (column.getForeignKeyColumn() != null) {
                    content.add("<<FK>>\\t" + column.getName() + " : " + column.getType());
                } else {
                    content.add("\\t\\t" + column.getName() + " : " + column.getType());
                }

            }
            content.add("}");
        }

        content.add("'relationships");

        for (Table table : parsedTables.values()) {
            String baseTableName = table.getName();
            List<String> referencedTables = table.getReferencedTables();

            if (!referencedTables.isEmpty()) {
                for (String referencedTableName : referencedTables) {
                    content.add(baseTableName + " --- " +  referencedTableName);
                }
            }
        }


        content.add("----");

        return content;
    }

    private String getTillTag(Map<String, Object> attributes) {
        return (String) attributes.getOrDefault("tillTag", null);
    }

    @Override
    public Object process(StructuralNode parent, String target, Map<String, Object> attributes) {
        parseContent(parent, generateAsciiDocMarkup(parent, getTargetAsFile(parent, target), attributes));
        return null;
    }

    @SuppressWarnings("unused")
    private File getBuildDir(StructuralNode structuralNode) {
        Map<Object, Object> globalOptions = structuralNode.getDocument().getOptions();

        String toDir = (String) globalOptions.get("to_dir");
        String destDir = (String) globalOptions.get("destination_dir");
        String buildDir = toDir != null ? toDir : destDir;
        return new File(buildDir);
    }

    private String getAttribute(StructuralNode structuralNode, String attributeName, String defaultValue) {
        String value = (String) structuralNode.getAttribute(attributeName);

        if (value == null || value.trim().isEmpty()) {
            value = defaultValue;
        }

        return value;
    }


    private File getTargetAsFile(StructuralNode structuralNode, String target) {
        String docdir = getAttribute(structuralNode, "docdir", "");
        return "".equals(docdir) ? new File(target) : new File(docdir, target);
    }
}
