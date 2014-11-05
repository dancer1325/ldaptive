/* See LICENSE for licensing and NOTICE for copyright. */
package org.ldaptive.io;

/**
 * Decodes and encodes an integer for use in an ldap attribute value.
 *
 * @author  Middleware Services
 */
public class IntegerValueTranscoder
  extends AbstractPrimitiveValueTranscoder<Integer>
{


  /** Default constructor. */
  public IntegerValueTranscoder() {}


  /**
   * Creates a new integer value transcoder.
   *
   * @param  b  whether this transcoder is operating on a primitive
   */
  public IntegerValueTranscoder(final boolean b)
  {
    setPrimitive(b);
  }


  /** {@inheritDoc} */
  @Override
  public Integer decodeStringValue(final String value)
  {
    return Integer.valueOf(value);
  }


  /** {@inheritDoc} */
  @Override
  public Class<Integer> getType()
  {
    return isPrimitive() ? int.class : Integer.class;
  }
}
