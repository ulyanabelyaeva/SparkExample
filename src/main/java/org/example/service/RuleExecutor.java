package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.example.model.Criteria;
import org.example.model.Join;
import org.example.model.Response;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RuleExecutor implements Serializable {

    private RuleParser ruleParser;

    private Set<String> tableNames;

    private SparkSession sparkSession;

    private List<Response> responseList;

    public RuleExecutor(RuleParser ruleParser, SparkSession sparkSession) {
        this.ruleParser = ruleParser;
        this.sparkSession = sparkSession;
        this.tableNames = new HashSet<>();
        this.responseList = new ArrayList<>();
        tableNames.add("tmp_tos.vsa_nd_nds_r1");
        tableNames.add("tmp_tos.vsa_nd_nds_r3");
    }

    /**
     * метод для выполнения джоинов
     * */
    public void joinTables(){
        //зпрос для джоина
        String joinQuery = "SELECT * FROM ";
        for (Join join : ruleParser.getJoins()) {
            //таблица, которую надо заджонить должна существовать (таблица tmp_tos.vsa_nd_nds_r2 есть в rule.json, но
            // она не создается)
            if (tableNames.contains(join.getTableLeft()) && tableNames.contains(join.getTableRight())) {
                String nameTableLeft = join.getTableLeft().replace(".", "_");
                String nameTableRight = join.getTableRight().replace(".", "_");
                //составление запроса
                joinQuery = joinQuery + nameTableLeft + " " + join.getType() + " JOIN " + nameTableRight +
                        " ON " + nameTableLeft + "." + join.getEntityLeft() + " = " + nameTableRight + "." +
                        join.getEntityRight() + " ";
            }
        }

        //выполнение джона, переименвание повторяющихся столбцов, создание представления
        Dataset<Row> dataset = sparkSession.sql(joinQuery)
                .toDF("fid", "year", "quarter", "date_receipt", "s40", "code_period", "code_present_place",
                        "fid2", "year2", "quarter2", "s120_3", "s109_5", "s3_5", "date_creation");
        dataset.createOrReplaceTempView("afterJoinView");

        System.out.println("До фильтрации: " + dataset.count());
    }

    /**
     * метод для выполнения фильтрации
     * */
    public void execute() {
        //запрос фильтрации (выборка из представления, полученного джонами)
        String criteriaQuery = "SELECT afterjoinview.fid, afterjoinview.year, afterjoinview.quarter FROM afterJoinView WHERE ";
        //итерируемся по критериям
        for (int i = 0; i < ruleParser.getTree().getCriterias().size(); i++) {
            Long criteriaId = ruleParser.getTree().getCriterias().get(i);
            Criteria criteria = ruleParser.getCriterias().get(criteriaId);
            //удаление лишних фигурных скобок
            if (criteria.getValue().contains("{")) {
                criteria.setValue(criteria.getValue().replace("{", ""));
                criteria.setValue(criteria.getValue().replace("}", ""));
            }
            //если параметр задан выражением
            if (ruleParser.getParameters().containsKey(criteria.getParameter()))
                criteria.setParameter(ruleParser.getParameters().get(criteria.getParameter()).getValue());
            //составление запроса
            switch (criteria.getOperator()){
                case "lt":{
                    criteriaQuery = criteriaQuery + criteria.getParameter() + " < " + criteria.getValue();
                    break;
                }
                case "gt":{
                    criteriaQuery = criteriaQuery + criteria.getParameter() + " > " + criteria.getValue();
                    break;
                }
                case "eq":{
                    criteriaQuery = criteriaQuery + criteria.getParameter() + " = " + criteria.getValue();
                    break;
                }
            }
            //добавление and в where
            if (i != ruleParser.getTree().getCriterias().size() - 1)
                criteriaQuery = criteriaQuery + " AND ";
        }
        //выполнение запроса
        Dataset<Row> dataset = sparkSession.sql(criteriaQuery);
        //конфертация в pojo
        Encoder<Response> encoder = Encoders.bean(Response.class);
        Dataset<Response> responseDataset = dataset.as(encoder);
        responseList = responseDataset.collectAsList();

        System.out.println("После фильтрации: " + dataset.count());
        writeToFile();
    }

    /**
     * метод для записи результирующего списка в файл в формате json
     * */
    private void writeToFile() {
        File file = new File(System.getProperty("user.dir") + "/data/response.json");
        if (file.exists())
            file.delete();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            bufferedWriter.write("[ \n");
            for (Response response :
                    responseList) {
                String json = ow.writeValueAsString(response);
                bufferedWriter.write(json);
                bufferedWriter.write(", \n");
            }
            bufferedWriter.write("]");
        } catch (IOException e) {
            System.out.println("не удалось записать данные в файл");
        }
    }
}
