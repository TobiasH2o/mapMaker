import javax.swing.*;
import java.awt.*;

public class JOptionPaneMultiInput {
    public static String[] showInput(Component parentComponent, String optionOne, String optionTwo, String title) {
        JTextField xField = new JTextField(5);
        JTextField yField = new JTextField(5);

        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel(optionOne));
        myPanel.add(xField);
        myPanel.add(Box.createHorizontalStrut(15));
        myPanel.add(new JLabel(optionTwo));
        myPanel.add(yField);

        int result = JOptionPane.showConfirmDialog(parentComponent, myPanel, title, JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            return new String[]{xField.getText(), yField.getText()};
        }
        return new String[]{""};
    }
}