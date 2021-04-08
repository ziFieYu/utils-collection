package org.sa.utils.spark.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.sa.utils.universal.base.functions;


/**
 * @author Stuart Alex
 * @date 2017/11/29
 */
public class SubstringIndex extends UDF {

    public String evaluate(String source, String delimiter, int count) {
        return functions.substringIndex(source, delimiter, count);
    }

}