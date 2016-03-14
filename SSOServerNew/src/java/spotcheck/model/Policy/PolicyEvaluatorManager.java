

package spotcheck.model.Policy;

import spotcheck.model.util.Configurator;

/**
 *
 * @author wangyu
 */
public class PolicyEvaluatorManager {
    private static PolicyEvaluator pe;

    public static synchronized PolicyEvaluator getPolicyEvaluator(){
        if (pe == null) {
            Configurator.readProperties();
            String userStore= Configurator.getUserStore().trim();
            if (userStore.equals("database")) pe = new PolicyJDBCEvaluator();
        }
        
        return pe;
    }
}
