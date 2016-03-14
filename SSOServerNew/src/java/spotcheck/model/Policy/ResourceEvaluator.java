package spotcheck.model.Policy;

/**
 *
 * @author wangyu
 */
public interface ResourceEvaluator {
    boolean evaluate(String definedURI, String passedURI);
}
