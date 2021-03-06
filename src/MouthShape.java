import javax.swing.*;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class MouthShape {
    private String [] spelling;
    private String fileName;
    private ImageIcon imageIcon;
    private Image image;

    // Constructor
    public MouthShape(String fileName, String[] spelling){
        this.fileName = fileName;
        this.spelling = spelling;
        this.imageIcon = new ImageIcon(fileName);
        this.image = this.imageIcon.getImage();
    }

    public Image getImage() {
        return image;
    }

    public ImageIcon getImageIcon() {
        return imageIcon;
    }

    public String[] getSpelling() {
        return spelling;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
