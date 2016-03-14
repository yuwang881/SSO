
package spotcheck.model.client;

import spotcheck.model.Authentication.TokenManager;
import spotcheck.model.Policy.PolicyEvaluator;
import spotcheck.model.Policy.PolicyEvaluatorManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

/**
 *
 * @author wangyu
 */
public class PolicyClient {
    private final static PolicyClient  instance = new PolicyClient();
    private boolean isLocal = true;
    private int cachesize = 5000;
    private CacheManager manager;
    private Cache tokenStore;
    private TokenManager tokenManager = TokenManager.getInstance();
    private String SSOServerURL;
    private String cookiename="MySSOCookie";
    private PolicyEvaluator pe = PolicyEvaluatorManager.getPolicyEvaluator();

    private PolicyClient(){
        manager = CacheManager.create();
        Cache myCache = new Cache("clientpolicystore", cachesize, MemoryStoreEvictionPolicy.LFU,
                false, "/tmp", false, 3600, 300, false, 0, null);
        manager.addCache(myCache);
        tokenStore = manager.getCache("clientpolicystore");

    }

    public static PolicyClient getInstance(){
        return instance;
    }

    public void setCookiename(String name){
        this.cookiename = name;
    }

    public void setLocal (boolean islocal){
        isLocal = islocal;
    }

    public void setSSOServer(String URL) {
        this.SSOServerURL  = URL;
    }

    public void setCacheSize(int size) {
        this.cachesize = size;
    }

    public boolean URLPolicyEval(HttpServletRequest request, String resourceURI) throws MalformedURLException, IOException{
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return false;
        for (int i=0; i< cookies.length; i++) {
            if (cookies[i].getName().equals(this.cookiename)){
                return URLPolicyEval(cookies[i].getValue(),resourceURI);
            }
        }
        return false;

    }

    public boolean URLPolicyEval(String token, String resourceURI) throws MalformedURLException, IOException{
        if (cachehit(token,resourceURI)) {
            return true;
        }

        boolean hasPri = false;
        if (isLocal){
            hasPri = URLPolicyEvalLocal(token, resourceURI);
        }else {
            hasPri = URLPolicyEvalRemote(token, resourceURI);
        }

        if (hasPri){
            addToCache(token,resourceURI);
            return true;
        }else {
            return false;
        }
    }

    public boolean simplePolicyEval(HttpServletRequest request, String priname) throws MalformedURLException, IOException{
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return false;
        for (int i=0; i< cookies.length; i++) {
            if (cookies[i].getName().equals(this.cookiename)){
                return simplePolicyEval(cookies[i].getValue(),priname);
            }
        }
        return false;
    }

    public boolean simplePolicyEval(String token, String priname) throws MalformedURLException, IOException{

        if (cachehit(token,priname)) return true;
        boolean hasPri = false;
        if (isLocal){
            hasPri = simplePolicyEvalLocal(token, priname);
        }else {
            hasPri = simplePolicyEvalRemote(token, priname);
        }

        if (hasPri){
            addToCache(token,priname);
            return true;
        }else {
            return false;
        }

    }

    private boolean simplePolicyEvalLocal(String token, String priname) {
        return pe.evaluatePribyToken(token, priname);
    }

    private boolean simplePolicyEvalRemote(String token, String priname) throws MalformedURLException, IOException {
            String ssoserverurl = this.SSOServerURL + "/SSOServlet?actiontype=policyEval&token=" + token +"&simplepriname=" + priname;
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
                if(inputLine.indexOf("success") > -1) return true;
            }
            return false;
    }

    private boolean cachehit(String token, String priname) {

        Element element = tokenStore.get(token);
        if (element == null) {
            return false;
        }
        ConcurrentHashMap match = (ConcurrentHashMap) element.getObjectValue();
        if (match == null) {
            System.out.println("Error inside cache structure!");
            return false;
        }

        if (match.containsKey(priname)) {
            return true;
        }
        return false;

    }

    private void addToCache(String token, String priname) {
        Element element = tokenStore.get(token);
        if (element == null){
            ConcurrentHashMap ch = new ConcurrentHashMap();
            ch.put(priname, "ok");
            Element e = new Element(token, ch);
            tokenStore.put(e);
        }else {
            ConcurrentHashMap match = (ConcurrentHashMap) element.getObjectValue();
            match.put(priname,"ok");
        }

    }

    private boolean URLPolicyEvalLocal(String token, String resourceURI) {
        return pe.evaluatePriTypebyToken(token, "URLPolicy", resourceURI);
    }

    private boolean URLPolicyEvalRemote(String token, String resourceURI) throws MalformedURLException, IOException {
        String ssoserverurl = this.SSOServerURL + "/SSOServlet?actiontype=policyEval&token=" + token +"&pritype=URLPolicy&resourceURI="+resourceURI ;
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
                if(inputLine.indexOf("success") > -1) return true;
            }
            return false;
    }

}
