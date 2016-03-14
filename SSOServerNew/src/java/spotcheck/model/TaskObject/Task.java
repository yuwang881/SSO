
package spotcheck.model.TaskObject;

import java.util.List;

/**
 *
 * @author wangyu
 */
public class Task {
    private int taskID;
    private String taskName;
    private int orgid;
    private int startlocationid;
    private int status;
    private String lastinst_time;
    private int deviation;
    private String cront_monthday;
    private String cront_workday;
    private String cront_hour;
    private String cront_min;
    private String start_time;
    private TaskFactory myFactory;


    Task(){
        myFactory = TaskFactory.getInstance();
    }

    public String getCront_hour() {
        return cront_hour;
    }

    public void setCront_hour(String cront_hour) {
        this.cront_hour = cront_hour;
    }

    public String getCront_min() {
        return cront_min;
    }

    public void setCront_min(String cront_min) {
        this.cront_min = cront_min;
    }

    public String getCront_monthday() {
        return cront_monthday;
    }

    public void setCront_monthday(String cront_monthday) {
        this.cront_monthday = cront_monthday;
    }

    public String getCront_workday() {
        return cront_workday;
    }

    public void setCront_workday(String cront_workday) {
        this.cront_workday = cront_workday;
    }

    public int getDeviation() {
        return deviation;
    }

    public void setDeviation(int deviation) {
        this.deviation = deviation;
    }

    public String getLastinst_time() {
        return lastinst_time;
    }

    public void setLastinst_time(String lastinst_time) {
        this.lastinst_time = lastinst_time;
    }

    public int getOrgid() {
        return orgid;
    }

    public void setOrgid(int orgid) {
        this.orgid = orgid;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public int getStartlocationid() {
        return startlocationid;
    }

    public void setStartlocationid(int startlocationid) {
        this.startlocationid = startlocationid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }


    public void addLocation(int locationid){
        myFactory.addTaskLocation(taskID,locationid);
    }

    public void addLocation(Location lo){
        addLocation(lo.getLocationID());
    }

    public void removeLocation(int locationid){
        myFactory.removeTaskLocation(taskID,locationid);
    }

    public void removeLocation(Location lo){
        removeLocation(lo.getLocationID());
    }

    public List<Location> getAllLocations(){
        return myFactory.getTaskLocations(taskID);
    }

    public void create(){
        myFactory.createTaskTemplate(taskName, orgid, startlocationid, deviation, cront_monthday, cront_workday, cront_hour, cront_min, start_time);
    }

    public void update(){
        myFactory.updateTaskTemplate(taskID, taskName, orgid, startlocationid, deviation, cront_monthday, cront_workday, cront_hour, cront_min, start_time);
    }

    public void delete(){
        myFactory.deleteTask(taskID);
    }


}
