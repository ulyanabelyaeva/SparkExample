package org.example.model;

import java.io.Serializable;

public class Parameter implements Serializable {

    private String name;

    private String value;

    private String type;

    public Parameter(String name, String value, String type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }
}
