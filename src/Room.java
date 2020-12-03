public class Room {

    private int roomWidth = 0;
    private int roomHeight = 0;

    private boolean[][] wallMap;

    private String[][] terrainMap;

    private String roomName = "";

    private int roomID;

    public Room(int roomID, String roomName) {
        this.roomName = roomName;
        this.roomID = roomID;
    }

    public void setRoomSize(int width, int height) {
        roomWidth = width;
        roomHeight = height;

        wallMap = new boolean[width][height];
        terrainMap = new String[width][height];
    }

    public int getRoomHeight() {
        return roomHeight;
    }

    public int getRoomWidth() {
        return roomWidth;
    }

    public int getID() {
        return roomID;
    }

    public String getName() {
        return roomName;
    }

}
