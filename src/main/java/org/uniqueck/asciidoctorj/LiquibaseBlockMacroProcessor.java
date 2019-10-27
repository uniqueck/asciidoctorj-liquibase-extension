package org.uniqueck.asciidoctorj;

import org.asciidoctor.ast.StructuralNode;
import org.asciidoctor.extension.BlockMacroProcessor;
import org.asciidoctor.extension.Name;
import org.uniqueck.asciidoctorj.liquibase.LiquibaseChangesetParser;
import org.uniqueck.asciidoctorj.liquibase.model.Column;
import org.uniqueck.asciidoctorj.liquibase.model.Table;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Name("liquibase")
public class LiquibaseBlockMacroProcessor extends BlockMacroProcessor {


    protected  List<String> generateAsciiDocMarkup(StructuralNode parent, File sourceFile, Map<String, Object> attributes) {
        List<String>  content = new ArrayList<>();
        Map<String, Table> parsedTables = new LiquibaseChangesetParser(sourceFile, getTillTag(attributes)).parse();

        content.add("[plantuml]");
        content.add("----");

        content.add("'hide the spot");
        content.add("hide circle");

        content.add("' avoid problems with angled crows feet");
        content.add("skinparam linetype ortho");

        content.add("'entities");
        for (Table table : parsedTables.values()) {
            content.add("Entity \"" + table.getName()+  "\" {");
            for (Column column : table.getColumns().stream().filter(Column::isPrimary).collect(Collectors.toList())) {
                content.add("<<PK>> " + column.getName() + " : " + column.getType());
            }
            content.add("--");
            for (Column column : table.getColumns().stream().filter(c -> !c.isPrimary()).collect(Collectors.toList())) {
                if (column.getForeignKeyColumn() != null) {
                    content.add("<<FK>> " + column.getName() + " : " + column.getType());
                } else {
                    content.add("       " + column.getName() + " : " + column.getType());
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

    protected File getBuildDir(StructuralNode structuralNode) {
        Map<Object, Object> globalOptions = structuralNode.getDocument().getOptions();

        String toDir = (String) globalOptions.get("to_dir");
        String destDir = (String) globalOptions.get("destination_dir");
        String buildDir = toDir != null ? toDir : destDir;
        return new File(buildDir);
    }

    protected String getAttribute(StructuralNode structuralNode, String attributeName, String defaultValue) {
        String value = (String) structuralNode.getAttribute(attributeName);

        if (value == null || value.trim().isEmpty()) {
            value = defaultValue;
        }

        return value;
    }


    protected File getTargetAsFile(StructuralNode structuralNode, String target) {
        String docdir = getAttribute(structuralNode, "docdir", "");
        return new File(docdir, target);
    }
}
