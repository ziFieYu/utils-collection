package org.sa.utils.hadoop.yarn

import org.sa.utils.universal.base.Enum

/**
 * Created by Stuart Alex on 2017/9/6.
 */
object OutputType extends Enum {
    val err: Value = Value("stderr")
    val out: Value = Value("stdout")
}
