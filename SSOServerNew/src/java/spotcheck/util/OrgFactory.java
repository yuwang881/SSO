package spotcheck.util;

import java.util.HashMap;
import java.util.List;
import spotcheck.model.AMObject.JDBCObjectFactory;
import spotcheck.model.AMObject.Org;


/**
 * @author Wang Yu
 *
 * Copyright(c) 2009 Sun Microsystems Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of Sun Microsystems Company
 * and Sinosure Corp.
 */

public class OrgFactory {
    private JDBCObjectFactory myFactory;
    private static final OrgFactory instance = new OrgFactory();

    public static OrgFactory getInstance(){
        return instance;
    }

    private OrgFactory() {
        myFactory = JDBCObjectFactory.getInstance();
    }

    public void create(Org org){
        org.create();
    }

    public void delete(Org org){
        org.delete();
    }

    public void update(Org org){
        org.update();
    }

    public List<Org> find(HashMap condition) {
        return myFactory.findOrgs(condition);
    }

    public void delete(List<Org> orgs){
        for (int i=0;i< orgs.size();i++){
            orgs.get(i).delete();
        }
    }

    public void create(List<Org> orgs){
        for (int i=0;i< orgs.size();i++){
            orgs.get(i).create();
        }
    }

}
