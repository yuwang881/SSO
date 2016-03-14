package spotcheck.model.Authentication;

import java.util.HashMap;
import spotcheck.model.util.Configurator;

/**
 *
 * @author wangyu
 */
public class AuthenticatorManager {
    private String userStore;
    private String dataSource;
    private String ldapserver;
    private String orgOfUser;
    private String passwordEncode;
    private String usertable;
    private String usernameCol;
    private String passwdCol;

    public void init(){
        Configurator.readProperties();
        userStore= Configurator.getUserStore().trim();
        dataSource = Configurator.getDataSource().trim();
        ldapserver = Configurator.getLdapServer().trim();
        orgOfUser = Configurator.getOrgOfUser().trim();
        passwordEncode = Configurator.getPasswordEncode().trim();
        usertable= Configurator.getUserTableName().trim();
        usernameCol= Configurator.getUserColName().trim();
        passwdCol=  Configurator.getPasswdColName().trim();
    }

    public Authenticator getAuthenticator(){
        HashMap env = new HashMap();
        Authenticator myAuth = null;
        if (userStore.equals("database")){
            env.put("passwdencode", passwordEncode);
            env.put("usertable", usertable);
            env.put("usernameCol", usernameCol);
            env.put("passwdCol",passwdCol );
            env.put("dataSource", dataSource);
            myAuth = new JDBCAuthenticator();
            System.out.println("System is using JDBC DataSource");
        }else if (userStore.equals("ldap")){
            env.put("passwdencode", passwordEncode);
            env.put("LDAPServer", ldapserver);
            env.put("orgOfUser", orgOfUser);
            myAuth = new LDAPAuthenticator();
            System.out.println("System is using LDAP dataSource");
        }else if (userStore.equals("monk")){
            myAuth = new MonkAuthenticator();
            System.out.println("System is using Monk users!");
        }
        myAuth.setEnv(env);
        return myAuth;
    }

}
