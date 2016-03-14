package spotcheck.model.AMObject;

import java.util.List;

/**
 *
 * @author wangyu
 */
public class PrivilegeDAO implements Privilege{
    private int priID;
    private String priName;
    private int parentPriID;
    private boolean isForder;
    private String resource_uri;
    private String action_uri;
    private String condition_uri;
    private int typeid;
    private String priDes;
    private JDBCObjectFactory myFactory;

    PrivilegeDAO(){
        myFactory = JDBCObjectFactory.getInstance();
    }

    public int getID() {
        return priID;
    }

    public PrivilegeType getType() {
        return myFactory.getPriTypeByID(typeid);
    }

    public String getName() {
        return priName;
    }

    public void setName(String priname) {
        priName = priname;
    }

    public String getDes() {
        return priDes;
    }

    public void setDes(String prides) {
        priDes = prides;
    }

    public void setType(PrivilegeType pritype) {
        this.typeid = pritype.getID();
    }

    public boolean isTopPri() {
        return (parentPriID == 0);
    }

    public boolean hasSubPris() {
        return myFactory.isPriHasSubPris(priID);
    }

    public List<Privilege> getSubPris() {
        return myFactory.getSubPris(priID);
    }

    public Privilege getParentPri() {
        return myFactory.getPriByID(parentPriID);
    }

    public boolean isPriInRole(Role myRole) {
        return myFactory.findPriRolePair(priID, myRole.getID());
    }

    public void addToRole(Role myRole) {
        myFactory.addRolePriPair(priID, myRole.getID());
    }

    public void removeFromRole(Role myRole) {
        myFactory.removeRolePriPair(priID, myRole.getID());
    }

    public void update() {
        myFactory.updatePri(priID, priName, parentPriID, isForder, resource_uri, action_uri, condition_uri, typeid, priDes);
    }

    public void delete() {
        myFactory.deletePri(priID);
    }

    public int getTypeID() {
        return typeid;
    }

    public int getParentID() {
        return parentPriID;
    }

    public boolean isForlder() {
        return isForder;
    }

    public String getResourceURI() {
        return this.resource_uri;
    }

    public String getActionURI() {
        return this.action_uri;
    }

    public String getConditionURI() {
        return this.condition_uri;
    }

    public void setID(int id) {
        this.priID = id;
    }

    public void setTypeID(int type) {
        this.typeid = type;
    }

    public void setParentID(int id) {
        this.parentPriID = id;
    }

    public void setForlder(boolean folder) {
        this.isForder = folder;
    }

    public void setResourceURI(String uri) {
        this.resource_uri = uri;
    }

    public void setActionURI(String uri) {
        this.action_uri = uri;
    }

    public void setConditionURI(String uri) {
        this.condition_uri = uri;
    }

    public void create() {
        myFactory.createPrivilege(priName, parentPriID, isForder, resource_uri, action_uri, condition_uri, typeid, priDes);
        priID = myFactory.getIDByPriName(priName);
    }

}
