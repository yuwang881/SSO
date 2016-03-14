package spotcheck.model.AMObject;

import java.util.List;

/**
 * @author Wang Yu
 *
 * Copyright(c) 2009 Sun Microsystems Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of Sun Microsystems Company
 * and Sinosure Corp.
 */
public interface Org {

    public void setOrgID(int id);
    public int getOrgID();
    public String getOrgName();
    public boolean isTopOrg();
    public boolean hasSubOrgs();
    public List<Org> getSubOrgs();
    public Org getParentOrg();
    public void setParentID(int id);
    public List<User> getUsers();
    public List<User> findUsersbyName(String username);
    public void setOrgName(String orgname);
    public void update();

    //delete all sub-orgs and itself
    public void delete();
    public void create();

}
