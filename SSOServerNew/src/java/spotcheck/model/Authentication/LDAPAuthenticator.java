package spotcheck.model.Authentication;

import java.util.HashMap;
import java.util.Hashtable;
import javax.naming.AuthenticationException;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

/**
 *
 * @author wangyu
 */
public class LDAPAuthenticator implements Authenticator{
    private String LDAPServer;
    private String orgOfUser;
    private boolean isReady = false;


    public boolean isUserValid(String username, String password) {
        if (isReady) {
            DirContext ctx = null;
            Hashtable env = new Hashtable();
            env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
            env.put("java.naming.provider.url", "ldap://" + LDAPServer);
            env.put("java.naming.security.authentication", "simple");
            env.put("java.naming.security.principal", "uid=" + username + "," + "ou=people,"+orgOfUser);
            env.put("java.naming.security.credentials", password);
            try {
                ctx = new InitialDirContext(env);
            } catch (AuthenticationException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                return false;
            } catch (NamingException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                return false;
            }
            try {
                ctx.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean isUserValid(String username, byte[] password) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setEnv(HashMap env) {
        LDAPServer = (String) env.get("LDAPServer");
        orgOfUser = (String) env.get("orgOfUser");
        if (LDAPServer == null || orgOfUser ==null){
            isReady = false;
        } else {
            isReady = true;
        }
    }

}
