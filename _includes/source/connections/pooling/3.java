PooledConnectionFactory cf = PooledConnectionFactory.builder()
  .config(ConnectionConfig.builder()
    .url("ldap://directory.ldaptive.org")
    .build())
  .validatePeriodically(true)
  .validator(new SearchConnectionValidator(SearchRequest.builder()
    .dn("uid=dfisher,ou=people,dc=vt,dc=edu")
    .filter("(uid=dfisher)")
    .returnAttributes(ReturnAttributes.NONE.value())
    .scope(SearchScope.OBJECT)
    .sizeLimit(1)
    .build()))
  .build();
cf.initialize();
try {
  SearchOperation search = new SearchOperation(cf, "dc=ldaptive,dc=org");
  SearchResponse response = search.execute("(uid=dfisher)");
  LdapEntry entry = response.getEntry();
} finally {
  // close the pool
  cf.close();
}
