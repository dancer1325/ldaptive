/* See LICENSE for licensing and NOTICE for copyright. */
package org.ldaptive.ad.control.util;

import org.ldaptive.ConnectionFactory;
import org.ldaptive.LdapException;
import org.ldaptive.SearchOperation;
import org.ldaptive.SearchRequest;
import org.ldaptive.SearchResponse;
import org.ldaptive.ad.control.DirSyncControl;
import org.ldaptive.ad.control.ExtendedDnControl;
import org.ldaptive.ad.control.ShowDeletedControl;
import org.ldaptive.control.RequestControl;
import org.ldaptive.control.util.CookieManager;
import org.ldaptive.control.util.DefaultCookieManager;
import org.ldaptive.handler.ExceptionHandler;
import org.ldaptive.handler.LdapEntryHandler;
import org.ldaptive.handler.ResultHandler;
import org.ldaptive.handler.ResultPredicate;
import org.ldaptive.handler.SearchReferenceHandler;
import org.ldaptive.handler.SearchResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client that simplifies using the active directory dir sync control.
 *
 * @author  Middleware Services
 */
public class DirSyncClient
{

  /** Logger for this class. */
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  /** Connection factory to get a connection from. */
  private final ConnectionFactory factory;

  /** DirSync flags. */
  private final DirSyncControl.Flag[] dirSyncFlags;

  /** Maximum attribute count. */
  private final int maxAttributeCount;

  /** ExtendedDn flags. */
  private ExtendedDnControl.Flag extendedDnFlag = ExtendedDnControl.Flag.STANDARD;

  /** Functions to handle response results. */
  private ResultHandler[] resultHandlers;

  /** Function to handle exceptions. */
  private ExceptionHandler exceptionHandler;

  /** Function to test results. */
  private ResultPredicate throwCondition;

  /** Functions to handle response entries. */
  private LdapEntryHandler[] entryHandlers;

  /** Functions to handle response references. */
  private SearchReferenceHandler[] referenceHandlers;

  /** Functions to handle response results. */
  private SearchResultHandler[] searchResultHandlers;


  /**
   * Creates a new dir sync client.
   *
   * @param  cf  to get a connection from
   */
  public DirSyncClient(final ConnectionFactory cf)
  {
    this(cf, null, 0);
  }


  /**
   * Creates a new dir sync client.
   *
   * @param  cf  to get a connection from
   * @param  dsFlags  to set on the dir sync control
   */
  public DirSyncClient(final ConnectionFactory cf, final DirSyncControl.Flag[] dsFlags)
  {
    this(cf, dsFlags, 0);
  }


  /**
   * Creates a new dir sync client.
   *
   * @param  cf  to get a connection from
   * @param  dsFlags  to set on the dir sync control
   * @param  count  max attribute count
   */
  public DirSyncClient(final ConnectionFactory cf, final DirSyncControl.Flag[] dsFlags, final int count)
  {
    factory = cf;
    dirSyncFlags = dsFlags;
    maxAttributeCount = count;
  }


  public ResultHandler[] getResultHandlers()
  {
    return resultHandlers;
  }


  public void setResultHandlers(final ResultHandler... handlers)
  {
    resultHandlers = handlers;
  }


  public ExceptionHandler getExceptionHandler()
  {
    return exceptionHandler;
  }


  public void setExceptionHandler(final ExceptionHandler handler)
  {
    exceptionHandler = handler;
  }


  public ResultPredicate getThrowCondition()
  {
    return throwCondition;
  }


  public void setThrowCondition(final ResultPredicate function)
  {
    throwCondition = function;
  }


  public LdapEntryHandler[] getEntryHandlers()
  {
    return entryHandlers;
  }


  public void setEntryHandlers(final LdapEntryHandler... handlers)
  {
    entryHandlers = handlers;
  }


  public SearchReferenceHandler[] getReferenceHandlers()
  {
    return referenceHandlers;
  }


  public void setReferenceHandlers(final SearchReferenceHandler... handlers)
  {
    referenceHandlers = handlers;
  }


