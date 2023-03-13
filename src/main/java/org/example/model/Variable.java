package org.example.model;

import java.io.Serializable;

public class Variable implements Serializable {

    private String name;

    private String table;

    private String field;

    private String type;

    public Variable(String name, String table, String field, String type) {
        this.name = name;
        this.table = table;
        this.field = field;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getTable() {
        return table;
    }

    public String getField() {
        return field;
    }

    public String getType() {
        return type;
    }
}
