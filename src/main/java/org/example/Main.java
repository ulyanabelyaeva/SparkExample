package org.example;

import org.apache.spark.sql.SparkSession;
import org.example.service.Data;
import org.example.service.RuleExecutor;
import org.example.service.RuleParser;

public class Main {
    public static void main(String args[]) {

        SparkSession sparkSession = SparkSession
                .builder()
                .master("local")
                .appName("SparkApp")
                .getOrCreate();

        //получение данных из файлов .tsv
        Data.getDataForR1(sparkSession).createOrReplaceTempView("tmp_tos_vsa_nd_nds_r1");
        Data.getDataForR3(sparkSession).createOrReplaceTempView("tmp_tos_vsa_nd_nds_r3");

        //парсинг rule.json
        RuleParser ruleParser = new RuleParser();
        ruleParser.parseRule();

        RuleExecutor ruleExecutor = new RuleExecutor(ruleParser, sparkSession);
        //выполнение джоинов
        ruleExecutor.joinTables();
        //выполнение фильтрации
        ruleExecutor.execute();

        sparkSession.stop();
    }
}