/* See LICENSE for licensing and NOTICE for copyright. */
package org.ldaptive.control;

import org.ldaptive.LdapUtils;

/**
 * Used by {@link SortRequestControl} to declare how sorting should occur. See
 * RFC 3698 for the definition of matchingRuleId.
 *
 * @author  Middleware Services
 */
public class SortKey
{

  /** hash code seed. */
  private static final int HASH_CODE_SEED = 739;

  /** attribute description. */
  private String attributeDescription;

  /** matching rule id. */
  private String matchingRuleId;

  /** reverse order. */
  private boolean reverseOrder;


  /** Default constructor. */
  public SortKey() {}


  /**
   * Creates a new sort key.
   *
   * @param  attrDescription  attribute description
   */
  public SortKey(final String attrDescription)
  {
    setAttributeDescription(attrDescription);
  }


  /**
   * Creates a new sort key.
   *
   * @param  attrDescription  attribute description
   * @param  ruleId  matching rule id
   */
  public SortKey(final String attrDescription, final String ruleId)
  {
    setAttributeDescription(attrDescription);
    setMatchingRuleId(ruleId);
  }


  /**
   * Creates a new sort key.
   *
   * @param  attrDescription  attribute description
   * @param  ruleId  matching rule id
   * @param  reverse  reverse order
   */
  public SortKey(
    final String attrDescription,
    final String ruleId,
    final boolean reverse)
  {
    setAttributeDescription(attrDescription);
    setMatchingRuleId(ruleId);
    setReverseOrder(reverse);
  }


  /**
   * Returns the attribute description.
   *
   * @return  attribute description
   */
  public String getAttributeDescription()
  {
    return attributeDescription;
  }


  /**
   * Sets the attribute description.
   *
   * @param  s  attribute description
   */
  public void setAttributeDescription(final String s)
  {
    attributeDescription = s;
  }


  /**
   * Returns the matching rule id.
   *
   * @return  matching rule id
   */
  public String getMatchingRuleId()
  {
    return matchingRuleId;
  }


  /**
   * Sets the matching rule id.
   *
   * @param  s  matching rule id
   */
  public void setMatchingRuleId(final String s)
  {
    matchingRuleId = s;
  }


  /**
   * Returns whether results should be in reverse sorted order.
   *
   * @return  whether results should be in reverse sorted order
   */
  public boolean getReverseOrder()
  {
    return reverseOrder;
  }


  /**
   * Sets whether results should be in reverse sorted order.
   *
   * @param  b  whether results should be in reverse sorted order
   */
  public void setReverseOrder(final boolean b)
  {
    reverseOrder = b;
  }


  /** {@inheritDoc} */
  @Override
  public boolean equals(final Object o)
  {
    return LdapUtils.areEqual(this, o);
  }


  /** {@inheritDoc} */
  @Override
  public int hashCode()
  {
    return
      LdapUtils.computeHashCode(
        HASH_CODE_SEED,
        attributeDescription,
        matchingRuleId,
        reverseOrder);
  }


  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    return
      String.format(
        "[%s@%d::attributeDescription=%s, matchingRuleId=%s, reverseOrder=%s]",
        getClass().getName(),
        hashCode(),
        attributeDescription,
        matchingRuleId,
        reverseOrder);
  }
}
