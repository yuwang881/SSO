package spotcheck.model.AMObject;

import java.util.List;

/**
 *
 * @author wangyu
 */
public class OrgDAO implements Org{
   
    private int orgID;
    private String orgName;
    private int parentOrgID;
    private JDBCObjectFactory myFactory;

    OrgDAO(){
        myFactory = JDBCObjectFactory.getInstance();
    }

    public int getOrgID() {
        return orgID;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgID(int id) {
       orgID = id;
    }

    public void setParentID(int id) {
        parentOrgID =id;
    }

     public void setOrgName(String orgname) {
        orgName = orgname;
    }

    public boolean isTopOrg() {
        return (parentOrgID ==0 );
    }

    public boolean hasSubOrgs() {
        return myFactory.isOrgHasSubOrgs(orgID);
    }

    public List<Org> getSubOrgs() {
        return myFactory.getSubOrgs(orgID);
    }

    public Org getParentOrg() {
       return myFactory.getOrgByID(parentOrgID);
    }


    public List<User> getUsers() {
        return myFactory.getOrgUsers(orgID);
    }

    
    public List<User> findUsersbyName(String username) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

   
    public void update() {
        myFactory.updateOrg(orgID, orgName, parentOrgID);
    }

    public void delete() {
        myFactory.deleteOrg(orgID);
    }

    public void create() {
        myFactory.createOrg(orgName, parentOrgID);
        orgID = myFactory.getIDByOrgName(orgName);
    }



}
