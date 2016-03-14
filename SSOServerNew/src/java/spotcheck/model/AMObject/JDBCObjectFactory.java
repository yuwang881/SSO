
package spotcheck.model.AMObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import spotcheck.model.util.Configurator;

/**
 * @author Wang Yu
 *
 * Copyright(c) 2009 Sun Microsystems Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of Sun Microsystems Company
 * and Sinosure Corp.
 */

public class JDBCObjectFactory {
    private String passwdEncode;
    private SimpleJdbcTemplate myTemplate;
    private static final JDBCObjectFactory  singleInstance= new JDBCObjectFactory();

    private JDBCObjectFactory(){
        Configurator.readProperties();
        passwdEncode = Configurator.getPasswordEncode().trim();
        String datasource = Configurator.getDataSource().trim();
        try {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup(datasource);
            myTemplate = new SimpleJdbcTemplate(ds);
        } catch (NamingException ex) {
            System.out.println("Naming Exception!");
        }

    }

    public static JDBCObjectFactory getInstance(){
        return singleInstance;
    }

    public void createTopOrg(String OrgName){
        myTemplate.update("insert into ORG values (null, ?, 0)", OrgName);
    }

    public void createOrg(String OrgName, int parentOrgID){
        myTemplate.update("insert into ORG values (null, ?, ?)", OrgName, parentOrgID);
    }

    public void createUser(String username, String passwd, String telphone, String sex, 
            String address, String birthday, int orgID, String chinesename){
        myTemplate.update("insert into User values (null,?,?,?,?,?,?,?,?)", 
              username, passwd, telphone, sex, address, birthday, orgID, chinesename );
    }

    public void createRole(String rolename){
        myTemplate.update("insert into Role values (null, ?, null)",rolename);
    }


    public Org getOrgByID(int orgid){
        List matches = myTemplate.query("select * from ORG where ORGID=?",
                new ORGRowMapper(), orgid);
        return matches.size() > 0 ? (Org) matches.get(0): null;
        
    }

    public void updateOrg(int orgid, String orgname, int parentid){
        myTemplate.update("update ORG set ORGNAME=?, PARENTID=? where ORGID=?", orgname,parentid, orgid);
    }


    public void updateRole(int roleid,String rolename, String channel_def){
        myTemplate.update("update Role set ROLENAME=?, CHANNEL=? where ROLEID=?", rolename,channel_def, roleid);
    }

    public void updateUser(int uid, String username, String telephone, String sex,
            String address, String birthday, int orgid, String chinesename){
        myTemplate.update("update User set USERNAME=?, TELEPHONE=?,SEX=?, ADDRESS=?," +
                "BIRTHDAY=?, ORGID=?, CHINESENAME=? where UID=?",
                username,telephone,sex,address,birthday, orgid, chinesename, uid);
    }

    public void deleteOrg(int orgid){
        myTemplate.update("delete from ORG where PARENTID=? or ORGID=?",orgid, orgid);
    }

    public void deleteUser(int uid){
        myTemplate.update("delete from User where UID=?",uid);
    }

    public void deletePriType(int typeid){
        myTemplate.update("delete from Pri_Type where TYPEID=?",typeid);
    }
    
    public void deletePri(int priid){
        myTemplate.update("delete from Privilege where PRIID=?",priid);
    }

    public void deleteRole(int roleid){
        myTemplate.update("delete from Role where ROLEID=?",roleid);
    }


    public void resetPassword(int uid, String oldpassword, String newpassword){
        String encodedOldpasswd = oldpassword;
        String encodedNewpasswd = newpassword;
        if (passwdEncode.equals("MD5")) {
            encodedOldpasswd = DigestUtils.md5Hex(oldpassword);
            encodedNewpasswd = DigestUtils.md5Hex(newpassword);
        } else if (passwdEncode.equals("SHA256")) {
            encodedOldpasswd = DigestUtils.sha256Hex(oldpassword);
            encodedNewpasswd = DigestUtils.sha256Hex(newpassword);
        }
        myTemplate.update("update User set PASSWORD=? where UID=?, and PASSWORD=?",encodedNewpasswd, uid, encodedOldpasswd );
    }

    public boolean isOrgHasSubOrgs(int orgid){
        return (myTemplate.queryForInt("select COUNT(*) from ORG where PARENTID = ?",orgid) > 0);
    }

    public boolean isPriHasSubPris(int priid){
        return (myTemplate.queryForInt("select COUNT(*) from Privilege where PARENTID = ?",priid) > 0);
    }

