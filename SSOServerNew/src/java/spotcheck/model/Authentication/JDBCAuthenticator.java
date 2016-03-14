
package spotcheck.model.Authentication;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 *
 * @author wangyu
 */
public class JDBCAuthenticator implements Authenticator{

    private String passwdencode;
    private String dataSource;
    private DataSource ds;
    private SimpleJdbcTemplate myTemplate;
    private String execSQL = "select COUNT(*) from ";
    private String userTable;
    private String usernameCol;
    private String passwdCol;

    public void setEnv(HashMap env) {
        userTable = (String) env.get("usertable");
        usernameCol =(String) env.get("usernameCol");
        passwdCol =(String) env.get("passwdCol");
        execSQL = execSQL + userTable + " where "+ usernameCol + "=? and " + passwdCol+ "=?";
        passwdencode = (String) env.get("passwdencode");
        dataSource = (String) env.get("dataSource");
        try {
            InitialContext ic = new InitialContext();
            ds = (DataSource) ic.lookup(dataSource);
            myTemplate = new SimpleJdbcTemplate(ds);
        } catch (NamingException ex) {
            Logger.getLogger(JDBCAuthenticator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isUserValid(String username, String password) {
        if (ds == null) {
            System.out.println("Cannot get DataSource!");
            return false;
        }
        String encodepasswd = password;
        if (passwdencode.equals("MD5")) encodepasswd = DigestUtils.md5Hex(password);
        if (passwdencode.equals("SHA256")) encodepasswd = DigestUtils.sha256Hex(password);
        int count = myTemplate.queryForInt(execSQL, username,encodepasswd);
        if (count == 1) return true;
        return false;
        
    }

    public boolean isUserValid(String username, byte[] password) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