  public SearchResultHandler[] getSearchResultHandlers()
  {
    return searchResultHandlers;
  }


  public void setSearchResultHandlers(final SearchResultHandler... handlers)
  {
    searchResultHandlers = handlers;
  }


  /**
   * Returns the flag that is used on the extended dn control.
   *
   * @return  extended dn control flag
   */
  public ExtendedDnControl.Flag getExtendedDnFlag()
  {
    return extendedDnFlag;
  }


  /**
   * Sets the flag to use on the extended dn control.
   *
   * @param  flag  to set on the extended dn control
   */
  public void setExtendedDnFlag(final ExtendedDnControl.Flag flag)
  {
    extendedDnFlag = flag;
  }


  /**
   * Performs a search operation with the {@link DirSyncControl}. The supplied request is modified in the following way:
   *
   * <ul>
   *   <li>{@link SearchRequest#setControls( org.ldaptive.control.RequestControl...)} is invoked with {@link
   *     DirSyncControl}, {@link ShowDeletedControl}, and {@link ExtendedDnControl}</li>
   * </ul>
   *
   * @param  request  search request to execute
   *
   * @return  search operation response
   *
   * @throws  LdapException  if the search fails
   */
  public SearchResponse execute(final SearchRequest request)
    throws LdapException
  {
    return execute(request, new DefaultCookieManager());
  }


  /**
   * Performs a search operation with the {@link DirSyncControl}. The supplied request is modified in the following way:
   *
   * <ul>
   *   <li>{@link SearchRequest#setControls( org.ldaptive.control.RequestControl...)} is invoked with {@link
   *     DirSyncControl}, {@link ShowDeletedControl}, and {@link ExtendedDnControl}</li>
   * </ul>
   *
   * <p>The cookie is extracted from the supplied response and replayed in the request.</p>
   *
   * @param  request  search request to execute
   * @param  result  of a previous dir sync operation
   *
   * @return  search operation response
   *
   * @throws  IllegalArgumentException  if the response does not contain a dir sync cookie
   * @throws  LdapException  if the search fails
   */
  public SearchResponse execute(final SearchRequest request, final SearchResponse result)
    throws LdapException
  {
    final byte[] cookie = getDirSyncCookie(result);
    if (cookie == null) {
      throw new IllegalArgumentException("Response does not contain a dir sync cookie");
    }

    return execute(request, new DefaultCookieManager(cookie));
  }


  /**
   * Performs a search operation with the {@link DirSyncControl}. The supplied request is modified in the following way:
   *
   * <ul>
   *   <li>{@link SearchRequest#setControls( org.ldaptive.control.RequestControl...)} is invoked with {@link
   *     DirSyncControl}, {@link ShowDeletedControl}, and {@link ExtendedDnControl}</li>
   * </ul>
   *
   * <p>The cookie used in the request is read from the cookie manager and written to the cookie manager after a
   * successful search, if the response contains a cookie.</p>
   *
   * @param  request  search request to execute
   * @param  manager  for reading and writing cookies
   *
   * @return  search operation response
   *
   * @throws  LdapException  if the search fails
   */
  public SearchResponse execute(final SearchRequest request, final CookieManager manager)
    throws LdapException
  {
    request.setControls(createRequestControls(manager.readCookie()));
    final SearchOperation search = createSearchOperation();
    final SearchResponse result = search.execute(request);
    final byte[] cookie = getDirSyncCookie(result);
    if (cookie != null) {
      manager.writeCookie(cookie);
    }
    return result;
  }


  /**
   * Returns whether {@link #execute(SearchRequest, SearchResponse)} can be invoked again.
   *
   * @param  result  of a previous dir sync operation
   *
   * @return  whether more dir sync results can be retrieved from the server
   */
  public boolean hasMore(final SearchResponse result)
  {
    return getDirSyncFlags(result) != 0;
  }


