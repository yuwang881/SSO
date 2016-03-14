package spotcheck.model.AMObject;

import java.util.List;

/**
 * @author Wang Yu
 *
 * Copyright(c) 2009 Sun Microsystems Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of Sun Microsystems Company
 * and Sinosure Corp.
 */
public interface PrivilegeType {
    public int getID();
    public String getName();
    public String getDes();
    public String getResourceClass();
    public String getActionClass();
    public String getConditionClass();
    void setID(int id);
    public void setName(String priTypeName);
    public void setDes(String priTypeDes);
    public void setResourceClass(String clazz);
    public void setActionClass(String clazz);
    public void setConditionClass(String clazz);

    public List<Privilege> getAllPrivileges();
    public void update();

    //delete all privilege belong to this Type
    public void delete();
    public void create();
}