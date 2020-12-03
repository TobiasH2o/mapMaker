import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MainPanel extends JPanel implements KeyListener, ActionListener {

    Rooms roomPanel = new Rooms();

    Timer movement = new Timer(10, this);

    Room currentRoom = new Room(0, "");
    int cameraX = 0;
    int deltaX = 0;
    int cameraY = 0;
    int deltaY = 0;
    Tiles tilePanel = new Tiles();

    public MainPanel(JFrame parent) {
        roomPanel.setLocation(parent.getLocationOnScreen().x - roomPanel.frame.getWidth() - 2,
                parent.getLocationOnScreen().y);
        roomPanel.setFocusable(false);
        roomPanel.setVisible(true);

        tilePanel.setLocation(parent.getLocationOnScreen().x + parent.getWidth() + 2,
                parent.getLocationOnScreen().y);
        tilePanel.setFocusable(false);
        tilePanel.setVisible(true);

        movement.setActionCommand("move");

        parent.setFocusable(true);
        parent.addKeyListener(this);

    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
        for (int i = 0;i < currentRoom.getRoomWidth();i += 64) {
            g2.drawLine(i+cameraX, cameraY, i+cameraX, cameraY + (currentRoom.getRoomHeight()*64));
        }
        for (int i = 0;i < currentRoom.getRoomHeight();i += 64) {
            g2.drawLine(cameraX, i+cameraY, cameraX + (currentRoom.getRoomWidth()*64), i+cameraX);
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){

            case 38:
                //UP
                deltaY = -1;
                if(!movement.isRunning())
                    movement.start();
                break;

            case 40:
                //DOWN
                deltaY = 1;
                if(!movement.isRunning())
                    movement.start();
                break;

            case 37:
                //LEFT
                deltaX = -1;
                if(!movement.isRunning())
                    movement.start();
                break;

            case 39:
                //RIGHT
                deltaX = 1;
                if(!movement.isRunning())
                    movement.start();
                break;

            default:
                Log.logLine(e.getKeyCode());
        }
        repaint();
        Log.logLine("Repainted");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()){

            case 38:
            case 40:
                deltaY = 0;
                if(deltaX == 0)
                    movement.stop();
                break;

            case 37:
            case 39:
                deltaX = 0;
                if(deltaY == 0)
                    movement.stop();
                break;

            default:
                Log.logLine(e.getKeyCode());
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if(command.equalsIgnoreCase("move")){
            cameraX += deltaX;
            cameraY += deltaY;
            repaint();
        }
    }

    public boolean isValid(Room room){
        return room.getRoomWidth() != 0 && room.getRoomHeight() != 0;
    }
}
