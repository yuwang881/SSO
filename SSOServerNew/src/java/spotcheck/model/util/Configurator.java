package spotcheck.model.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 *
 * @author wangyu
 */
public class Configurator {
    
    static Properties properties;
    static boolean isReady = false;



    static public synchronized void readProperties(){
        if(isReady) return;
        properties = new Properties();
        try {
            InputStream in = Configurator.class.getClassLoader().getResourceAsStream("MySSO.properties");
            if (in == null){
                System.out.println("Cannot find the properties file!"); 
            } 
            properties.load(in);
           
            isReady = true;
        } catch (IOException e) {
            System.out.println("Erro when reading properties file!");
            e.printStackTrace();
        }
    }
    static public String getTokenSize(){
        return properties.getProperty("tokensize", "128");
    }

    static public String getMaxSessions(){
        return properties.getProperty("maxSessions", "5000");
    }

    static public String getSessionTimeToLive(){
        return properties.getProperty("sessionTimeToLiveSeconds", "3600");
    }

    static public String getSessionTimeToIdle(){
        return properties.getProperty("sessionTimeToIdleSeconds", "300");
    }

    static public String getDataSource(){
        return properties.getProperty("dataSource", "");
    }


    static public String getDomainName(){
        return properties.getProperty("domainname", ".sun.com");
    }

    static public String getLdapServer(){
        return properties.getProperty("ldapserver", "localhost:389");
    }

    static public String getOrgOfUser(){
        return properties.getProperty("orgOfUser", "");
    }

    static public String getUserStore(){
        return properties.getProperty("userStore", "database");
    }

    static public String getPasswordEncode(){
        return properties.getProperty("passwordEncode", "clear");
    }

    static public String getCookieName(){
        return properties.getProperty("cookiename", "MySSOCookie");
    }

    static public String getUserTableName(){
        return properties.getProperty("usertable", "User");
    }

    static public String getUserColName(){
        return properties.getProperty("usernamecolname", "username");
    }

    static public String getPasswdColName(){
        return properties.getProperty("passwdcolname", "password");
    }

    static public String getLoginPage(){
        return properties.getProperty("loginpage", "login.jsp");
    }

    static public String getLoginFailedPage(){
        return properties.getProperty("loginfailedpage", "error.jsp");
    }

    static public String getIndexPage(){
        return properties.getProperty("indexpage", "index.jsp");
    }

}