  /**
   * Invokes {@link #execute(SearchRequest, CookieManager)} with a {@link DefaultCookieManager}.
   *
   * @param  request  search request to execute
   *
   * @return  search operation response of the last dir sync operation
   *
   * @throws  LdapException  if the search fails
   */
  public SearchResponse executeToCompletion(final SearchRequest request)
    throws LdapException
  {
    return executeToCompletion(request, new DefaultCookieManager());
  }


  /**
   * Performs a search operation with the {@link DirSyncControl}. The supplied request is modified in the following way:
   *
   * <ul>
   *   <li>{@link SearchRequest#setControls( org.ldaptive.control.RequestControl...)} is invoked with {@link
   *     DirSyncControl}, {@link ShowDeletedControl}, and {@link ExtendedDnControl}</li>
   * </ul>
   *
   * <p>This method will continue to execute search operations until all dir sync search results have been retrieved
   * from the server. The returned response contains the response data of the last dir sync operation plus the entries
   * and references returned by all previous search operations.</p>
   *
   * <p>The cookie used for each request is read from the cookie manager and written to the cookie manager after a
   * successful search, if the response contains a cookie.</p>
   *
   * @param  request  search request to execute
   * @param  manager  for reading and writing cookies
   *
   * @return  search operation response of the last dir sync operation
   *
   * @throws  LdapException  if the search fails
   */
  public SearchResponse executeToCompletion(final SearchRequest request, final CookieManager manager)
    throws LdapException
  {
    SearchResponse response = null;
    final SearchResponse combinedResponse = new SearchResponse();
    final SearchOperation search = createSearchOperation();
    byte[] cookie = manager.readCookie();
    long flags;
    do {
      if (response != null) {
        combinedResponse.addEntries(response.getEntries());
        combinedResponse.addReferences(response.getReferences());
      }
      request.setControls(createRequestControls(cookie));
      response = search.execute(request);
      flags = getDirSyncFlags(response);
      cookie = getDirSyncCookie(response);
      if (cookie != null) {
        manager.writeCookie(cookie);
      }
    } while (flags != 0);
    response.addEntries(combinedResponse.getEntries());
    response.addReferences(combinedResponse.getReferences());
    return response;
  }


  /**
   * Creates a new search operation configured with the properties on this client.
   *
   * @return  new search operation
   */
  protected SearchOperation createSearchOperation()
  {
    final SearchOperation search = new SearchOperation(factory);
    search.setResultHandlers(resultHandlers);
    search.setExceptionHandler(exceptionHandler);
    search.setThrowCondition(throwCondition);
    search.setEntryHandlers(entryHandlers);
    search.setReferenceHandlers(referenceHandlers);
    search.setSearchResultHandlers(searchResultHandlers);
    return search;
  }


  /**
   * Returns the dir sync flags in the supplied response or -1 if no flags exists.
   *
   * @param  result  of a previous dir sync operation
   *
   * @return  dir sync flags or -1
   */
  protected long getDirSyncFlags(final SearchResponse result)
  {
    long flags = -1;
    final DirSyncControl ctl = (DirSyncControl) result.getControl(DirSyncControl.OID);
    if (ctl != null) {
      flags = ctl.getFlags();
    }
    return flags;
  }


  /**
   * Returns the dir sync cookie in the supplied response or null if no cookie exists.
   *
   * @param  result  of a previous dir sync operation
   *
   * @return  dir sync cookie or null
   */
  protected byte[] getDirSyncCookie(final SearchResponse result)
  {
    byte[] cookie = null;
    final DirSyncControl ctl = (DirSyncControl) result.getControl(DirSyncControl.OID);
    if (ctl != null && ctl.getCookie() != null && ctl.getCookie().length > 0) {
      cookie = ctl.getCookie();
    }
    return cookie;
  }


  /**
   * Returns the list of request controls configured for this client.
   *
   * @param  cookie  to add to the dir sync control or null
   *
   * @return  search request controls
   */
  private RequestControl[] createRequestControls(final byte[] cookie)
  {
    return
      new RequestControl[] {
        new DirSyncControl(dirSyncFlags, cookie, maxAttributeCount, true),
        new ExtendedDnControl(extendedDnFlag),
        new ShowDeletedControl(),
      };
  }
}
