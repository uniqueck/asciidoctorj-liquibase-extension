package org.uniqueck.asciidoctorj.liquibase.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ToString
@EqualsAndHashCode
@Getter
public class Table {

    final String name;

    final List<Column> columns;

    public Table(String name) {
        this.name = name;
        this.columns = new ArrayList<>();
    }

    public Column add(String name, String type) {
        Column column = new Column(getName(), name, type);
        this.columns.add(column);
        return column;
    }

    public List<Column> getColums() {
        return Collections.unmodifiableList(columns);
    }

    public Map<String, Column> getColumnsAsMap() {
        return getColumns().stream().collect(Collectors.toMap(Column::getName, c -> c));
    }

    public void removeColumn(String columnName) {
        Column column = getColumnsAsMap().get(columnName);
        this.columns.remove(column);
    }

    public List<String> getReferencedTables() {
        return getColumns().stream().filter(c -> c.getForeignKeyColumn() != null).map(c -> c.getForeignKeyColumn().getOwnedTableName()).distinct().collect(Collectors.toList());
    }

    public List<Column> getPrimarykeyColumns() {
        return getColumns().stream().filter(Column::isPrimary).collect(Collectors.toList());
    }

    public List<Column> getNonPrimarykeyColumns() {
        return getColumns().stream().filter(Column::isNotPrimary).collect(Collectors.toList());
    }

}
