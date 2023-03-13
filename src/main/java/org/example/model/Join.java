package org.example.model;

import java.io.Serializable;

public class Join implements Serializable {

    private String tableLeft;

    private String tableRight;

    private String entityLeft;

    private String entityRight;

    private String type;

    public Join(String tableLeft, String tableRight, String entityLeft, String entityRight, String type) {
        this.tableLeft = tableLeft;
        this.tableRight = tableRight;
        this.entityLeft = entityLeft;
        this.entityRight = entityRight;
        this.type = type;
    }

    public String getTableLeft() {
        return tableLeft;
    }

    public String getTableRight() {
        return tableRight;
    }

    public String getEntityLeft() {
        return entityLeft;
    }

    public String getEntityRight() {
        return entityRight;
    }

    public String getType() {
        return type;
    }
}
