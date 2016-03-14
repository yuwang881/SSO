package spotcheck.model.TaskObject;

/**
 *
 * @author wangyu
 */
public class SpotButton {
    private String physicalID;
    private int location_org_id;
    private String spottype;
    private TaskFactory myFactory;


    SpotButton(){
        myFactory = TaskFactory.getInstance();
    }

    public int getLocation_org_id() {
        return location_org_id;
    }

    public void setLocation_org_id(int location_org_id) {
        this.location_org_id = location_org_id;
    }

    public String getPhysicalID() {
        return physicalID;
    }

    public void setPhysicalID(String physicalID) {
        this.physicalID = physicalID;
    }

    public String getSpottype() {
        return spottype;
    }

    public void setSpottype(String spottype) {
        this.spottype = spottype;
    }

    public void delete(){
        myFactory.deleteSpotButton(physicalID);
    }

    public void update(){
        myFactory.updateSpotButton(physicalID,location_org_id);
    }

    public void create(){
        if(spottype ==null) return;
        if(spottype.equals("L")){
            myFactory.createLocationSpotButton(physicalID, location_org_id);
        } else if (spottype.equals("O")){
            myFactory.createOrgSpotButton(physicalID, location_org_id);
        }
    }
    


}
