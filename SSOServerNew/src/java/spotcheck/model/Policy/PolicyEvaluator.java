package spotcheck.model.Policy;


/**
 *
 * @author wangyu
 */
public interface PolicyEvaluator {
    public boolean evaluatePriType(String username, String priTypename, String resourceURI);
    public boolean evaluatePriType(String username, String priTypename, String actionURI,String resourceURI);
    public boolean evaluatePriType(String username, String priTypename, String conditionURI, String actionURI, String resourceURI);

    public boolean evaluatePri(String username, String priName);
    public boolean evaluatePri(String username, String priName, String resourceURI);
    public boolean evaluatePri(String username, String priName, String actionURI,String resourceURI);
    public boolean evaluatePri(String username, String priName, String conditionURI, String actionURI, String resourceURI);


    public boolean evaluatePriTypebyToken(String token, String priTypename, String resourceURI);
    public boolean evaluatePriTypebyToken(String token, String priTypename, String actionURI,String resourceURI);
    public boolean evaluatePriTypebyToken(String token, String priTypename, String conditionURI, String actionURI, String resourceURI);

    public boolean evaluatePribyToken(String token, String priName);
    public boolean evaluatePribyToken(String token, String priName, String resourceURI);
    public boolean evaluatePribyToken(String token, String priName, String actionURI,String resourceURI);
    public boolean evaluatePribyToken(String token, String priName, String conditionURI, String actionURI, String resourceURI);

}
