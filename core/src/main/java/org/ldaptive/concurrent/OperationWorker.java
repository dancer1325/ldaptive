/* See LICENSE for licensing and NOTICE for copyright. */
package org.ldaptive.concurrent;

import java.util.Collection;
import java.util.concurrent.Future;
import org.ldaptive.Request;
import org.ldaptive.Response;

/**
 * Interface for ldap operation workers. Operation workers leverage the
 * java.util.concurrent package to execute non-blocking operations.
 *
 * @param  <Q>  type of ldap request
 * @param  <S>  type of ldap response
 *
 * @author  Middleware Services
 */
public interface OperationWorker<Q extends Request, S>
{


  /**
   * Execute an ldap operation on a separate thread.
   *
   * @param  request  containing the data required by this operation
   *
   * @return  future response for this operation
   */
  Future<Response<S>> execute(Q request);


  /**
   * Execute an ldap operation for each request on a separate thread.
   *
   * @param  requests  containing the data required by this operation
   *
   * @return  future responses for this operation
   */
  @SuppressWarnings("unchecked")
  Collection<Future<Response<S>>> execute(Q... requests);


  /**
   * Execute an ldap operation for each request on a separate thread and waits
   * for each operation to complete.
   *
   * @param  requests  containing the data required by this operation
   *
   * @return  responses for this operation
   */
  @SuppressWarnings("unchecked")
  Collection<Response<S>> executeToCompletion(Q... requests);
}
