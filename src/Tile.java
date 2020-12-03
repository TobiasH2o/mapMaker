import java.awt.image.BufferedImage;

public class Tile{

    private String name;
    private BufferedImage Image;

    public Tile(String name, BufferedImage Image){
        this.name = name;
        this.Image = Image;
    }

    public String getName(){
        return name;
    }

    public BufferedImage getImage(){
        return Image;
    }

}
