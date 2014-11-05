/* See LICENSE for licensing and NOTICE for copyright. */
package org.ldaptive.provider;

/**
 * Interface to describe various connection strategies. Each strategy returns an
 * ordered list of URLs to attempt when opening a connection.
 *
 * @author  Middleware Services
 */
public interface ConnectionStrategy
{

  /** default strategy. */
  ConnectionStrategy DEFAULT =
    new ConnectionStrategies.DefaultConnectionStrategy();

  /** active-passive strategy. */
  ConnectionStrategy ACTIVE_PASSIVE =
    new ConnectionStrategies.ActivePassiveConnectionStrategy();

  /** round robin strategy. */
  ConnectionStrategy ROUND_ROBIN =
    new ConnectionStrategies.RoundRobinConnectionStrategy();

  /** random strategy. */
  ConnectionStrategy RANDOM =
    new ConnectionStrategies.RandomConnectionStrategy();


  /**
   * Returns an ordered list of URLs to attempt to open.
   *
   * @param  metadata  which can be used to produce the URL list
   *
   * @return  array of ldap URLs
   */
  String[] getLdapUrls(ConnectionFactoryMetadata metadata);
}
