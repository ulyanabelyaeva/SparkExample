package org.example.service;

import org.example.model.Criteria;
import org.example.model.Join;
import org.example.model.Parameter;
import org.example.model.Tree;
import org.example.model.Variable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleParser implements Serializable {

    private List<Join> joins;

    private Map<String, Variable> variables;

    private Map<String, Parameter> parameters;

    private Map<Long, Criteria> criterias;

    private Tree tree;

    public RuleParser() {
        joins = new ArrayList<>();
        variables = new HashMap<>();
        parameters = new HashMap<>();
        criterias = new HashMap<>();
    }

    /**
     * метод для парсинга rule.json
     * */
    public void parseRule(){
        try {
            String data = Files.readString(Path.of(System.getProperty("user.dir") + "/data/rule.json"));
            JSONObject root = new JSONObject(data);
            JSONArray joinsArray = root.getJSONArray("joins");
            for (int i = 0; i < joinsArray.length(); i++) {
                JSONObject object = joinsArray.getJSONObject(i);
                joins.add(new Join(
                        object.getString("table_left"),
                        object.getString("table_right"),
                        object.getString("entity_left"),
                        object.getString("entity_right"),
                        object.getString("type")
                ));
            }
            JSONArray variablesArray = root.getJSONArray("variables");
            for (int i = 0; i < variablesArray.length(); i++) {
                JSONObject object = variablesArray.getJSONObject(i);
                variables.put(object.getString("name"), new Variable(
                        object.getString("name"),
                        object.getString("table"),
                        object.getString("field"),
                        object.getString("type")
                ));
            }
            JSONArray parametersArray = root.getJSONArray("parameters");
            for (int i = 0; i < parametersArray.length(); i++) {
                JSONObject object = parametersArray.getJSONObject(i);
                parameters.put(object.getString("name"), new Parameter(
                        object.getString("name"),
                        object.getString("value"),
                        object.getString("type")
                ));
            }
            JSONArray criteriasArray = root.getJSONArray("criterias");
            for (int i = 0; i < criteriasArray.length(); i++) {
                JSONObject object = criteriasArray.getJSONObject(i);
                criterias.put(object.getLong("id"), new Criteria(
                        object.getLong("id"),
                        object.getString("parameter"),
                        object.getString("operator"),
                        object.getString("value")
                ));
            }
            JSONArray treeArray = root.getJSONArray("tree");
            JSONObject object = treeArray.getJSONObject(0);
            tree = new Tree(object.getLong("id"));
            JSONArray treeCriterias = object.getJSONArray("criterias");
            for (int i = 0; i < treeCriterias.length(); i++) {
                tree.getCriterias().add(treeCriterias.getLong(i));
            }
        } catch (IOException e) {
            System.out.println("Не удалось найти rule.json");
        }
    }

    public List<Join> getJoins() {
        return joins;
    }

    public Map<String, Variable> getVariables() {
        return variables;
    }

    public Map<String, Parameter> getParameters() {
        return parameters;
    }

    public Map<Long, Criteria> getCriterias() {
        return criterias;
    }

    public Tree getTree() {
        return tree;
    }
}
