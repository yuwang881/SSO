
package spotcheck.model.AMObject;

import java.util.List;

/**
 *
 * @author wangyu
 */
public class PrivilegeTypeDAO implements PrivilegeType{

    private int typeID;
    private String typeName;
    private String typeDes;
    private String resource_class;
    private String condition_class;
    private String action_class;
    private JDBCObjectFactory myFactory;

    PrivilegeTypeDAO(){
        myFactory = JDBCObjectFactory.getInstance();
    }

    public int getID() {
        return typeID;
    }

    public String getName() {
        return typeName;
    }

    public String getDes() {
        return typeDes;
    }

    public String getResourceClass() {
        return resource_class;
    }

    public String getActionClass() {
        return action_class;
    }

    public String getConditionClass() {
        return condition_class;
    }

    public void setName(String priTypeName) {
        typeName = priTypeName;
    }

    public void setDes(String priTypeDes) {
        typeDes = priTypeDes;
    }

    public void setResourceClass(String clazz) {
        resource_class = clazz;
    }

    public void setActionClass(String clazz) {
        action_class =  clazz;
    }

    public void setConditionClass(String clazz) {
        condition_class = clazz;
    }

    public void setID(int id) {
        typeID = id;
    }

    public List<Privilege> getAllPrivileges() {
        return myFactory.getPrisByTypeID(typeID);
    }

    public void update() {
        myFactory.updatePriType(typeID, typeName, typeDes, resource_class, action_class, condition_class);
    }

    public void delete() {
        myFactory.deletePriType(typeID);
    }

    public void create() {
        myFactory.createPriType(typeName, typeDes, resource_class, action_class, condition_class);
        typeID = myFactory.getIDByPriTypeName(typeName);
    }

}
