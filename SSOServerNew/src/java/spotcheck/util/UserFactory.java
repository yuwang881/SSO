
package spotcheck.util;

import java.util.HashMap;
import java.util.List;
import spotcheck.model.AMObject.JDBCObjectFactory;
import spotcheck.model.AMObject.User;

/**
 * @author Wang Yu
 *
 * Copyright(c) 2009 Sun Microsystems Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of Sun Microsystems Company
 * and Sinosure Corp.
 */

public class UserFactory {
    private JDBCObjectFactory myFactory;
    private static final UserFactory instance = new UserFactory();

    public static UserFactory getInstance(){
        return instance;
    }

    private UserFactory() {
        myFactory = JDBCObjectFactory.getInstance();
    }

    public void create(User user){
        user.create();
    }

    public void delete(User user){
        user.delete();
    }

    public void update(User user){
        user.update();
    }

    public List<User> find(HashMap condition) {
        return myFactory.findUsers(condition);
    }

    public void delete(List<User> users){
        for (int i=0;i< users.size();i++){
            users.get(i).delete();
        }
    }

    public void create(List<User> users){
        for (int i=0;i< users.size();i++){
            users.get(i).create();
        }
    }

}
