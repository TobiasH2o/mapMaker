import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Objects;

public class MainPanel extends JPanel implements KeyListener, ActionListener, MouseListener {

    JFrame parent;
    Rooms roomPanel = new Rooms(this);
    Entities entities = new Entities();
    Timer movement = new Timer(10, this);
    Timer click = new Timer(10, this);
    Room currentRoom = new Room(0, "");
    Tile currentTile = new Tile("", null);
    int cameraX = 0;
    int deltaX = 0;
    int cameraY = 0;
    int deltaY = 0;
    Tiles tilePanel = new Tiles();
    JComboBox<? extends String> placementTypes = new JComboBox<>(new String[]{"Walls", "Terrain", "Entities", "Player" +
                                                                                                              " View"});
    boolean change;
    JButton saveMapButton = new JButton("Save Map");
    JButton loadMapButton = new JButton("Load Map");
    Container southContainer = new Container();

    public MainPanel(JFrame parent) {
        this.parent = parent;

        roomPanel.setLocation(parent.getLocationOnScreen().x - roomPanel.frame.getWidth() - 2,
                parent.getLocationOnScreen().y);
        roomPanel.setFocusable(false);
        roomPanel.setVisible(true);

        entities.setLocation(parent.getLocationOnScreen().x - entities.frame.getWidth() - 2,
                parent.getLocationOnScreen().y + 2 + roomPanel.frame.getHeight());
        entities.setFocusable(false);
        entities.setVisible(true);

        tilePanel.setLocation(parent.getLocationOnScreen().x + parent.getWidth() + 2, parent.getLocationOnScreen().y);
        tilePanel.setFocusable(false);
        tilePanel.setVisible(true);

        movement.setActionCommand("move");
        click.setActionCommand("click");
        saveMapButton.setActionCommand("save");
        loadMapButton.setActionCommand("load");

        setLayout(new BorderLayout());

        parent.setFocusable(true);
        parent.addKeyListener(this);
        parent.addMouseListener(this);

        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);

        saveMapButton.setFocusable(false);
        saveMapButton.addActionListener(this);

        placementTypes.setFocusable(false);
        placementTypes.addActionListener(this);

        loadMapButton.setFocusable(false);
        loadMapButton.addActionListener(this);

        southContainer.setLayout(new BoxLayout(southContainer, BoxLayout.X_AXIS));

        southContainer.add(loadMapButton);
        southContainer.add(saveMapButton);
        southContainer.add(placementTypes);

