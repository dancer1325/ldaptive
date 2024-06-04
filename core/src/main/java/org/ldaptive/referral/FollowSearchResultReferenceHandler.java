/* See LICENSE for licensing and NOTICE for copyright. */
package org.ldaptive.referral;

import java.util.ArrayList;
import java.util.List;
import org.ldaptive.ConnectionFactory;
import org.ldaptive.LdapEntry;
import org.ldaptive.LdapURL;
import org.ldaptive.SearchOperation;
import org.ldaptive.SearchRequest;
import org.ldaptive.SearchResponse;
import org.ldaptive.SearchResultReference;
import org.ldaptive.handler.SearchResultHandler;
import org.ldaptive.transport.DefaultSearchOperationHandle;

/**
 * Provides handling of an ldap continuation reference for search operations.
 *
 * @author  Middleware Services
 */
public class FollowSearchResultReferenceHandler extends AbstractFollowReferralHandler<SearchRequest, SearchResponse>
  implements SearchResultHandler
{


  /** Creates a new search result reference handler. */
  public FollowSearchResultReferenceHandler()
  {
    this(DEFAULT_REFERRAL_LIMIT, 1, new DefaultReferralConnectionFactory());
  }


  /**
   * Creates a new search result reference handler.
   *
   * @param  factory  referral connection factory
   */
  public FollowSearchResultReferenceHandler(final ReferralConnectionFactory factory)
  {
    this(DEFAULT_REFERRAL_LIMIT, 1, factory);
  }


  /**
   * Creates a new search result reference handler.
   *
   * @param  limit  number of referrals to follow
   */
  public FollowSearchResultReferenceHandler(final int limit)
  {
    this(limit, 1, new DefaultReferralConnectionFactory());
  }


  /**
   * Creates a new search result reference handler.
   *
   * @param  limit  number of referrals to follow
   * @param  factory  referral connection factory
   */
  public FollowSearchResultReferenceHandler(final int limit, final ReferralConnectionFactory factory)
  {
    this(limit, 1, factory);
  }


  /**
   * Creates a new search result reference handler.
   *
   * @param  limit  number of referrals to follow
   * @param  depth  number of referrals followed
   * @param  factory  referral connection factory
   */
  private FollowSearchResultReferenceHandler(final int limit, final int depth, final ReferralConnectionFactory factory)
  {
    super(limit, depth, factory);
  }


  @Override
  protected SearchRequest createReferralRequest(final LdapURL url)
  {
    return SearchRequest.builder()
      .controls(getRequest().getControls())
      .scope(!url.getUrl().isDefaultScope() ? url.getUrl().getScope() : getRequest().getSearchScope())
      .dn(!url.getUrl().isDefaultBaseDn() ? url.getUrl().getBaseDn() : getRequest().getBaseDn())
      .filter(
        !url.getUrl().isDefaultFilter() ? url.getUrl().getParsedFilter() : getRequest().getFilter())
      .sizeLimit(getRequest().getSizeLimit())
      .timeLimit(getRequest().getTimeLimit())
      .typesOnly(getRequest().isTypesOnly())
      .returnAttributes(getRequest().getReturnAttributes())
      .aliases(getRequest().getDerefAliases())
      .binaryAttributes(getRequest().getBinaryAttributes())
      .build();
  }


  @Override
  protected SearchOperation createReferralOperation(final ConnectionFactory factory)
  {
    final DefaultSearchOperationHandle handle = (DefaultSearchOperationHandle) getHandle();
    final SearchOperation op = new SearchOperation(factory);
    op.setResultHandlers(handle.getOnResult());
    op.setEntryHandlers(handle.getOnEntry());
    op.setReferenceHandlers(handle.getOnReference());
    op.setControlHandlers(handle.getOnControl());
    op.setExceptionHandler(handle.getOnException());
    op.setIntermediateResponseHandlers(handle.getOnIntermediate());
    op.setReferralHandlers(handle.getOnReferral());
    op.setUnsolicitedNotificationHandlers(handle.getOnUnsolicitedNotification());
    op.setSearchResultHandlers(
      new FollowSearchResultReferenceHandler(
        getReferralLimit(),
        getReferralDepth() + 1,
        getReferralConnectionFactory()));
    return op;
  }


  @Override
  public SearchResponse apply(final SearchResponse result)
  {
    if (result.getReferences() == null || result.getReferences().isEmpty()) {
      return result;
    }
    final SearchResponse referralResult = SearchResponse.copy(result);
    final List<SearchResultReference> refsToAdd = new ArrayList<>();
    final List<SearchResultReference> refsToRemove = new ArrayList<>();
    if (referralDepth <= referralLimit) {
      for (SearchResultReference ref : referralResult.getReferences()) {
        refsToRemove.add(ref);
        final SearchResponse sr = followReferral(ref.getUris());
        if (sr != null) {
          sr.getEntries().forEach(e -> {
            final LdapEntry entry = LdapEntry.builder()
              .copy(e)
              .messageID(referralResult.getMessageID())
              .build();
            if (!referralResult.getEntries().contains(entry)) {
              referralResult.addEntries(entry);
            }
          });
          sr.getReferences().forEach(r -> {
            final SearchResultReference reference = SearchResultReference.builder()
              .copy(r)
              .messageID(referralResult.getMessageID())
              .build();
            refsToAdd.add(reference);
          });
        }
      }
      refsToRemove.forEach(referralResult::removeReferences);
      for (SearchResultReference reference : refsToAdd) {
        if (!referralResult.getReferences().contains(reference)) {
          referralResult.addReferences(reference);
        }
      }
    }
    return referralResult;
  }


  @Override
  public FollowSearchResultReferenceHandler newInstance()
  {
    return new FollowSearchResultReferenceHandler(
      getReferralLimit(), getReferralDepth(), getReferralConnectionFactory());
  }
}
