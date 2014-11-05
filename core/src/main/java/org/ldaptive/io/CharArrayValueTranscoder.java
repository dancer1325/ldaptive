/* See LICENSE for licensing and NOTICE for copyright. */
package org.ldaptive.io;

/**
 * Decodes and encodes a character array for use in an ldap attribute value.
 *
 * @author  Middleware Services
 */
public class CharArrayValueTranscoder
  extends AbstractStringValueTranscoder<char[]>
{


  /** {@inheritDoc} */
  @Override
  public char[] decodeStringValue(final String value)
  {
    return value.toCharArray();
  }


  /** {@inheritDoc} */
  @Override
  public String encodeStringValue(final char[] value)
  {
    return String.valueOf(value);
  }


  /** {@inheritDoc} */
  @Override
  public Class<char[]> getType()
  {
    return char[].class;
  }
}
