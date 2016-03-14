package spotcheck.model.util;

import spotcheck.model.AMObject.User;
import java.util.Hashtable;
import javax.naming.AuthenticationException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class LDAPHelper
{
    public static final String LDAP_CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
    public static final String DEFAULT_LDAP_SERVER = "ldap1.singapore.sun.com:389";
    public static final String DEFAULT_LDAP_SEARCH_BASE = "ou=people,dc=sun,dc=com";
    public static final String AUTHENTICATION_BASE = "ou=people,o=WebAuth";
    private String ldapServer;
    private String ldapSearchBase;
    private Hashtable env;

    public LDAPHelper()
    {
        ldapServer = null;
        ldapSearchBase = null;
        ldapServer = "sun-ds.east.sun.com:389";
        ldapSearchBase = "ou=people,dc=sun,dc=com";
        env = new Hashtable();
        env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
        env.put("java.naming.provider.url", "ldap://" + ldapServer);
    }

    public void setLDAPServer(String s)
    {
        ldapServer = s;
        env = new Hashtable();
        env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
        env.put("java.naming.provider.url", "ldap://" + ldapServer);
    }

    public String getLDAPServer()
    {
        return ldapServer;
    }

    public void setLDAPSearchBase(String s)
    {
        ldapSearchBase = s;
    }

    public String getLDAPSearchBase()
    {
        return ldapSearchBase;
    }

    public User authenticate(String uid, String passwd)
    {
        User ldapUser = null;
        DirContext ctx = null;
        Hashtable env = null;
        if(passwd == null || passwd.trim().length() == 0)
            return null;
        env = new Hashtable();
        env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
        env.put("java.naming.provider.url", "ldap://" + ldapServer);
        env.put("java.naming.security.authentication", "simple");
        env.put("java.naming.security.principal", "uid=" + uid + "," + "ou=people,o=WebAuth");
        env.put("java.naming.security.credentials", passwd);
        try
        {
            ctx = new InitialDirContext(env);
        }
        catch(AuthenticationException e)
        {
            e.printStackTrace();
			System.out.println(e.getMessage());
            return null;
        }
        catch(NamingException e)
        {
			e.printStackTrace();
			System.out.println(e.getMessage());
            return null;
        }
        try
        {
            ctx.close();
        }
        catch(Exception e)
        {
			e.printStackTrace();
			System.out.println(e.getMessage());
            return null;
        }

        ldapUser = searchByEmployeeNumber(uid);
//        if(ldapUser != null)
            //ldapUser.setPassword(passwd);
        return ldapUser;
    }

    public User searchByUID(String user)
    {
        return search("uid=" + user);
    }

    public User searchByEmployeeNumber(String user)
    {
        return search("employeenumber=" + user);
    }

    public User SEARCH(String searchString)
    {
        return search(searchString);
    }

    private User search(String searchString)
    {
        User ldapUser = null;
        try
        {
            SearchControls control = new SearchControls();
            control.setSearchScope(2);
            DirContext ctx = new InitialDirContext(env);
            NamingEnumeration results = ctx.search(ldapSearchBase, searchString, control);
            if(results != null && results.hasMore())
            {
                SearchResult sr = (SearchResult)results.next();
                Attributes attr = sr.getAttributes();
//                ldapUser = new LDAPUser();
//                ldapUser.setUID((String)attr.get("uid").get());
//                ldapUser.setMailAddress((String)attr.get("mail").get());
//                ldapUser.setMailHost((String)attr.get("mailhost").get());
//                ldapUser.setCommonName((String)attr.get("cn").get());
//                ldapUser.setCountryCode((String)attr.get("countrycode").get());
//                ldapUser.setTelephoneNumber((String)attr.get("telephonenumber").get());
//                ldapUser.setEmployeeNumber((String)attr.get("employeenumber").get());
//                ctx.close();
            }
        }
        catch(Exception e)
        {
            System.out.println("LDAPHelper.search: " + e.getMessage());
        }
        return ldapUser;
    }

    public static void main(String args[])
    {
        LDAPHelper ldapHelper = new LDAPHelper();
        System.out.println(ldapHelper.SEARCH(args[0]));
    }
}
