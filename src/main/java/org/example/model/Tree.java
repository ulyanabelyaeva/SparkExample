package org.example.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tree implements Serializable {

    private Long id;

    private List<Long> criterias;

    public Tree(Long id) {
        this.id = id;
        criterias = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public List<Long> getCriterias() {
        return criterias;
    }
}
