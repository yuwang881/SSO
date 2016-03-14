package spotcheck.model.AMObject;

import java.util.List;

/**
 * @author Wang Yu
 *
 * Copyright(c) 2009 Sun Microsystems Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of Sun Microsystems Company
 * and Sinosure Corp.
 */
public interface Privilege {
    public int getID();
    public int getTypeID();
    public PrivilegeType getType();
    public String getName();
    public String getDes();
    public Privilege getParentPri();
    public int getParentID();
    public boolean isForlder();
    public String getResourceURI();
    public String getActionURI();
    public String getConditionURI();

    public void setName(String priname);
    public void setID(int id);
    public void setDes(String prides);
    public void setType(PrivilegeType pritype);
    public void setTypeID(int type);
    public void setParentID(int id);
    public void setForlder(boolean folder);
    public void setResourceURI(String uri);
    public void setActionURI(String uri);
    public void setConditionURI(String uri);

    public boolean isTopPri();
    public boolean hasSubPris();
    public List<Privilege> getSubPris();
    public boolean isPriInRole(Role myRole);
    public void addToRole(Role myRole);
    public void removeFromRole(Role myRole);

    public void update();
    public void delete();

    public void create();
}
