package spotcheck.model.client;

import java.io.IOException;
import java.net.MalformedURLException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import spotcheck.model.util.URLPattern;

/**
 *
 * @author wangyu
 */
public class SSOFilter implements Filter {

    private static final boolean debug = true;
    private String cookiename = "MySSOCookie";
    private String ServerLocation = "remote";
    private String SSOServerURL = "http://localhost:8080/SSOServer";
    private String PolicyEnable = "false";
    private String[] notEnforceURLList;
    private String loginpage;
    private String errorpage;
    private URLPattern myPattern;
    private AuthClient myAuth = AuthClient.getInstance();
    private PolicyClient myPolicy = PolicyClient.getInstance();

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;

    public SSOFilter() {
    } 

    

  
    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
	throws IOException, ServletException {

	
	boolean notEnforce =  isURLNotEnforced(request);
        if (!notEnforce){
            System.out.println("This page is protected!");
            if (!isTokenValid(request)) {
                gotoLoginPage(request, response);
                return;
            }

            if (PolicyEnable.equals("true")){
                System.out.println("Should eval policy......");
                if (!policyEval(request)) {
                    gotoErrorPage(response);
                    return;
                }
            }
        }
	
	chain.doFilter(request, response);
	
    }
    
    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
	return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
	this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter 
     */
    public void destroy() { 
    }

    /**
     * Init method for this filter 
     */
    public void init(FilterConfig filterConfig) { 
	this.filterConfig = filterConfig;
	if (filterConfig != null) {
	    String ServerLocation1 = filterConfig.getInitParameter("ServerLocation");
            if (ServerLocation1 != null) ServerLocation = ServerLocation1;
            if (ServerLocation.equals("local")){
                myAuth.setLocal(true);
                myPolicy.setLocal(true);
            }

            String cachesize1  = filterConfig.getInitParameter("cachesize");
            if (cachesize1 == null) cachesize1 ="1000";
            int cachesize = Integer.parseInt(cachesize1);
            myAuth.setCacheSize(cachesize);
            myPolicy.setCacheSize(cachesize);

            String PolicyEnable1 = filterConfig.getInitParameter("PolicyEnable");
            if (PolicyEnable1 != null) PolicyEnable = PolicyEnable1;

            String SSOServerURL1 = filterConfig.getInitParameter("SSOServerURL");
            String loginpage1 = filterConfig.getInitParameter("loginpage");
            String loginfailedpage = filterConfig.getInitParameter("loginfailedpage");
            if ( SSOServerURL1!= null) SSOServerURL = SSOServerURL1;
            myAuth.setSSOServer(SSOServerURL);
            myPolicy.setSSOServer(SSOServerURL);
            loginpage =  SSOServerURL1 + "/"+loginpage1;
            errorpage = SSOServerURL1 + "/"+loginfailedpage;


            String cookiename1 = filterConfig.getInitParameter("cookiename");
            if ( cookiename1!= null) cookiename = cookiename1;
            myPolicy.setCookiename(cookiename);

            String notenforcelistlength1 = filterConfig.getInitParameter("NotEnforceListLength");
            System.out.println("get Not Enforce List Lengh: "+ notenforcelistlength1);
            if (notenforcelistlength1 == null) notenforcelistlength1 = "0";
            int notenforcelistlength = Integer.parseInt(notenforcelistlength1);
            notEnforceURLList = new String[notenforcelistlength];

            myPattern = new URLPattern();
            for (int i = 0; i<notenforcelistlength; i++){
                notEnforceURLList[i] = filterConfig.getInitParameter("URL"+i);
                myPattern.addPatternURL(notEnforceURLList[i]);
                System.out.println("add pattern: "+notEnforceURLList[i] );
            }
	}
    }

   
    public void log(String msg) {
	filterConfig.getServletContext().log(msg); 
    }

    private boolean isURLNotEnforced(ServletRequest request) {
        String requestURI = ((HttpServletRequest)request).getRequestURI();
        return myPattern.match(requestURI);
    }



    private boolean isTokenValid(ServletRequest request) {
        Cookie[] cookies = ((HttpServletRequest)request).getCookies();
        if (cookies == null) return false;
        for (int i=0; i< cookies.length; i++) {
            if (cookies[i].getName().equals(this.cookiename)){
                String cookievalue = cookies[i].getValue();
                if (!myAuth.validateToken(cookievalue)) return false;
               ((HttpServletRequest)request).getSession().setAttribute("token", cookievalue);
                return true;
            }
        }
        return false;
    }

    private void gotoLoginPage(ServletRequest request, ServletResponse response) throws IOException {
        String loginurl = this.loginpage + "?goto=";
        StringBuffer gotoURL = ((HttpServletRequest)request).getRequestURL();
        String query = ((HttpServletRequest)request).getQueryString();
        if (query!= null) gotoURL.append("?").append(query);
        ((HttpServletResponse)response).sendRedirect(loginurl+gotoURL.toString());
    }

    private boolean policyEval(ServletRequest request) throws MalformedURLException, IOException {
        return myPolicy.URLPolicyEval((HttpServletRequest)request, ((HttpServletRequest)request).getRequestURI());
    }

    private void gotoErrorPage(ServletResponse response) throws IOException {
        ((HttpServletResponse)response).sendRedirect(errorpage);

    }

}
