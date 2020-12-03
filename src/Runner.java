import javax.swing.*;

public class Runner {

    public static void main(String[] args) {

        JFrame frame = new JFrame();

        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(3);
        frame.setTitle("Map Maker");
        frame.setVisible(true);

        FileManager.validate(frame);

        MainPanel main = new MainPanel(frame);

        frame.add(main);
        frame.revalidate();

    }

}
