package spotcheck.model.TaskObject;

/**
 *
 * @author wangyu
 */
public class Equipment {
    private String SAPID;
    private String SAPName;
    private int orgid;
    private int locationid;
    private String DES;
    private TaskFactory myFactory;


    Equipment(){
        myFactory = TaskFactory.getInstance();
    }

    public String getDES() {
        return DES;
    }

    public void setDES(String DES) {
        this.DES = DES;
    }

    public String getSAPID() {
        return SAPID;
    }

    public void setSAPID(String SAPID) {
        this.SAPID = SAPID;
    }

    public String getSAPName() {
        return SAPName;
    }

    public void setSAPName(String SAPName) {
        this.SAPName = SAPName;
    }

    public int getLocationid() {
        return locationid;
    }

    public void setLocationid(int locationid) {
        this.locationid = locationid;
    }

    public int getOrgid() {
        return orgid;
    }

    public void setOrgid(int orgid) {
        this.orgid = orgid;
    }


    public void create(){
        myFactory.createEquipment(SAPID, SAPName, orgid, locationid, DES);
    }

    public void delete(){
        myFactory.deleteEquipment(SAPID);
    }

    public void update(){
        myFactory.updateEquipment(SAPID, SAPName, orgid, locationid, DES);
    }

}
