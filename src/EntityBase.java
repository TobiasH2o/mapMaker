import java.awt.image.BufferedImage;

public class EntityBase {

    private String name;
    private int ID;
    private BufferedImage icon;
    private boolean door;
    private boolean chest;
    private int matchDoorID;
    private int matchRoomID;

    public EntityBase(int ID, String name, BufferedImage icon) {
        this.ID = ID;
        this.name = name;
        this.icon = icon;
    }

    public void setDoor(int matchDoorID, int matchRoomID) {
        this.matchDoorID = matchDoorID;
        this.matchRoomID = matchRoomID;
        door = true;
    }

    public String getName(){
        return name;
    }
    public BufferedImage getImage(){
        return icon;
    }

}
