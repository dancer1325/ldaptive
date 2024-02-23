---
layout: default
title: Ldaptive - properties
redirect_from: "/docs/guide/properties/"
---

# Properties

In order to support various configuration formats Ldaptive provides the ability to configure its objects with Properties. Each property identifies a method which is reflectively invoked with the value.

{% highlight text %}
org.ldaptive.ldapUrl=ldap://directory.ldaptive.org
org.ldaptive.baseDn=dc=ldaptive,dc=org
org.ldaptive.connectTimeout=3000
org.ldaptive.useStartTLS=true
{% endhighlight %}

{% highlight java %}
{% include source/properties/1.java %}
{% endhighlight %}

