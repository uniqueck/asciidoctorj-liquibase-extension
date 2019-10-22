package org.uniqueck.asciidoctorj.liquibase.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        Column column = new Column(name, type);
        this.columns.add(column);
        return column;
    }

    public List<Column> getColums() {
        return Collections.unmodifiableList(columns);
    }


}
