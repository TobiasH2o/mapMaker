public class Room {

    private int roomWidth = 0;
    private int roomHeight = 0;

    private boolean[][] wallMap;

    private Tile[][] terrainMap;

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
        terrainMap = new Tile[width][height];

        for (boolean[] a : wallMap)
            for (boolean b : a)
                b = false;
        for (int i = 0;i < terrainMap.length;i++) {
            for (int j = 0;j < terrainMap[i].length;j++) {
                terrainMap[i][j] = new Tile("", null);
            }
        }
    }

    public Tile[][] getTerrainMap() { return terrainMap;}

    public boolean[][] getWallMap() {
        return wallMap;
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
