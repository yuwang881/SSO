/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import spotcheck.model.util.URLPattern;

/**
 *
 * @author wangyu
 */
public class NewMain1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        URLPattern up = new URLPattern();
        up.addPatternURL("*.jsp");
        up.addPatternURL("/security/*");

        System.out.println(up.match("http://fsdf/sdfsd/asd.jsp"));
        System.out.println(up.match("/asdas/asda/asdas/asd.jspp"));
        System.out.println(up.match("http://sdad.sd.sd/security/sdfs/sdfs"));
    }

}
