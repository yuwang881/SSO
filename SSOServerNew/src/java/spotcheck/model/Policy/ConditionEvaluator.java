package spotcheck.model.Policy;

/**
 *
 * @author wangyu
 */
public interface ConditionEvaluator {
    boolean evaluate(String definedURI, String passedURI);
}
