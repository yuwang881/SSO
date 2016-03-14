package spotcheck.model.Policy;

/**
 *
 * @author wangyu
 */
public interface ActionEvaluator {
    boolean evaluate(String definedURI, String passedURI);
}
