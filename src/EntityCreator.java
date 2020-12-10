import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;

public class EntityCreator extends JPanel implements ActionListener {

    BufferedImage entityImage;
    JButton changeImageButton = new JButton("Change Icon");
    JCheckBox doorCheck = new JCheckBox("Door");
    JCheckBox chestCheck = new JCheckBox("Chest");
    HintTextField nameField = new HintTextField("EntityBase Name", HintTextField.RIGHT_LEADING);

    {
        try {
            entityImage = ImageIO.read(getClass().getResourceAsStream("Images/questionMark.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public EntityCreator() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createVerticalStrut(68));
        add(changeImageButton);
        add(Box.createVerticalStrut(2));
        add(doorCheck);
        add(Box.createVerticalStrut(2));
        add(chestCheck);
        add(Box.createVerticalStrut(2));
        add(nameField);

        changeImageButton.addActionListener(this);
        changeImageButton.setActionCommand("changeImage");
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
        AffineTransform at = new AffineTransform();
        at.scale(2, 2);
        at.translate(getWidth()/4.0 - 16, 2);
        g2.drawImage(entityImage, at, null);
    }

    public void cleanSetup() {
        try {
            entityImage = ImageIO.read(getClass().getResourceAsStream("Images/questionMark.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        doorCheck.setSelected(false);
        chestCheck.setSelected(false);
        nameField.setText("");
    }

    public void changeImage(){
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(
                "Image files", ImageIO.getReaderFileSuffixes());
        fileChooser.setFileFilter(imageFilter);
        fileChooser.showOpenDialog(getParent());
        try {
            entityImage = ImageIO.read(fileChooser.getSelectedFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if(command.equals("changeImage"))
            changeImage();
    }
}
