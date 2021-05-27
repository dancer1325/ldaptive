/* See LICENSE for licensing and NOTICE for copyright. */
package org.ldaptive.filter;

import java.lang.reflect.Constructor;
import org.ldaptive.LdapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Encapsulates a {@link FilterFunction} and exposes a convenience static method for parsing filters. The filter
 * function used by this class can be set using the system property {@link #FILTER_FUNCTION_PROPERTY}.
 *
 * @author  Middleware Services
 */
public final class FilterParser
{

  /** Ldap filter function system property. */
  private static final String FILTER_FUNCTION_PROPERTY = "org.ldaptive.filter.function";

  /** Logger for this class. */
  private static final Logger LOGGER = LoggerFactory.getLogger(FilterParser.class);

  /** Default filter function. */
  private static final FilterFunction FILTER_FUNCTION = getFilterFunction();

  /** Custom filter parser constructor. */
  private static final Constructor<?> FILTER_FUNCTION_CONSTRUCTOR;

  static {
    // Initialize a custom filter function if a system property is found
    FILTER_FUNCTION_CONSTRUCTOR = LdapUtils.createConstructorFromProperty(FILTER_FUNCTION_PROPERTY);
    if (FILTER_FUNCTION_CONSTRUCTOR != null) {
      LOGGER.info("Setting ldap filter function to {}", FILTER_FUNCTION_CONSTRUCTOR);
    }
  }


  /** Default constructor. */
  private FilterParser() {}


  /**
   * The {@link #FILTER_FUNCTION_PROPERTY} property is checked and that class is loaded if provided. Otherwise the
   * {@link DefaultFilterFunction} is returned.
   *
   * @return  default filter function
   */
  public static FilterFunction getFilterFunction()
  {
    if (FILTER_FUNCTION_CONSTRUCTOR != null) {
      try {
        return (FilterFunction) FILTER_FUNCTION_CONSTRUCTOR.newInstance();
      } catch (Exception e) {
        LOGGER.error("Error creating new filter function instance with {}", FILTER_FUNCTION_CONSTRUCTOR, e);
        throw new IllegalStateException(e);
      }
    }
    return new DefaultFilterFunction();
  }


  /**
   * Parse the supplied filter string.
   *
   * @param  filter  to parse
   *
   * @return  search filter
   *
   * @throws  FilterParseException  if filter is invalid
   */
  public static Filter parse(final String filter)
    throws FilterParseException
  {
    return FILTER_FUNCTION.parse(filter);
  }
}
