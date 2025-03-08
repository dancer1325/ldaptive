//package Home;

import org.ldaptive.SearchOperation;
import org.ldaptive.SearchResponse;
import org.ldaptive.LdapEntry;
import org.ldaptive.DefaultConnectionFactory;
import org.ldaptive.LdapException;

public class Main {
    public static void main(String[] args) {
        SearchOperation search = new SearchOperation(
                new DefaultConnectionFactory("ldap://directory.ldaptive.org"), "dc=ldaptive,dc=org");
        try {
            SearchResponse response = search.execute("(uid=dfisher)");
            LdapEntry entry = response.getEntry();
        } catch (LdapException e) {
            e.getMessage();
        }
    }
}
