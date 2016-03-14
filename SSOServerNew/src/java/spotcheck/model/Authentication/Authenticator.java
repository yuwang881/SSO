
package spotcheck.model.Authentication;

import java.util.HashMap;

/**
 *
 * @author wangyu
 */
public interface Authenticator {
    public void setEnv(HashMap env);
    public boolean isUserValid(String username, String password);
    public boolean isUserValid(String username, byte[] password);
}
