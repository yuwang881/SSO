package spotcheck.model.TaskObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import spotcheck.model.util.Configurator;

/**
 *
 * @author wangyu
 */
public class TaskFactory {

    private SimpleJdbcTemplate myTemplate;
    private static final TaskFactory  singleInstance= new TaskFactory();

    private TaskFactory(){
        Configurator.readProperties();
        String datasource = Configurator.getDataSource().trim();
        try {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup(datasource);
            myTemplate = new SimpleJdbcTemplate(ds);
        } catch (NamingException ex) {
            System.out.println("Naming Exception!");
        }
    }


    public static TaskFactory getInstance(){
        return singleInstance;
    }

    public void createLocation(String locationName){
        myTemplate.update("insert into Location values (null, ?)", locationName);
    }

    public void createEquipment(String SAPID, String SAPName, int orgid, int locationid,String des){
        myTemplate.update("insert into Equipment values (?,?,?,?,?)",SAPID,SAPName,orgid,locationid,des);
    }

    public void createOrgSpotButton(String pid, int orgid){
        myTemplate.update("insert into SpotButton values (?,?,'O')",pid,orgid);
    }

    public void createLocationSpotButton(String pid, int locationid){
        myTemplate.update("insert into SpotButton values (?,?,'L')",pid,locationid);
    }
    
    public void batchCreateLocationSpotButton(List<Object[]> args){
        myTemplate.batchUpdate("insert into SpotButton values (?,?,'L')",args);
    }

    public void createTaskTemplate(String taskname, int orgid, int startlocation, int deviation, String month, String workday, String hour, String minutes,String start_time){
        myTemplate.update("insert into TaskTemplate values (null,?,?,?,0,null,?,?,?,?,?,?)",taskname,orgid,startlocation,deviation,month,workday,hour, minutes, start_time);
    }

    public void updateTaskTemplate(int taskid,String taskname, int orgid, int startlocation, int deviation, String cront_month, String cront_workday, String cront_hour, String cront_min,String start_time){
        myTemplate.update("update TaskTemplate set TASKNAME=?,ORGID=?,STARTLOCATIONID=?,DEVIATION=?,CRONT_MONTHDAY=?,CRONT_WORKDAY=?,CRONT_HOUR=?,CRONT_MIN=?, START_TIME=? where TASKID=?",taskname,orgid,startlocation,deviation,cront_month,cront_workday,cront_hour, cront_min, start_time,taskid);
    }

    public void updateLocation(int locationid, String locationname){
        myTemplate.update("update Location set LOCATIONNAME = ? where LOCATIONID = ?",locationname,locationid);
    }

    public void updateEquipment(String SAPID, String SAPName, int orgid, int locationid, String des){
        myTemplate.update("update Equipment set SAPNAME=?,ORGID=?,LOCATIONID=?,DES=? where SAPID=?",SAPName,orgid,locationid,des,SAPID);
    }

    public void deleteLocation(int locationid){
        myTemplate.update("delete from Location where LOCATIONID = ?",locationid);
    }

    public void deleteTask(int taskid){
        myTemplate.update("delete from TaskTemplate where TASKID=?",taskid);
    }
    public void createTask_Location(){
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int getIDByLocationName(String locationName) {
        return myTemplate.queryForInt("select LOCATIONID from Location where LOCATIONNAME = ?", locationName);
    }

    public List<Equipment> getEquipmentsByLocationID(int locationID) {
        List<Equipment> matches = myTemplate.query("select * from Equipment where LOCATIONID = ?",
                new EquipmentRowMapper(),locationID);
        return matches;
    }

    public List<Equipment> searchEquipmentsByName(String likename){
        List<Equipment> matches = myTemplate.query("select * from Equipment where SAPNAME like '%"+likename+"%'",
                new EquipmentRowMapper());
        return matches;
    }

    public List<Location> searchLocationsByName(String likename) {
        List<Location> matches = myTemplate.query("select * from Location where LOCATIONNAME like '%"+likename+"%'",
                new LocationRowMapper());
        return matches;
    }

    public List<Location> getLastLocations(int number){
        List<Location> matches = myTemplate.query("select * from Location ORDER by LOCATIONID DESC LIMIT "+ number,
                new LocationRowMapper());
        return matches;
    }

    public List<SpotButton> searchSpotButtonByID(String likeID){
        List<SpotButton> matches = myTemplate.query("select * from SpotButton where PHYSICALID like '%"+likeID+"%'",
                new SpotButtonRowMapper());
        return matches;
    }


    public void deleteEquipment(String SAPID) {
        myTemplate.update("delete from Equipment where SAPID = ?", SAPID);
    }

    public void deleteSpotButton(String physicalID) {
        myTemplate.update("delete from SpotButton where PHYSICALID =?",physicalID);
    }

    public void updateSpotButton(String physicalID, int location_org_id) {
        myTemplate.update("update SpotButton set LOCATIONORGID=? where PHYSICALID=?",location_org_id,physicalID );
    }

    public void addTaskLocation(int taskID, int locationid) {
        myTemplate.update("insert into Task_Location values(?,?,0)",taskID,locationid);
    }

    public void removeTaskLocation(int taskID, int locationid) {
        myTemplate.update("delete from Task_Location where TASKID=? and LOCATIONID=?",taskID,locationid);
    }

    public List<Location> getTaskLocations(int taskID) {
        List<Location> matches = myTemplate.query("select * from Location INNER JOIN Task_Location ON " +
                "Location.LOCATIONID = Task_Location.LOCATIONID where TASKID=?",
                new LocationRowMapper(), taskID );
        return matches;
    }

    public List<Task> getTasksforOrg(int orgid){
        List<Task> matches = myTemplate.query("select * from TaskTemplate where ORGID=?",
                new TaskRowMapper(), orgid );
        return matches;
    }

    private class EquipmentRowMapper implements ParameterizedRowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            Equipment eq = new Equipment();
            eq.setSAPID(rs.getString(1));
            eq.setSAPName(rs.getString(2));
            eq.setOrgid(rs.getInt(3));
            eq.setLocationid(rs.getInt(4));
            eq.setDES(rs.getString(5));
            return eq;
        }
    }


    private class LocationRowMapper implements ParameterizedRowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            Location lo = new Location();
            lo.setLocationID(rs.getInt(1));
            lo.setLocationName(rs.getString(2));
            return lo;
        }
    }


    private class SpotButtonRowMapper implements ParameterizedRowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            SpotButton sb = new SpotButton();
            sb.setPhysicalID(rs.getString(1));
            sb.setLocation_org_id(rs.getInt(2));
            sb.setSpottype(rs.getString(3));
            return sb;
        }
    }

    private class TaskRowMapper implements ParameterizedRowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            Task  task = new Task();
            task.setTaskID(rs.getInt(1));
            task.setTaskName(rs.getString(2));
            task.setOrgid(rs.getInt(3));
            task.setStartlocationid(rs.getInt(4));
            task.setStatus(rs.getInt(5));
            task.setLastinst_time(rs.getString(6));
            task.setDeviation(rs.getInt(7));
            task.setCront_monthday(rs.getString(8));
            task.setCront_workday(rs.getString(9));
            task.setCront_hour(rs.getString(10));
            task.setCront_min(rs.getString(11));
            task.setStart_time(rs.getString(12));
            return task;
        }
    }

}
