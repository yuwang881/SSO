
package spotcheck.model.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wangyu
 */
public class URLPattern {
    private List patterns = new ArrayList();

    public void addPatternURL(String pattern){
        patterns.add(pattern);
    }

    public boolean match(String input){
        for (int i =0; i< patterns.size(); i++){
            String p = (String) patterns.get(i);
            boolean isFiletype  = p.startsWith("*");
            boolean isPathtype = p.endsWith("*");
            if(isFiletype) {
                String trim = p.substring(1);
                if (input.endsWith(trim)) return true;
            } else if (isPathtype){
                String trim = p.substring(0,p.length()-1);
                if (input.indexOf(trim)> -1) return true;
            } else {
                if (input.indexOf(p)>-1) return true;
            }
        }
        
        return false;
    }

    public static boolean match(String pattern, String input) {
        boolean isFiletype = pattern.startsWith("*");
        boolean isPathtype = pattern.endsWith("*");
        if (isFiletype) {
            String trim = pattern.substring(1);
            if (input.endsWith(trim)) {
                return true;
            }
        } else if (isPathtype) {
            String trim = pattern.substring(0, pattern.length() - 1);
            if (input.indexOf(trim) > -1) {
                return true;
            }
        } else {
            if (input.indexOf(pattern) > -1) {
                return true;
            }
        }

        return false;
    }
}
