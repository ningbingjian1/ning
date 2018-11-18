# coding=UTF-8
#aws s3 ls   s3://huan-bigdata/preprocess/huan/tcl/live/live_tcl_log/dt=2018-03-26/
import os
import uuid
"".endswith()
tmpdir = "/tmp/" + str(uuid.uuid4())
#os.mkdir()
s3path = raw_input("请输入s3路径:\n")
hiveTable = raw_input("请输入hive表名\n")
partitions = raw_input("请问导入到hive表的哪个分区,格式dt='2016-01-01',city='beijing'\n")
dataPath = tmpdir + "/*"
if not partitions:
    partitions = "(" + partitions + ")"
    cmd ='hive -e "' "LOAD DATA INPATH '" + dataPath + "'" + " OVERWRITE INTO TABLE " + hiveTable + "PARTITION" + partitions + '"'
else :
    cmd = 'hive -e "' "LOAD DATA INPATH '" + dataPath + "'" + " OVERWRITE INTO TABLE " + hiveTable + '"'
print(cmd)

