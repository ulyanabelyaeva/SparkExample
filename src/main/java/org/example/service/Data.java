package org.example.service;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class Data {

    public static Dataset<Row> getDataForR1(SparkSession sparkSession){
        return  sparkSession.read()
                .format("csv")
                .option("multiline", true)
                .option("header", true)
                .option("delimiter", "\t")
                .load( System.getProperty("user.dir") + "/data/db/tmp_tos_vsa_nd_nds_r1_2.tsv");
    }

    public static Dataset<Row> getDataForR3(SparkSession sparkSession){
        return  sparkSession.read()
                .format("csv")
                .option("multiline", true)
                .option("header", true)
                .option("delimiter", "\t")
                .load( System.getProperty("user.dir") + "/data/db/tmp_tos_vsa_nd_nds_r3_2.tsv");
    }
}
