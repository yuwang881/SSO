
package spotcheck.model.AMObject;

import java.util.List;


/**
 *
 * @author wangyu
 */
public class UserDAO implements User{
    private JDBCObjectFactory myFactory;
    private int uid;
    private String username;
    private String password;
    private String telephone;
    private String sex;
    private String address;
    private String birthday;
    private int orgid;
    private String chinesename;


    UserDAO(){
        myFactory = JDBCObjectFactory.getInstance();
    }

    public int getID() {
        return uid;
    }

    public int getOrgID() {
        return orgid;
    }

    public Org getOrg() {
        return myFactory.getOrgByID(orgid);
    }

    public String getName() {
        return username;
    }

    public String getAddress() {
        return address;
    }

    public String getSex() {
        return sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getPhoneNumber() {
        return telephone;
    }

    public List<Role> getRoles() {
        return myFactory.getRolesByUserID(uid);
    }

    public void addRole(Role myRole) {
        myFactory.addRoleUserPair(uid, myRole.getID());
    }

    public void removeRole(Role myRole) {
        myFactory.removeRoleUserPair(uid, myRole.getID());
    }

    public boolean isUserInRole(Role myRole) {
        return myFactory.findUserRolePair(uid, myRole.getID());
    }

    public void setPhoneNumber(String phonenumber) {
        telephone = phonenumber;
    }

    public void setBirthday(String birth) {
        birthday = birth;
    }

    public void setSex(String se) {
        sex = se;
    }

    public void setAddress(String addr) {
        address = addr;
    }

    public void setName(String name) {
        username = name;
    }

    public void setOrgID(int id) {
        orgid = id;
    }

    public void resetPassword(String currentPass, String newPass) {
        myFactory.resetPassword(uid,currentPass,newPass );
    }

    public void update() {
        myFactory.updateUser(uid, username, telephone, sex, address, birthday, orgid, chinesename);
    }


    public void delete() {
        myFactory.deleteUser(uid);
    }

    public String getChineseName() {
        return chinesename;
    }

    public void setChineseName(String name) {
        chinesename = name;
    }

    public void setID(int id) {
       uid = id;
    }

    public void create() {
        myFactory.createUser(username, password, telephone, sex, address, birthday, orgid, chinesename);
        uid = myFactory.getIDbyUserName(username);
    }

}
