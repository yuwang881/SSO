package spotcheck.model.AMObject;

import java.util.List;

/**
 * @author Wang Yu
 *
 * Copyright(c) 2009 Sun Microsystems Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of Sun Microsystems Company
 * and Sinosure Corp.
 */
public interface Role {
    public int getID();
    public String getName();
    public String getChannelDef();
    public void setID(int id);
    public void setName(String name);
    public void setChannelDef(String channelDef);

    public void addUser(User myUser);
    public void removeUser(User myUser);
    public List<User> getUsers();
    public boolean isUserInRole(User myUser);
    public List<Privilege> getPrivileges();
    public void addPrivilege(Privilege myPri);
    public void removePrivilege(Privilege myPri);
    public boolean isPriInRole(Privilege myPri);
    
    public void update();

    //remove all users in that role
    public void delete();
    public void create();
}
