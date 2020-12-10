import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Tiles extends JPanel implements MouseListener {

    Tile[] tiles;
    int selected = 0;

    JFrame frame = new JFrame();

    public Tiles() {
        frame.setSize(340, 400);
        frame.setTitle("Tiles");

        tiles = new Tile[FileManager.countTiles()];
        FileManager.loadTiles(tiles);
        frame.add(this);
        this.addMouseListener(this);
        this.setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
        for (int i = 0;i < tiles.length;i++) {
            g2.drawImage(tiles[i].getImage(), (i % (this.getWidth() / 32)) * 32, (i / (this.getWidth() / 32)) * 32,
                    null);
        }
        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
        g2.drawRect(((selected % (this.getWidth() / 32)) * 32) + 2, ((selected / (this.getHeight() / 32)) * 32) + 2, 28,
                28);

        drawGrid(g2);
    }

    public Tile getTile(){
        return tiles[selected];
    }

    public void drawGrid(Graphics2D g2) {
        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
        for (int i = 0;i < this.getWidth();i += 32) {
            g2.drawLine(i, 0, i, this.getHeight());
        }
        for (int i = 0;i < this.getHeight();i += 32) {
            g2.drawLine(0, i, this.getWidth(), i);
        }
    }

    @Override
    public void setLocation(int x, int y) {
        frame.setLocation(x, y);
    }

    @Override
    public void setVisible(boolean x) {
        frame.setVisible(x);
    }

    public boolean tileExists(String check){
        for (Tile tile : tiles) {
            if (tile.getName().equals(check)) return true;
        }
        return false;
    }

    public Tile getTile(String tileName){
        for (Tile tile : tiles) {
            if (tile.getName().equals(tileName)) return tile;
        }
        return null;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        double x = e.getX();
        double y = e.getY();
        selected = (int)(x / 32) + ((int)(y / 32) * (this.getWidth()/32));
        if (selected > tiles.length) selected = 0;
        frame.setTitle(tiles[selected].getName());
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