    public boolean findUserRolePair(int uid, int roleid){
        return (myTemplate.queryForInt("select COUNT(*) from User_Role where USERID=? and ROLEID=?",uid,roleid) > 0);
    }

    public boolean findPriRolePair(int priid, int roleid){
        return (myTemplate.queryForInt("select COUNT(*) from Role_Pri where PRI_ID=? and ROLE_ID=?",priid,roleid) > 0);
    }

    public List<Org> getSubOrgs(int orgid){
        List<Org> matches = myTemplate.query("select * from ORG where PARENTID=?",
                new ORGRowMapper(),orgid );
        return matches;
    }

    public List<Privilege> getSubPris(int priid){
        List<Privilege> matches = myTemplate.query("select * from Privilege where PARENTID=?",
                new PriRowMapper(),priid );
        return matches;
    }

    public List<User> getOrgUsers(int orgid) {
        List<User> matches = myTemplate.query("select * from User where ORGID=?",
                new UserRowMapper(), orgid
        );
        return matches;
    }

    public User getUserByID(int userid){
        List matches = myTemplate.query("select * from User where UID=?",
                new UserRowMapper(), userid );
        return matches.size() > 0 ? (User) matches.get(0): null;
    }

    public Role getRoleByID(int roleid){
        List matches = myTemplate.query("select * from Role where ROLEID=?",
                new RoleRowMapper(), roleid );
        return matches.size() > 0 ? (Role) matches.get(0): null;
    }

    public List<Role> getRolesByUserID(int userid){
        List<Role> matches = myTemplate.query("select * from Role INNER JOIN User_Role ON " +
                "Role.ROLEID = User_Role.ROLEID where User_Role.USERID=?",
                new RoleRowMapper(), userid );
        return matches;
    }

    public List<Role> getAllRoles(){
        List<Role> matches = myTemplate.query("select * from Role",
                new RoleRowMapper());
        return matches;
    }

    public Privilege getPriByID(int priid){
        List matches = myTemplate.query("select * from Privilege where PRIID=?",
                new PriRowMapper(), priid );
        return matches.size() > 0 ? (Privilege) matches.get(0): null;
    }

    public Privilege getPriByName(String priname){
        List matches = myTemplate.query("select * from Privilege where PRINAME=?",
                new PriRowMapper(), priname );
        return matches.size() > 0 ? (Privilege) matches.get(0): null;
    }

    public List<Privilege> getPrisByTypeID(int typeid){
        List<Privilege> matches = myTemplate.query("select * from Privilege where TYPEID=?",
                new PriRowMapper(), typeid );
        return matches;
    }

    public List<Privilege> getPrisByRoleID(int roleid){
        List<Privilege> matches = myTemplate.query("select PRIID, PRINAME, PARENTID, ISFOLDER, RESOURCEURI, ACTIONURI, CONDITIONURI, TYPEID, PRIDES from Privilege INNER JOIN Role_Pri ON " +
                "PRIID=PRI_ID where Role_Pri.ROLE_ID=?",
                new PriRowMapper(), roleid );
        return matches;
    }

    public PrivilegeType getPriTypeByID(int typeid){
        List matches = myTemplate.query("select * from Pri_Type where TYPEID=?",
                new PriTypeRowMapper(), typeid );
        return matches.size() > 0 ? (PrivilegeType) matches.get(0): null;
    }

    public List<PrivilegeType> getAllPriTypes(){
        List<PrivilegeType> matches = myTemplate.query("select * from Pri_Type ",
                new PriTypeRowMapper());
        return matches;
    }
    


    public void createPriType(String typename, String typedes, String resource_class, String action_class, String condition_class){
        myTemplate.update("insert into Pri_Type values (null, ?, ?, ?, ?, ?)", typename, typedes, resource_class,action_class,condition_class);
    }

    public void updatePriType(int typeid, String typename, String typedes, String resource_class, String action_class, String condition_class){
        myTemplate.update("update Pri_Type set TYPENAME=?, TYPE_DES=?, RESOURCE_CLASS=?, " +
                "ACTION_CLASS=?, CONDITION_CLASS=? where TYPEID=?",
                typename,typedes,resource_class,action_class,condition_class,typeid);
    }

    public void createPrivilege(String priname, int parentid, boolean isfolder, String resourceuri,
            String actionuri,String conditionuri, int typeid, String prides){
        myTemplate.update("insert into Privilege values (null,?,?,?,?,?,?,?,?)",priname,parentid,
               isfolder, resourceuri, actionuri, conditionuri, typeid, prides );
    }

