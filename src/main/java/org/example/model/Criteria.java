package org.example.model;

import java.io.Serializable;

public class Criteria implements Serializable {

    private Long id;

    private String parameter;

    private String operator;

    private String value;

    public Criteria(Long id, String parameter, String operator, String value) {
        this.id = id;
        this.parameter = parameter;
        this.operator = operator;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public String getParameter() {
        return parameter;
    }

    public String getOperator() {
        return operator;
    }

    public String getValue() {
        return value;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
