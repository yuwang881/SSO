
package spotcheck.model.AMObject;

import java.util.List;

/**
 *
 * @author wangyu
 */
public class RoleDAO implements Role{
    private JDBCObjectFactory myFactory;
    private int roleid;
    private String rolename;
    private String channel_def;


    RoleDAO(){
        myFactory = JDBCObjectFactory.getInstance();
    }

    public int getID() {
        return roleid;
    }

    public String getName() {
        return rolename;
    }

    public String getChannelDef() {
        return channel_def;
    }

    public void setID(int id) {
        roleid = id;
    }

    public void setName(String name) {
        rolename = name;
    }

    public void setChannelDef(String channelDef) {
        channel_def = channelDef;
    }
    

    public void addUser(User myUser) {
        myFactory.addRoleUserPair(myUser.getID(), roleid);
    }

    public void removeUser(User myUser) {
        myFactory.removeRoleUserPair(myUser.getID(), roleid);
    }

    public List<User> getUsers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isUserInRole(User myUser) {
        return myFactory.findUserRolePair(myUser.getID(), roleid);
    }

    public List<Privilege> getPrivileges() {
        return myFactory.getPrisByRoleID(roleid);
    }

    public void addPrivilege(Privilege myPri) {
        myFactory.addRolePriPair(myPri.getID(), roleid);
    }

    public void removePrivilege(Privilege myPri) {
        myFactory.removeRolePriPair(myPri.getID(), roleid);
    }

    public boolean isPriInRole(Privilege myPri) {
        return myFactory.findPriRolePair(myPri.getID(), roleid);
    }

    public void update() {
        myFactory.updateRole(roleid, rolename, channel_def);
    }

    public void delete() {
        myFactory.deleteRole(roleid);
    }

    public void create() {
        myFactory.createRole(rolename);
        roleid = myFactory.getIDByRoleName(rolename);
    }

}
