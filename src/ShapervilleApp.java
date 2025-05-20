package src;

import src.view.MainMenuPanel;
import javax.swing.*;

public class ShapervilleApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Shaperville - Learn Geometry!");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(960, 720);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);

            MainMenuPanel mainMenu = new MainMenuPanel(frame);
            frame.setContentPane(mainMenu);
            frame.setVisible(true);
        });
    }
} 