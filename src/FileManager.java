import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class FileManager {

    static String docs = null;
    static File main = null;
    static File tiles = null;
    static String break1 = "%55";
    static String break2 = "%10";

    public static boolean validate(JFrame parent) {
        try {
            Process p = Runtime.getRuntime()
                    .exec("reg query \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders\" /v personal");
            p.waitFor();

            InputStream in = p.getInputStream();
            byte[] b = new byte[in.available()];
            if (in.read(b) == 0) docs = JOptionPane
                    .showInputDialog(parent, "A fatal error has occurred. \n Please provide your documents folder.");
            else {

                docs = new String(b);
                docs = docs.split("\\s\\s+")[4];
                docs += "\\MapMakerData\\";
            }
            in.close();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        main = new File(docs);
        if (!checkFolder(main)) return false;

        tiles = new File(docs + "Tiles\\");
        if (!checkFolder(tiles)) return false;

        return true;
    }

    public static boolean checkFile(File file) {
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) return false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static String[] loadMapData(File file) {
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            ArrayList<String> arr = new ArrayList<>();
            String line = br.readLine();
            while (line != null) {
                arr.add(line);
                line = br.readLine();
            }
            br.close();
            return arr.toArray(String[]::new);

        } catch (IOException e) {
            e.printStackTrace();
            return new String[]{};
        }
    }

    public static boolean checkFolder(File file) {
        if (!file.exists()) {
            return file.mkdir();
        }
        return true;
    }

    public static int countTiles() {
        return (Objects.requireNonNull(tiles.listFiles()).length);
    }

    public static void loadTiles(Tile[] arr) {

        try {
            int count = 0;
            for (File f : Objects.requireNonNull(tiles.listFiles())) {
                arr[count] = new Tile(f.getName(), ImageIO.read(f));
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void saveRoom(File file, Room room) {
        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(room.getID() + break1 + room.getName() + break1 + room.getRoomWidth() + break1 +
                     room.getRoomHeight());
            bw.newLine();
            for (int i = 0;i < room.getRoomHeight();i++) {
                for (int k = 0;k < room.getRoomWidth();k++) {
                    Log.logLine(room.getWallMap()[k][i]);
                    bw.write(room.getWallMap()[k][i] + break1);
                }
            }
            bw.newLine();
            for (int i = 0;i < room.getRoomHeight();i++) {
                for (int k = 0;k < room.getRoomWidth();k++) {
                    if(!room.getTerrainMap()[k][i].getName().isEmpty())
                        bw.write(room.getTerrainMap()[k][i].getName() + break1);
                    else
                        bw.write("NULL" + break1);

                }
            }

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
