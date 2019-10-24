package org.uniqueck.asciidoctorj.liquibase.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data
public class Column {

    String name;
    final String type;
    @EqualsAndHashCode.Exclude
    boolean primary;
    @ToString.Exclude
    final String ownedTableName;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    Column foreignKeyColumn;

    public Column(String tableName, String name, String type) {
        this.ownedTableName = tableName;
        this.name = name;
        this.type = type;
    }

}
