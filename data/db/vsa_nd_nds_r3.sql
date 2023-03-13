CREATE TABLE `tmp_tos.vsa_nd_nds_r3`(
  `date_creation` date, 
  `fid` decimal(38,0), 
  `s109_5` decimal(38,0), 
  `s120_3` decimal(38,0), 
  `s3_5` decimal(38,0), 
  `year` decimal(38,0), 
  `quarter` decimal(38,0),
)
ROW FORMAT SERDE 
  'org.apache.hadoop.hive.ql.io.orc.OrcSerde' 
STORED AS INPUTFORMAT 
  'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat' 
OUTPUTFORMAT 
  'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat'