    public void updatePri(int priid, String priname, int parentid, boolean isfolder, String resourceuri,
            String actionuri,String conditionuri, int typeid, String prides){
        myTemplate.update("update Privilege set PRINAME=?,PARENTID=?,ISDOLDER=?,RESOURCEURI=?," +
                "ACTIONURI=?,CONDITIONURI=?,TYPEID=?,PRIDES=? where PRIID=?",priname,parentid,isfolder,
                resourceuri,actionuri,conditionuri,typeid,prides,priid);
    }

    public void createTopPrivilege(String priname,String prides){
         createPrivilege(priname,0,true,null,null,null,1,prides);
    }

    public void createFolderPrivilege(String priname,int parentid,String prides){
        createPrivilege(priname,parentid,true,null,null,null,0,prides);
    }


    public void addRoleUserPair(int uid, int roleid){
         myTemplate.update("insert into User_Role values (?,?)",roleid,uid);
    }

    public void addRolePriPair(int priid, int roleid){
         myTemplate.update("insert into Role_Pri values (?,?)",roleid,priid);
    }

    public void removeRoleUserPair(int uid, int roleid){
         myTemplate.update("delete from User_Role where ROLEID=? and USERID=?",roleid,uid);
    }

    public void removeRolePriPair(int priid, int roleid){
         myTemplate.update("delete from Role_Pri where ROLE_ID=? and PRI_ID=?",roleid,priid);
    }

    public User getUserByName(String username) {
        List matches = myTemplate.query("select * from User where USERNAME=?",
                new UserRowMapper(), username );
        return matches.size() > 0 ? (User) matches.get(0): null;
    }

    public int getIDByOrgName(String orgName) {
        return  myTemplate.queryForInt("select ORGID from ORG where ORGNAME = ?", orgName);
    }

    public int getIDbyUserName(String username) {
        return  myTemplate.queryForInt("select UID from User where USERNAME = ?", username);
    }

    int getIDByRoleName(String rolename) {
        return myTemplate.queryForInt("select ROLEID from Role where ROLENAME = ?", rolename);
    }

    int getIDByPriName(String priName) {
        return myTemplate.queryForInt("select PRIID from Privilege where PRINAME = ?", priName);
    }

    int getIDByPriTypeName(String typeName) {
        return myTemplate.queryForInt("select TYPEID from Pri_Type where TYPENAME = ?", typeName);
    }

    public List<User> findUsers(HashMap condition) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<Org> findOrgs(HashMap condition) {
        throw new UnsupportedOperationException("Not yet implemented");
    }



    private class ORGRowMapper implements ParameterizedRowMapper {

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
             OrgDAO org = new OrgDAO();
             org.setOrgID(rs.getInt(1));
             org.setOrgName(rs.getString(2));
             org.setParentID(rs.getInt(3));
             return org;
        }
    }


    private class UserRowMapper implements ParameterizedRowMapper {

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserDAO user = new UserDAO();
            user.setID(rs.getInt(1));
            user.setName(rs.getString(2));
            user.setPhoneNumber(rs.getString(4));
            user.setSex(rs.getString(5));
            user.setAddress(rs.getString(6));
            user.setBirthday(rs.getString(7));
            user.setOrgID(rs.getInt(8));
            user.setChineseName(rs.getString(9));
            return user;

        }
    }



    private class PriTypeRowMapper implements ParameterizedRowMapper {

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            PrivilegeTypeDAO pritype = new PrivilegeTypeDAO();
            pritype.setID(rs.getInt(1));
            pritype.setName(rs.getString(2));
            pritype.setDes(rs.getString(3));
            pritype.setResourceClass(rs.getString(4));
            pritype.setActionClass(rs.getString(5));
            pritype.setConditionClass(rs.getString(6));
            return pritype;
        }
    }


    private class PriRowMapper implements ParameterizedRowMapper {

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            PrivilegeDAO pri = new PrivilegeDAO();
            pri.setID(rs.getInt(1));
            pri.setName(rs.getString(2));
            pri.setParentID(rs.getInt(3));
            pri.setForlder(rs.getBoolean(4));
            pri.setResourceURI(rs.getString(5));
            pri.setActionURI(rs.getString(6));
            pri.setConditionURI(rs.getString(7));
            pri.setTypeID(rs.getInt(8));
            pri.setDes(rs.getString(9));
            return pri;
        }
    }


    private class RoleRowMapper implements ParameterizedRowMapper {

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            RoleDAO role = new RoleDAO();
            role.setID(rs.getInt(1));
            role.setName(rs.getString(2));
            role.setChannelDef(rs.getString(3));
            return role;
        }
    }

}
