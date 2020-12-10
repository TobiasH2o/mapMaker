import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Entities extends JPanel implements ActionListener, MouseListener, DocumentListener {

    EntityCreator entityCreator = new EntityCreator();
    HintTextField searchBar = new HintTextField("Search Entities", HintTextField.CENTER_HIDDEN);
    JFrame frame = new JFrame();

    ArrayList<EntityBase> entityBase = new ArrayList<>(0);
    ArrayList<EntityBase> displayEntityBase = new ArrayList<>(0);
    int selected = 0;
    JButton makeEntityButton = new JButton("New EntityBase");

    public Entities() {
        frame.setSize(250, 400);
        frame.setTitle("Entities");
        frame.add(this);
        setLayout(new BorderLayout());
        add(makeEntityButton, BorderLayout.SOUTH);
        makeEntityButton.addActionListener(this);
        makeEntityButton.setActionCommand("makeEntity");

        this.addMouseListener(this);
        this.setFocusable(true);
        add(searchBar, BorderLayout.NORTH);
        searchBar.getDocument().addDocumentListener(this);
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
        for (int i = 0;i < displayEntityBase.size();i++) {
            g2.drawImage(displayEntityBase.get(i).getImage(),
                    ((i % (this.getWidth() / 32)) * 32), (i / (this.getWidth() / 32) * 32) + searchBar.getHeight(),
                    null);
        }
        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
        g2.drawRect(((selected % (this.getWidth() / 32)) * 32) + 2,
                ((selected / (this.getWidth() / 32)) * 32) + 2 + searchBar.getHeight(), 28, 28);

        drawGrid(g2);

    }

    public void drawGrid(Graphics2D g2) {
        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
        for (int i = 0;i < this.getWidth();i += 32) {
            g2.drawLine(i, 0, i, this.getHeight());
        }
        for (int i = 0;i < this.getHeight();i += 32) {
            g2.drawLine(0, i + searchBar.getHeight(), this.getWidth(), i + searchBar.getHeight());
        }
    }

    public void updateDisplayList() {
        displayEntityBase.clear();
        for (EntityBase e : entityBase)
            if (e.getName().startsWith(searchBar.getText())) displayEntityBase.add(e);
    }

    @Override
    public void setLocation(int x, int y) {
        frame.setLocation(x, y);
    }

    @Override
    public void setFocusable(boolean x) {
        frame.setFocusable(x);
    }

    @Override
    public void setVisible(boolean x) {
        frame.setVisible(x);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("makeEntity")) {
            entityCreator.cleanSetup();
            JOptionPane.showConfirmDialog(null, entityCreator, "EntityBase Creator", JOptionPane.OK_CANCEL_OPTION);
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        double x = e.getX();
        double y = e.getY() - searchBar.getHeight();
        selected = (int) (x / 32) + ((int) (y / 32) * (this.getWidth() / 32));
        if (selected > displayEntityBase.size()) selected = 0;
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

    @Override
    public void insertUpdate(DocumentEvent e) {
        updateDisplayList();
        repaint();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateDisplayList();
        repaint();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }
}
