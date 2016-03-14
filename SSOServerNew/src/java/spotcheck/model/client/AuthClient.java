
package spotcheck.model.client;

import spotcheck.model.Authentication.TokenManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;


/**
 *
 * @author wangyu
 */
public class AuthClient {
    private final static AuthClient  instance = new AuthClient();
    private boolean isLocal = false;
    private int cachesize = 5000;
    private CacheManager manager;
    private Cache tokenStore;
    private TokenManager tokenManager = TokenManager.getInstance();
    private String SSOServerURL;

    private AuthClient(){
        manager = CacheManager.create();
        Cache myCache = new Cache("clienttokenstore", cachesize, MemoryStoreEvictionPolicy.LFU,
                false, "/tmp", false, 3600, 300, false, 0, null);
        manager.addCache(myCache);
        tokenStore = manager.getCache("clienttokenstore");
    }

    public static AuthClient getInstance(){
        return instance;
    }

    public void setLocal (boolean islocal){
        isLocal = islocal;
    }

    public void setCacheSize(int mysize){
        cachesize = mysize;
    }

    public void setSSOServer(String url){
        this.SSOServerURL = url;
    }

    boolean validateToken(String value) {
        if(isLocal){
            return validateLocal(value);
        }else {
            try {
                return validateRemote(value);
            } catch (MalformedURLException ex) {
                System.out.println("MalFormartedURL error!");
                return false;
            } catch (IOException ex) {
                System.out.println("IOException!");
                return false;
            }
        }
    }

    private boolean validateLocal(String value) {
        return tokenManager.isTokenValid(value);
    }

    private boolean validateRemote(String value) throws MalformedURLException, IOException {
        Element element = tokenStore.get(value);
        if (element != null) {
            return true;
        } else {
            String ssoserverurl = this.SSOServerURL + "/SSOServlet?actiontype=tokenvailidation&token=" + value;
            URL ssoserver = new URL(ssoserverurl);
            HttpURLConnection uc = (HttpURLConnection) ssoserver.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                    uc.getInputStream()));
            String inputLine = in.readLine();
            in.close();
            uc.disconnect();
            if (inputLine == null) {
                return false;
            } else {
                if(inputLine.indexOf("success") > -1){
                    this.tokenStore.put(new Element(value,"ok"));
                    return true;
                }
                return false;
            }
        }
    }



}
