import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainPanel extends JPanel implements KeyListener, ActionListener, MouseListener {

    JFrame parent;
    Rooms roomPanel = new Rooms(this);
    Timer movement = new Timer(10, this);
    Timer click = new Timer(10, this);
    Room currentRoom = new Room(0, "");
    Tile currentTile = new Tile("", null);
    int cameraX = 0;
    int deltaX = 0;
    int cameraY = 0;
    int deltaY = 0;
    Tiles tilePanel = new Tiles();
    JCheckBox wallsCheckBox = new JCheckBox("Wall map");
    boolean change;

    public MainPanel(JFrame parent) {
        this.parent = parent;
        roomPanel.setLocation(parent.getLocationOnScreen().x - roomPanel.frame.getWidth() - 2,
                parent.getLocationOnScreen().y);
        roomPanel.setFocusable(false);
        roomPanel.setVisible(true);

        tilePanel.setLocation(parent.getLocationOnScreen().x + parent.getWidth() + 2, parent.getLocationOnScreen().y);
        tilePanel.setFocusable(false);
        tilePanel.setVisible(true);

        movement.setActionCommand("move");
        click.setActionCommand("click");

        setLayout(new BorderLayout());

        parent.setFocusable(true);
        parent.addKeyListener(this);
        parent.addMouseListener(this);

        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);

        wallsCheckBox.setFocusable(false);
        wallsCheckBox.addActionListener(this);

        add(wallsCheckBox, BorderLayout.SOUTH);

    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
        for (int i = 0;i < currentRoom.getRoomWidth() + 1;i++) {
            g2.drawLine((i * 32) + cameraX, cameraY, (i * 32) + cameraX, cameraY + (currentRoom.getRoomHeight() * 32));
        }
        for (int i = 0;i < currentRoom.getRoomHeight() + 1;i++) {
            g2.drawLine(cameraX, (i * 32) + cameraY, cameraX + (currentRoom.getRoomWidth() * 32), (i * 32) + cameraY);
        }
        if (isValid(currentRoom)) {
            Tile[][] tileMap = currentRoom.getTerrainMap();
            boolean[][] wallMap = currentRoom.getWallMap();
            for (int i = 0;i < currentRoom.getRoomWidth();i++) {
                for (int k = 0;k < currentRoom.getRoomHeight();k++) {
                    if (tileMap[i][k] != null)
                        g2.drawImage(tileMap[i][k].getImage(), (i * 32) + cameraX, (k * 32) + cameraY, null);
                    if (wallsCheckBox.isSelected()) {
                        if (wallMap[i][k]) g2.setColor(new Color(134, 255, 128, 190));
                        else g2.setColor(new Color(255, 128, 128, 190));
                        g2.fillRect((i * 32) + cameraX, (k * 32) + cameraY, 32, 32);
                    }
                }
            }

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
                if (wallsCheckBox.isSelected()) currentRoom.getWallMap()[(int) xPos][(int) yPos] = change;
                else currentRoom.getTerrainMap()[(int) xPos][(int) yPos] = currentTile;
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
        if (wallsCheckBox.isSelected()) {
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
