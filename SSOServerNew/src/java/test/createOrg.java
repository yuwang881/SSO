/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import spotcheck.model.AMObject.JDBCObjectFactory;
import spotcheck.model.AMObject.Privilege;
import spotcheck.model.AMObject.Role;
import spotcheck.model.AMObject.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author wangyu
 */
public class createOrg extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        JDBCObjectFactory objectF = JDBCObjectFactory.getInstance();
//       objectF.createTopOrg("SUN");
//        objectF.createOrg("GPE", 1);
//        objectF.createUser("syy", "wangyu", "13701319042", "M", "haidian", "1972-05-13", 1, "sdf");
//        objectF.createPriType("folder", "container", null, null, null);
//        objectF.createPriType("simple", "test for exist", null, null, null);
//        objectF.createPriType("URLPolicy", "for eval URL resource", "spotcheck.model.Policy.URLResourceEvaluator", null, null);
//        objectF.createTopPrivilege("ERI", "Top ORG for Test");
//        objectF.createPrivilege("simple1", 1, false, null, null, null, 2, null);
//        objectF.createPrivilege("Security", 1, false, "/security/*", null, null, 3, null);
//        objectF.createRole("simpleRole");
//        objectF.createRole("SecurityRole");
//        Privilege simple = objectF.getPriByID(2);
//        Privilege urlsecurity = objectF.getPriByID(3);
//        Role simplerole = objectF.getRoleByID(1);
        Role urlrole = objectF.getRoleByID(2);
//        User wangyu = objectF.getUserByID(1);
        User syy = objectF.getUserByID(2);
//        simplerole.addPrivilege(simple);
//        urlrole.addPrivilege(urlsecurity);
//        wangyu.addRole(urlrole);
        syy.addRole(urlrole);



        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
           
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet createOrg</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet createOrg at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
           
        } finally { 
            out.close();
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

}
