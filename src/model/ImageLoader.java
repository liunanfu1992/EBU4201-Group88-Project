package src.model;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ImageLoader {
    private static final String BASE_PATH = System.getProperty("user.dir") + "/resources/images/";
    
    public static ImageIcon loadImage(String relativePath) {
        String fullPath = BASE_PATH + relativePath;
        File file = new File(fullPath);
        if (!file.exists()) {
            System.err.println("Image not found: " + fullPath);
            return null;
        }
        return new ImageIcon(fullPath);
    }
    
    public static ImageIcon loadAndScaleImage(String relativePath, int width, int height) {
        ImageIcon icon = loadImage(relativePath);
        if (icon != null) {
            Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(image);
        }
        return null;
    }
} 