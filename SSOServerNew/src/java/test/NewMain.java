/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import spotcheck.model.Authentication.TokenManager;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wangyu
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TokenManager tm = TokenManager.getInstance();
        String tmptoken =tm.generateToken("wangyu");
        try {
            Thread.sleep(6000);
        } catch (InterruptedException ex) {
            Logger.getLogger(NewMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        boolean ii = tm.isTokenValid(tmptoken);
        if (ii)System.out.println(tm.getusernameFromToken(tmptoken));
        System.out.println(ii);
    }

}
