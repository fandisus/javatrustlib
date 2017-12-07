package trust.image;

import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;

public class Resizer {
  public static Image getResizedResource(String path, int sizex, int sizey) {
    return new ImageIcon(Resizer.class.getResource(path))
            .getImage()
            .getScaledInstance(sizex, sizey, Image.SCALE_DEFAULT);
  }
  public static Image getResized(String path, int sizex, int sizey) {
//    if (!new File(path).exists()) throw new Exception("File not found");
    return new ImageIcon(path).getImage()
            .getScaledInstance(sizex, sizey, Image.SCALE_DEFAULT);
  }
}
