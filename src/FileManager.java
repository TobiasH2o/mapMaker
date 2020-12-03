import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FileManager {

    static String docs = null;
    static File main = null;
    static File tiles = null;

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
        if (!main.exists()) {
            Log.logLine("Creating parent " + main);
            if (!main.mkdirs()) {
                Log.logLine("Created main");
            } else {
                return false;
            }
        }
        tiles = new File(docs + "Tiles\\");
        if (!tiles.exists()) {
            Log.logLine("Creating Tiles " + tiles);
            if (!tiles.mkdir()) {
                Log.logLine("Created Tiles");
            } else {
                return false;
            }
        }
        return true;
    }

    public static int countTiles() {
        return (tiles.listFiles().length);
    }

    public static void loadTiles(Tile[] arr) {

        try {
            int count = 0;
            for (File f : tiles.listFiles()) {
                arr[count] = new Tile(f.getName(), ImageIO.read(f));
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
