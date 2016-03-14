
package spotcheck.model.Policy;

import spotcheck.model.util.URLPattern;

/**
 *
 * @author wangyu
 */
public class URLResourceEvaluator implements ResourceEvaluator{

    public boolean evaluate(String definedURI, String passedURI) {
        return URLPattern.match(definedURI,passedURI);
    }

}
