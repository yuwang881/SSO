
package spotcheck.model.Policy;

import spotcheck.model.AMObject.JDBCObjectFactory;
import spotcheck.model.AMObject.Privilege;
import spotcheck.model.AMObject.PrivilegeType;
import spotcheck.model.AMObject.Role;
import spotcheck.model.AMObject.User;
import spotcheck.model.Authentication.TokenManager;
import java.util.List;


/**
 *
 * @author wangyu
 */
public class PolicyJDBCEvaluator implements PolicyEvaluator{

    JDBCObjectFactory myFactory;
    TokenManager myTokenmanager;

    PolicyJDBCEvaluator(){
        myFactory = JDBCObjectFactory.getInstance();
        myTokenmanager = TokenManager.getInstance();
    }

    
    public boolean evaluatePriType(String username, String priTypename, String resourceURI) {
        return evaluatePriType(username, priTypename, null, null, resourceURI);
    }

    public boolean evaluatePriType(String username, String priTypename, String actionURI, String resourceURI) {
        return evaluatePriType(username, priTypename, null, actionURI, resourceURI);
    }

    public boolean evaluatePriType(String username, String priTypename, String conditionURI, String actionURI, String resourceURI) {

        User myuser = myFactory.getUserByName(username);
        List roles = myFactory.getRolesByUserID(myuser.getID());
        if (roles == null) return false;
        for (int i = 0; i< roles.size(); i++){
            List pris = ((Role)roles.get(i)).getPrivileges();
            if (pris == null) continue;
            for (int j = 0; j< pris.size(); j++){
                Privilege mypri = (Privilege)pris.get(j);
                if(mypri.getType().getName().equals(priTypename)){
                    if (evaluate(mypri.getName(), conditionURI, actionURI, resourceURI)) return true;
                }
            }
        }
        return false;
}

    public boolean evaluatePri(String username, String priName) {
        User myuser = myFactory.getUserByName(username);
        List roles = myFactory.getRolesByUserID(myuser.getID());
        if (roles == null) return false;
        for (int i = 0; i< roles.size(); i++){
            List pris = ((Role)roles.get(i)).getPrivileges();
            if (pris == null) continue;
            for (int j = 0; j< pris.size(); j++){
                String privilegeName = ((Privilege)pris.get(j)).getName();
                if (privilegeName.equals(priName)) return true;
            }
        }
        return false;
    }

    public boolean evaluatePri(String username, String priName, String resourceURI) {
        if (!this.evaluatePri(username, priName)) return false;
        return this.evaluate(priName, null, null, resourceURI);
    }

    public boolean evaluatePri(String username, String priName, String actionURI, String resourceURI) {
        if (!this.evaluatePri(username, priName)) return false;
        return this.evaluate(priName, null, actionURI, resourceURI);
    }

    public boolean evaluatePri(String username, String priName, String conditionURI, String actionURI, String resourceURI) {
        if (!this.evaluatePri(username, priName)) return false;
        return this.evaluate(priName, conditionURI, actionURI, resourceURI);
    }

    private boolean evaluate(String priName, String passedconditionURI, String passedactionURI, String passedresourceURI) {
        
        Privilege myPri = myFactory.getPriByName(priName);
        PrivilegeType priType = myPri.getType();

        //evaluate the action policy
        String definedactionURI = myPri.getConditionURI();
        String actionClass = priType.getActionClass();
        if (passedconditionURI!= null && actionClass != null && definedactionURI != null) {
            try {
                ActionEvaluator ae = (ActionEvaluator) PolicyJDBCEvaluator.class.getClassLoader().loadClass(actionClass).newInstance();
                if (!ae.evaluate(definedactionURI,passedactionURI)) {
                    return false;
                }
            } catch (ClassNotFoundException ex) {
                System.out.println("ClassNotFound for: " + actionClass);
            } catch (Exception ex1) {
                System.out.println("Some exception " + actionClass);
            }
        }


        //evaluate resouce policy
        String resourceClass = priType.getResourceClass();
        String definedresourceURI = myPri.getResourceURI();
        if (passedresourceURI != null && resourceClass != null && definedresourceURI != null) {
            try {
                ResourceEvaluator re = (ResourceEvaluator) PolicyJDBCEvaluator.class.getClassLoader().loadClass(resourceClass).newInstance();
                if (!re.evaluate(definedresourceURI,passedresourceURI)) {
                    return false;
                }
            } catch (ClassNotFoundException ex) {
                System.out.println("ClassNotFound for: " + resourceClass);
            } catch (Exception ex1) {
                System.out.println("Some exception " + resourceClass);
            }
        }

        //evaluate condition policy
        String definedconditionURI = myPri.getConditionURI();
        String conditionClass = priType.getConditionClass();
        if (passedconditionURI != null && conditionClass != null && definedconditionURI != null) {
            try {
                ConditionEvaluator ce = (ConditionEvaluator) PolicyJDBCEvaluator.class.getClassLoader().loadClass(conditionClass).newInstance();
                if (!ce.evaluate(definedconditionURI,passedconditionURI)) {
                    return false;
                }
            } catch (ClassNotFoundException ex) {
                System.out.println("ClassNotFound for: " + conditionClass);
            } catch (Exception ex1) {
                System.out.println("Some exception " + conditionClass);
            }
        }
       
        return true;

    }

    public boolean evaluatePriTypebyToken(String token, String priTypename, String resourceURI) {
        String username = myTokenmanager.getusernameFromToken(token);
        if (username != null){
            return evaluatePriType(username, priTypename, resourceURI);
        }
        return false;
    }

    public boolean evaluatePriTypebyToken(String token, String priTypename, String actionURI, String resourceURI) {
        String username = myTokenmanager.getusernameFromToken(token);

        if (username != null){
            return evaluatePriType(username, priTypename,actionURI, resourceURI);
        }
        return false;
    }

    public boolean evaluatePriTypebyToken(String token, String priTypename, String conditionURI, String actionURI, String resourceURI) {
        String username = myTokenmanager.getusernameFromToken(token);

        if (username != null){
            return evaluatePriType(username, priTypename,conditionURI, actionURI, resourceURI);
        }
        return false;
    }

    public boolean evaluatePribyToken(String token, String priName) {

        String username = myTokenmanager.getusernameFromToken(token);
        if (username != null){
            return evaluatePri(username, priName);
        }
        return false;
    }

    public boolean evaluatePribyToken(String token, String priName, String resourceURI) {

         String username = myTokenmanager.getusernameFromToken(token);
        if (username != null){
            return evaluatePri(username, priName,resourceURI);
        }
        return false;
    }

    public boolean evaluatePribyToken(String token, String priName, String actionURI, String resourceURI) {
       String username = myTokenmanager.getusernameFromToken(token);
        if (username != null){
            return evaluatePri(username, priName,actionURI, resourceURI);
        }
        return false;
    }

    public boolean evaluatePribyToken(String token, String priName, String conditionURI, String actionURI, String resourceURI) {
        String username = myTokenmanager.getusernameFromToken(token);
        if (username != null){
            return evaluatePri(username, priName,conditionURI, actionURI, resourceURI);
        }
        return false;
    }



}
