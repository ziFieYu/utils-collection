package org.sa.utils.universal.formats.avro

import com.fasterxml.jackson.core.JsonToken

/**
 * Created by Stuart Alex on 2021/3/18.
 */
case class JsonElement(val token: JsonToken, val value: String = null)
