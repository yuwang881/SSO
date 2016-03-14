
package spotcheck.model.SSOServer;

import spotcheck.model.Authentication.Authenticator;
import spotcheck.model.Authentication.AuthenticatorManager;
import spotcheck.model.Authentication.TokenManager;
import spotcheck.model.Policy.PolicyEvaluator;
import spotcheck.model.Policy.PolicyEvaluatorManager;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import spotcheck.model.util.Configurator;

/**
 *
 * @author wangyu
 */
public class SSOServlet extends HttpServlet {

    private TokenManager tokenManager;
    private Authenticator myAuthenticator;
    private PolicyEvaluator pe;
    private String cookiename;
    private String domainname;
    private String indexpage;
    private String loginpage;
    private String loginfailedpage;
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        String actionType = request.getParameter("actiontype");
        if (actionType == null || actionType.equals("")) {
            return;
        } else if(actionType.equals("login")){  
                generateToken(request, response);
        } else if(actionType.equals("tokenvailidation")){
                tokenValidate(request,response);
        } else if(actionType.equals("logout")){
                logout(request,response);
        } else if(actionType.equals("policyEval")){
                policyEval(request,response);
        }


    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    @Override
    public void init() throws ServletException {
   
            Configurator.readProperties();
            cookiename =  Configurator.getCookieName();
            domainname =  Configurator.getDomainName();
            tokenManager = TokenManager.getInstance();
            indexpage = Configurator.getIndexPage();
            loginpage = Configurator.getLoginPage();
            loginfailedpage = Configurator.getLoginFailedPage();
            AuthenticatorManager authManager = new AuthenticatorManager();
            authManager.init();
            myAuthenticator = authManager.getAuthenticator();
            pe = PolicyEvaluatorManager.getPolicyEvaluator();

       
    }

    public boolean validateToken(String token){
        return tokenManager.isTokenValid(token);
    }


    public boolean Authenticate(String username, String password){
        if (myAuthenticator == null) {
            System.out.println("Authenticator is null!");
            return false;
        }
        return myAuthenticator.isUserValid(username, password);
    }

    private void generateToken(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String gotoURL = request.getParameter("goto");
        if (!Authenticate(username, password)) {
            response.sendRedirect(loginfailedpage);
        } else {
            if (gotoURL == null || gotoURL.equals("")) {
                gotoURL = indexpage;
            }
            Cookie cookie = new Cookie(cookiename, tokenManager.generateToken(username));
            cookie.setDomain(domainname);
            cookie.setMaxAge(-1);
            cookie.setPath("/");
            response.addCookie(cookie);
            response.sendRedirect(gotoURL);
        }
    }

    private void tokenValidate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String result = "failed";
        String token = request.getParameter("token");
        if (token != null) {
            if (validateToken(token)) {
                result = "success";
            }
        }

        try {
            out.println(result);
        } finally {
            out.close();
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String token = request.getParameter("token");
        if (token != null) {
            tokenManager.removeValidToken(cookiename);
        }

        try {
            out.println("ok");
        } finally {
            out.close();
        }
    }

    private void policyEval(HttpServletRequest request, HttpServletResponse response) throws IOException{
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String result = "failed";
        String token = request.getParameter("token");
        if (token != null) {
            String priname = request.getParameter("priname");
            String simplepriname = request.getParameter("simplepriname");
            String pritype = request.getParameter("pritype");
            if (priname != null) {
                String conditionURI = request.getParameter("conditionURI");
                String actionURI =request.getParameter("actionURI");
                String resourceURI = request.getParameter("resourceURI");
                if (pe.evaluatePribyToken(token, priname,conditionURI,actionURI,resourceURI)) {
                    result = "success";
                }
            } else if(pritype != null){
                String conditionURI = request.getParameter("conditionURI");
                String actionURI =request.getParameter("actionURI");
                String resourceURI = request.getParameter("resourceURI");
                if (pe.evaluatePriTypebyToken(token, pritype,conditionURI,actionURI,resourceURI)) {
                    result = "success";
                }
            } else if(simplepriname != null){

                if (pe.evaluatePribyToken(token, simplepriname)) {
                    result = "success";
                }
            }
        }

        try {
            out.println(result);
        } finally {
            out.close();
        }

    }
 

}
