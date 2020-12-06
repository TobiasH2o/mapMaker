import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Rooms extends JPanel implements ActionListener, DocumentListener {

    JFrame frame = new JFrame();

    int cRoomID = 0;

    JButton addRoom = new JButton("New Room");

    ActionListener listener;

    Container roomsList = new Container();

    HintTextField searchTextField = new HintTextField("Search Rooms:", HintTextField.CENTER_HIDDEN);

    JScrollPane roomScroll = new JScrollPane(roomsList);

    ArrayList<Room> rooms = new ArrayList<>();

    public Rooms(ActionListener listener) {
        this.listener = listener;
        frame.setSize(250, 400);
        frame.setTitle("Rooms");
        frame.add(this);
        setLayout(new BorderLayout());
        add(roomScroll, BorderLayout.CENTER);
        add(addRoom, BorderLayout.SOUTH);
        add(searchTextField, BorderLayout.NORTH);

        addRoom.setActionCommand("NewRoom");
        addRoom.addActionListener(this);

        roomsList.setLayout(new BoxLayout(roomsList, BoxLayout.Y_AXIS));

        searchTextField.getDocument().addDocumentListener(this);
    }

    public void updateList() {
        roomsList.removeAll();
        roomsList.setLayout(new GridLayout(rooms.size() + 10, 1));
        JButton b;
        for (Room r : rooms) {
            b = new JButton(r.getName());
            b.setActionCommand("r:" + r.getID());
            b.addActionListener(this);
            b.addActionListener(listener);
            if (r.getName().startsWith(searchTextField.getText())) roomsList.add(b);
        }
        roomScroll.revalidate();
        repaint();
    }

    public Room getRoom(int roomID) {
        for (Room r : rooms)
            if (r.getID() == roomID) return r;
        return null;
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
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if ("NewRoom".equals(command.split(":")[0])) {
            Room temp = new Room(cRoomID, JOptionPane.showInputDialog("Room name"));
            String[] roomDetails = JOptionPaneMultiInput.showInput(null, "Width:", "Height:", "Room Size");
            if (roomDetails.length == 2) try {
                temp.setRoomSize(Integer.parseInt(roomDetails[0]), Integer.parseInt(roomDetails[1]));
                cRoomID++;
                rooms.add(temp);
            } catch (Exception ignored) {}
            updateList();
        } else {
            Log.logLine("Unknown command " + command);
            Log.logLine("Source " + e.getSource());
        }


    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        updateList();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateList();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }
}
