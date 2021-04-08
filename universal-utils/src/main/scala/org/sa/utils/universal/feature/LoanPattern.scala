package org.sa.utils.universal.feature

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.Exception._

/**
 * Created by Stuart Alex on 2017/3/20.
 *
 * Loan pattern implementation
 */
object LoanPattern {
    type Closable = {def close()}

    def using[R <: Closable, A](resource: R)(f: R => A): A = {
        try {
            f(resource)
        } finally {
            ignoring(classOf[Throwable]) apply {
                resource.close()
            }
        }
    }

    /**
     * Guarantees a Closeable resource will be closed after being passed to a block that takes
     * the resource as a parameter and returns a Future.
     */
    def futureUsing[R <: Closable, A](resource: R)(f: R => Future[A])(implicit ec: ExecutionContext): Future[A] = {
        f(resource) andThen { case _ => resource.close() } // close no matter what
    }

}
