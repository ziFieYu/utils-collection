package org.sa.utils.spark.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.sa.utils.universal.base.functions;

/**
 * @author Stuart Alex
 * @date 2017/11/29
 */
public class Bytes2Long extends UDF {

    /**
     * 转字节数组为长整型
     * 应用场景：HBase计数列对应Hive外部表字段类型为binary，取实际值需要用此UDF
     *
     * @param bytes 字节数组
     * @return Long
     */
    public Long evaluate(byte[] bytes) {
        return functions.bytes2Long(bytes);
    }

}