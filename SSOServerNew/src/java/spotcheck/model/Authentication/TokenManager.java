
package spotcheck.model.Authentication;

import java.security.SecureRandom;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;
import org.apache.commons.codec.binary.Base64;
import spotcheck.model.util.Configurator;

/**
 *
 * @author wangyu
 */
public class TokenManager {
    private final static TokenManager tokenManager = new TokenManager();
    private int tokensize;
    private int maxSessions;
    private int sessionTimetoIdle;
    private int sessionTimetoLive;
    private CacheManager manager;
    private Cache tokenStore;

    private TokenManager(){
        init();
  
    }

    public static TokenManager getInstance(){
        return tokenManager;
    }

    private void init() {
        readConfig();
        initCache();
    }

    private void readConfig() {
        Configurator.readProperties();
        tokensize = Integer.parseInt(Configurator.getTokenSize().trim());
        maxSessions = Integer.parseInt(Configurator.getMaxSessions().trim());
        sessionTimetoIdle = Integer.parseInt(Configurator.getSessionTimeToIdle().trim());
        sessionTimetoLive = Integer.parseInt(Configurator.getSessionTimeToLive().trim());
    }

    private void initCache() {

        manager = CacheManager.create();
        Cache myCache = new Cache("tokenstore", maxSessions, MemoryStoreEvictionPolicy.LFU,
                false, "/tmp", false, sessionTimetoLive, sessionTimetoIdle, false, 0, null);
        manager.addCache(myCache);
        tokenStore = manager.getCache("tokenstore");
        
    }

    public String generateToken(String username){
        String newtoken = generateToken(tokensize);
        addValidToken(newtoken,username);
        return newtoken;
    }

    public String getusernameFromToken(String token){
        Element element = tokenStore.get(token);
        if (element != null) return element.getObjectValue().toString();
        return null;
    }

    public boolean isTokenValid(String token){
        Element element = tokenStore.get(token);
        if (element != null) return true;
        return false;
    }


    public void printCacheHitInfo(){

    }

    //to do: add user information for authorization
    public void addValidToken(String token, String username){
        tokenStore.put(new Element(token,username));

    }

    public void removeValidToken(String token){
        tokenStore.remove(token);
    }

    private String generateToken(int tokensize)
	{
	int    cb  = (tokensize + 3) / 4 * 3;     // base 64: 3 bytes = 4 chars
	byte[] ab  = new byte[cb];
        SecureRandom random = new SecureRandom();
	random.nextBytes(ab);               
        return Base64.encodeBase64URLSafeString(ab);
	}

}
