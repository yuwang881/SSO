package spotcheck.model.TaskObject;

import java.util.List;



/**
 *
 * @author wangyu
 */
public class Location {
    private int locationID;
    private String locationName;
    private TaskFactory myFactory;


    Location(){
        myFactory = TaskFactory.getInstance();
    }


    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }


    public void update(){
        myFactory.updateLocation(locationID, locationName);
    }

    public void delete(){
        myFactory.deleteLocation(locationID);
    }

    public void create(){
        myFactory.createLocation(locationName);
        locationID = myFactory.getIDByLocationName(locationName);
    }

    public List<Equipment> getEquipments(){
        return myFactory.getEquipmentsByLocationID(locationID);
    }


}
