---
layout: default
title: Ldaptive - paged results
redirect_from: "/docs/guide/controls/pagedresults/"
---

# Paged Results

Request that the server return results in batches of a specific size. See [RFC 2696](http://www.ietf.org/rfc/rfc2696.txt). This control is often used to work around server result size limits.

## Using the Paged Results Client

The PagedResultClient encapsulates the cookie management associated with this control and exposes convenient methods for common operations. Note that you must use a SingleConnectionFactory with this client as each search request must occur on the same connection. In addition, the connection factory should not be used for other purposes while in use by the PagedResultsClient.

### Perform paged search to completion

{% highlight java %}
{% include source/controls/pagedresults/1.java %}
{% endhighlight %}

### Inspect each response myself

{% highlight java %}
{% include source/controls/pagedresults/2.java %}
{% endhighlight %}

## Using the PagedResultsControl

If you need fine grain control over this operation, this sample code illustrates how to use the PagedResultsControl directly with a SearchOperation.

{% highlight java %}
{% include source/controls/pagedresults/3.java %}
{% endhighlight %}
