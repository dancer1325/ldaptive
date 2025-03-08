package com;

import javax.net.ssl.SSLException;
import org.ldaptive.SearchOperation;
import org.ldaptive.SearchResponse;
import org.ldaptive.LdapEntry;
import org.ldaptive.DefaultConnectionFactory;
import org.ldaptive.LdapException;
import org.ldaptive.ConnectionConfig;

public class App 
{
    public static void main( String[] args )
    {
        homeSearching();
        homeStartTLS();
        // TODO:
    }

    private static void homeStartTLS() {
        System.out.println("<-- homeStartTLS");
        try {
            SearchOperation search = new SearchOperation(
                    DefaultConnectionFactory.builder()
                            .config(ConnectionConfig.builder()
                                    .url("ldap://localhost:389")
                                    .useStartTLS(true)
                                    .build())
                            .build(),
                    "dc=ldaptive,dc=org");
            SearchResponse response = search.execute("(uid=*fisher)", "mail", "sn");    // TODO: Fix javax.net.ssl.SSLHandshakeException: Trust check failed for chain
            for (LdapEntry entry : response.getEntries()) {
                System.out.println("entry " + entry);
            }
        } catch (LdapException e) {
            e.printStackTrace();
        } finally {
            System.out.println("homeStartTLS -->");
        }
    }

    private static void homeSearching() {
        System.out.println("<-- homeSearching ");
        try {
            SearchOperation search = new SearchOperation(
                    new DefaultConnectionFactory("ldap://localhost:389"), "dc=ldaptive,dc=org");
            SearchResponse response = search.execute("(uid=dfisher)");
            LdapEntry entry = response.getEntry();
            System.out.println("entry " + entry);       // TODO: Why is it null?
        } catch (LdapException e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("homeSearching -->");
        }
    }
}
