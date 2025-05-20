package src.view;

import javax.swing.*;
import java.awt.*;

public class SectorSelectionPanel extends JPanel {
    private JFrame parentFrame;
    private static boolean[] completed = new boolean[8]; // Track completion status of each question
    private JButton[] sectorButtons = new JButton[8];
    private JProgressBar progressBar;

    public SectorSelectionPanel(JFrame frame, boolean[] completedStatus) {
        this.parentFrame = frame;
        if (completedStatus != null) {
            System.arraycopy(completedStatus, 0, completed, 0, 8);
        }
        setLayout(new BorderLayout(10, 10));
        StyleUtil.stylePanel(this);
        // Title
        JLabel title = new JLabel("Sector Area Challenge", SwingConstants.CENTER);
        StyleUtil.styleLabel(title, StyleUtil.BIG_FONT, StyleUtil.MAIN_PURPLE);
        title.setOpaque(true);
        title.setBackground(StyleUtil.MAIN_PINK);
        title.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        add(title, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(2, 4, 32, 32));
        StyleUtil.stylePanel(gridPanel);
        for (int i = 0; i < 8; i++) {
            String imgPath = "src/resources/images/task6/Shape " + (i + 1) + ".jpg";
            ImageIcon icon = new ImageIcon(imgPath);
            Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            JButton btn = new JButton("Sector " + (i + 1), new ImageIcon(img));
            StyleUtil.styleButton(btn, StyleUtil.MAIN_YELLOW, Color.BLACK);
            btn.setFont(StyleUtil.BIG_FONT);
            btn.setPreferredSize(new Dimension(160, 180));
            btn.setHorizontalTextPosition(SwingConstants.CENTER);
            btn.setVerticalTextPosition(SwingConstants.BOTTOM);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(StyleUtil.MAIN_PURPLE, 3, true));
            if (completed[i]) {
                btn.setBackground(new Color(180, 255, 180));
                btn.setForeground(StyleUtil.MAIN_PURPLE);
                btn.setText(btn.getText() + "  ✓");
                btn.setEnabled(false);
            } else {
                int idx = i;
                btn.addActionListener(e -> {
                    parentFrame.setContentPane(new SectorAreaPanel(parentFrame, idx, completed));
                    parentFrame.revalidate();
                });
            }
            sectorButtons[i] = btn;
            gridPanel.add(btn);
        }
        gridPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
        add(gridPanel, BorderLayout.CENTER);

        // Style progress bar
        int done = 0;
        for (int i = 0; i < 8; i++) if (completed[i]) done++;
        progressBar = new JProgressBar(0, 8);
        progressBar.setValue(done);
        progressBar.setStringPainted(true);
        progressBar.setString("Completed: " + done + "/8");
        progressBar.setFont(StyleUtil.BIG_FONT);
        progressBar.setForeground(StyleUtil.MAIN_GREEN);
        progressBar.setBackground(new Color(230,230,255));
        progressBar.setBorder(BorderFactory.createLineBorder(StyleUtil.MAIN_PURPLE, 2, true));
        JPanel progressPanel = new JPanel(new BorderLayout());
        StyleUtil.stylePanel(progressPanel);
        progressPanel.setBorder(BorderFactory.createEmptyBorder(8, 80, 8, 80));
        progressPanel.add(progressBar, BorderLayout.CENTER);
        add(progressPanel, BorderLayout.SOUTH);

        // Style Home button
        JButton homeBtn = new JButton("Home");
        StyleUtil.styleButton(homeBtn, StyleUtil.MAIN_YELLOW, Color.BLACK);
        homeBtn.setFont(StyleUtil.NORMAL_FONT);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 18));
        StyleUtil.stylePanel(bottomPanel);
        bottomPanel.add(homeBtn);
        add(bottomPanel, BorderLayout.AFTER_LAST_LINE);
        homeBtn.addActionListener(e -> {
            parentFrame.setContentPane(new MainMenuPanel(parentFrame));
            parentFrame.revalidate();
        });
    }

    public static boolean isAllCompleted() {
        for (boolean b : completed) if (!b) return false;
        return true;
    }
}
