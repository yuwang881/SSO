/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package spotcheck.model.Authentication;

import java.util.HashMap;

/**
 *
 * @author wangyu
 */
public class MonkAuthenticator implements Authenticator{

    public void setEnv(HashMap env) {
       
    }

    public boolean isUserValid(String username, String password) {
        System.out.println("Beginning authenticating...");
        if (username.equals("wangyu")) return true;
        return false;
    }

    public boolean isUserValid(String username, byte[] password) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
