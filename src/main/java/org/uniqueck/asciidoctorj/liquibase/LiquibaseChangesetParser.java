package org.uniqueck.asciidoctorj.liquibase;

import lombok.AccessLevel;
import lombok.Getter;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.uniqueck.asciidoctorj.liquibase.model.Column;
import org.uniqueck.asciidoctorj.liquibase.model.Table;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter(AccessLevel.PROTECTED)
public class LiquibaseChangesetParser {


    final private SAXBuilder saxBuilder;

    public LiquibaseChangesetParser() {
        this.saxBuilder = new SAXBuilder();
    }

    public Map<String, Table> parse(File masterFile) {
        Map<String, Table> parsedTables = new HashMap<>();
        try {
            Document rootDocument = getSaxBuilder().build(masterFile);
            Namespace namespace = getNamespace4Liquibase(rootDocument.getRootElement());

            List<Element> changeSets = rootDocument.getRootElement().getChildren("changeSet", namespace);
            for (Element changeSet : changeSets) {
                Element documentTable = changeSet.getChild("createTable", namespace);
                if (documentTable != null) {
                    Table table = extractTable(namespace, documentTable);
                    parsedTables.put(table.getName(), table);
                }
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parsedTables;
    }


    protected Table extractTable(Namespace namespace, Element tableElement) {
        String tableName = tableElement.getAttributeValue("tableName");
        Table table = new Table(tableName);
        List<Element> columns = tableElement.getChildren("column", namespace);
        for (Element columnElement : columns) {
            extractColumn(table, namespace, columnElement);
        }
        return table;
    }

    protected Column extractColumn(Table table, Namespace namespace, Element columnElement) {
        String name = columnElement.getAttributeValue("name");
        String type = columnElement.getAttributeValue("type");
        return table.add(name, type);
    }

    protected Namespace getNamespace4Liquibase(Element rootElement) {
        return rootElement.getNamespace();
    }

}
