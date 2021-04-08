package org.sa.utils.universal.feature

import org.sa.utils.universal.feature.SyntaxSugar._

object IF {
    def apply[T](expression: => Boolean, trueValue: => T, falseValue: => T) = new IF(expression, () => trueValue, () => falseValue)
}

private[feature] class IF[T](expression: Boolean, trueValue: () => T, falseValue: () => T) {

    def eval: T = expression ? trueValue.apply() `:` falseValue.apply()

}