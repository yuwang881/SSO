package spotcheck.model.AMObject;

import java.util.List;

/**
 * @author Wang Yu
 *
 * Copyright(c) 2009 Sun Microsystems Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of Sun Microsystems Company
 * and Sinosure Corp.
 */
public interface User {
    public int getID();
    public Org getOrg();
    public String getName();
    public String getAddress();
    public String getSex();
    public String getBirthday();
    public String getPhoneNumber();
    public String getChineseName();
    public List<Role> getRoles();

    public void addRole(Role myRole);
    public void removeRole(Role myRole);
    public boolean isUserInRole(Role myRole);

    public void setChineseName(String name);
    public void setID(int id);
    public void setPhoneNumber(String telephone);
    public void setBirthday(String birthday);
    public void setSex(String sex);
    public void setAddress(String address);
    public void setName(String username);
    public void setOrgID(int orgid);
    public void resetPassword(String currentPass, String newPass);
    public void update();

    //remove all roles for that user
    public void delete();
    public void create();

}
