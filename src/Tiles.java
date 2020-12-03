import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

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
            g2.drawImage(tiles[i].getImage(), (i % (this.getWidth() / 64)) * 64, (i / (this.getWidth() / 64)) * 64,
                    null);
        }
        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
        g2.drawRect(((selected % (this.getWidth() / 64)) * 64) + 4, ((selected / (this.getHeight() / 64)) * 64) + 4, 56,
                56);

        drawGrid(g2);
    }

    public void drawGrid(Graphics2D g2) {
        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
        for (int i = 0;i < this.getWidth();i += 64) {
            g2.drawLine(i, 0, i, this.getHeight());
        }
        for (int i = 0;i < this.getHeight();i += 64) {
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

    @Override
    public void mouseClicked(MouseEvent e) {
        double x = e.getX();
        double y = e.getY();
        selected = (int)(x / 64) + ((int)(y / 64) * (this.getWidth()/64));
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
