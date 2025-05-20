package src.view;
import java.awt.*;
import javax.swing.*;

public class StyleUtil {
    public static final Color MAIN_BLUE = new Color(59, 130, 246);
    public static final Color MAIN_YELLOW = new Color(255, 221, 51);
    public static final Color MAIN_GREEN = new Color(34, 197, 94);
    public static final Color MAIN_PINK = new Color(255, 182, 193);
    public static final Color MAIN_PURPLE = new Color(167, 139, 250);
    public static final Color BG_COLOR = new Color(255, 250, 240); // Soft beige
    public static final Font BIG_FONT = new Font("Comic Sans MS", Font.BOLD, 24);
    public static final Font NORMAL_FONT = new Font("Comic Sans MS", Font.PLAIN, 18);
    public static final Font TITLE_FONT = new Font("Comic Sans MS", Font.BOLD, 32);

    public static void styleButton(JButton btn, Color bg, Color fg) {
        btn.setFont(BIG_FONT);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3, true));
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void styleLabel(JLabel label, Font font, Color fg) {
        label.setFont(font);
        label.setForeground(fg);
    }

    public static void stylePanel(JPanel panel) {
        panel.setBackground(BG_COLOR);
    }
} 