        add(southContainer, BorderLayout.SOUTH);

    }

    public void saveMapData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showSaveDialog(parent);
        File saveLocation = fileChooser.getSelectedFile();
        if (!saveLocation.getAbsolutePath().endsWith(".mmk"))
            saveLocation = new File(saveLocation.getAbsolutePath() + ".mmk");
        if (saveLocation.exists()) if (!saveLocation.delete()) JOptionPane.showMessageDialog(parent,
                "Failed to overwrite save file. Please " + "make sure that the file is not open in " +
                "any other application.", "FAIL", JOptionPane.ERROR_MESSAGE);
        FileManager.checkFile(saveLocation);
        for (int i = 0;i < roomPanel.getRoomCount();i++) {
            FileManager.saveRoom(saveLocation, roomPanel.getRoom(i));
        }
    }

    public void loadMapData() {
        roomPanel.clearRooms();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showOpenDialog(parent);
        File loadLocation = fileChooser.getSelectedFile();
        String[] fileData = FileManager.loadMapData(loadLocation);
        Room room = null;
        boolean[][] wallMap;
        Tile[][] terrainMap;
        Log.logLine(fileData);
        for (int i = 0;i < fileData.length;i += 3) {
            room = new Room(Integer.parseInt(fileData[i].split(FileManager.break1)[0]),
                    fileData[i].split(FileManager.break1)[1]);
            room.setRoomSize(Integer.parseInt(fileData[i].split(FileManager.break1)[2]),
                    Integer.parseInt(fileData[i].split(FileManager.break1)[3]));
            wallMap = new boolean[room.getRoomWidth()][room.getRoomHeight()];
            for (int l = 0;l < room.getRoomWidth() * room.getRoomHeight();l++) {
                wallMap[l % room.getRoomWidth()][l / room.getRoomWidth()] =
                        Boolean.parseBoolean(fileData[i + 1].split(FileManager.break1)[l]);
            }
            terrainMap = new Tile[room.getRoomWidth()][room.getRoomHeight()];
            for (int l = 0;l < room.getRoomWidth() * room.getRoomHeight();l++) {
                String tileName = fileData[i + 2].split(FileManager.break1)[l];
                if(tilePanel.tileExists(tileName) && !tileName.equals("NULL")){
                    terrainMap[l % room.getRoomWidth()][l/room.getRoomWidth()] = tilePanel.getTile(tileName);
                }else{
                    terrainMap[l % room.getRoomWidth()][l/room.getRoomWidth()] = new Tile("", null);;
                }
            }


            room.setWallMap(wallMap);
            room.setTerrainMap(terrainMap);
        }
        roomPanel.addRoom(room);

    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);

        if (isValid(currentRoom)) {
            Tile[][] tileMap = currentRoom.getTerrainMap();
            boolean[][] wallMap = currentRoom.getWallMap();
            for (int i = 0;i < currentRoom.getRoomWidth();i++) {
                for (int k = 0;k < currentRoom.getRoomHeight();k++) {
                    if (tileMap[i][k] != null)
                        g2.drawImage(tileMap[i][k].getImage(), (i * 32) + cameraX, (k * 32) + cameraY, null);
                    if (Objects.equals(placementTypes.getSelectedItem(), "Walls")) {
                        if (wallMap[i][k]) g2.setColor(new Color(134, 255, 128, 190));
                        else g2.setColor(new Color(255, 128, 128, 190));
                        g2.fillRect((i * 32) + cameraX, (k * 32) + cameraY, 32, 32);
                    }
                }
            }
        }
        if(!Objects.equals(placementTypes.getSelectedItem(), "Player View")) {
            g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
            for (int i = 0;i < currentRoom.getRoomWidth() + 1;i++) {
                g2.drawLine((i * 32) + cameraX, cameraY, (i * 32) + cameraX,
                        cameraY + (currentRoom.getRoomHeight() * 32));
            }
            for (int i = 0;i < currentRoom.getRoomHeight() + 1;i++) {
                g2.drawLine(cameraX, (i * 32) + cameraY, cameraX + (currentRoom.getRoomWidth() * 32),
                        (i * 32) + cameraY);
            }
            g2.setStroke(new BasicStroke(2));
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {

            case 38:
                //UP
                deltaY = -1;
                if (!movement.isRunning()) movement.start();
                break;

            case 40:
                //DOWN
                deltaY = 1;
                if (!movement.isRunning()) movement.start();
                break;

            case 37:
                //LEFT
                deltaX = -1;
                if (!movement.isRunning()) movement.start();
                break;

            case 39:
                //RIGHT
                deltaX = 1;
                if (!movement.isRunning()) movement.start();
                break;

            default:
                Log.logLine(e.getKeyCode());
        }
        repaint();
        Log.logLine("Repainted");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {

            case 38:
            case 40:
                deltaY = 0;
                if (deltaX == 0) movement.stop();
                break;

            case 37:
            case 39:
                deltaX = 0;
                if (deltaY == 0) movement.stop();
                break;

            default:
                Log.logLine(e.getKeyCode());
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String[] command = e.getActionCommand().split(":");
        String condition = e.getActionCommand().split(":")[0];
        repaint();
        double xPos = MouseInfo.getPointerInfo().getLocation().getX() - getLocationOnScreen().getX() - cameraX;
        double yPos = MouseInfo.getPointerInfo().getLocation().getY() - getLocationOnScreen().getY() - cameraY;
        xPos /= 32;
        yPos /= 32;
        switch (condition) {
            case "move":
                cameraX += deltaX;
                cameraY += deltaY;
                repaint();
                break;
            case "r":
                currentRoom = roomPanel.getRoom(Integer.parseInt(command[1]));
                break;
            case "click":
                if (Objects.equals(placementTypes.getSelectedItem(), "Walls")) currentRoom.getWallMap()[(int) xPos][(int) yPos] = change;
                else currentRoom.getTerrainMap()[(int) xPos][(int) yPos] = currentTile;
                break;
            case "save":
                saveMapData();
                break;
            case "load":
                loadMapData();
                break;
            default:
                Log.logLine("Action Unrecognized");
                Log.logLine(e.getActionCommand());
                break;
        }
    }

    public boolean isValid(Room room) {
        return room.getRoomWidth() != 0 && room.getRoomHeight() != 0;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        double xPos = MouseInfo.getPointerInfo().getLocation().getX() - getLocationOnScreen().getX();
        double yPos = MouseInfo.getPointerInfo().getLocation().getY() - getLocationOnScreen().getY();
        xPos -= cameraX;
        yPos -= cameraY;
        xPos /= 32;
        yPos /= 32;
        if (Objects.equals(placementTypes.getSelectedItem(), "Walls")) {
            click.start();
            change = !currentRoom.getWallMap()[(int) xPos][(int) yPos];
        } else {
            click.start();
            currentTile = tilePanel.getTile();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        click.stop();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
