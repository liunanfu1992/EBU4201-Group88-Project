package src.model;

import javax.swing.*;
import java.awt.*;
import java.io.File;

// Utility class for loading and scaling images.
// This class provides methods to load images from a specified path and scale them to desired dimensions.
public class ImageLoader {
    private static final String BASE_PATH = System.getProperty("user.dir") + "/resources/images/";
    
    // Loads an image from the specified relative path.
    public static ImageIcon loadImage(String relativePath) {
        String fullPath = BASE_PATH + relativePath;
        File file = new File(fullPath);
        if (!file.exists()) {
            System.err.println("Image not found: " + fullPath);
            return null;
        }
        return new ImageIcon(fullPath);
    }
    
    // Loads and scales an image to the specified dimensions.
    public static ImageIcon loadAndScaleImage(String relativePath, int width, int height) {
        ImageIcon icon = loadImage(relativePath);
        if (icon != null) {
            Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(image);
        }
        return null;
    }
} 