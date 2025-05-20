package src.view;

import javax.swing.*;
import java.awt.*;


public class CompoundShapeSelectionPanel extends JPanel {
    private JFrame parentFrame;
    private static boolean[] completed = new boolean[6]; // 只保留6题
    private JButton[] shapeButtons = new JButton[6];
    private JProgressBar progressBar;

    public CompoundShapeSelectionPanel(JFrame frame, boolean[] completedStatus) {
        this.parentFrame = frame;
        if (completedStatus != null) {
            System.arraycopy(completedStatus, 0, completed, 0, 6);
        }
        setLayout(new BorderLayout(10, 10));
        StyleUtil.stylePanel(this);
        JLabel title = new JLabel("Compound Shapes Area Challenge", SwingConstants.CENTER);
        StyleUtil.styleLabel(title, StyleUtil.BIG_FONT, StyleUtil.MAIN_PURPLE);
        title.setOpaque(true);
        title.setBackground(StyleUtil.MAIN_PINK);
        title.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        add(title, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 32, 32));
        StyleUtil.stylePanel(gridPanel);
        int[] validIdx = {1, 2, 3, 4, 7, 8};
        for (int i = 0; i < 6; i++) {
            int idx = validIdx[i];
            String imgPath = "src/resources/images/task5/Shape " + (idx + 1) + ".jpg";
            ImageIcon icon = new ImageIcon(imgPath);
            Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            JButton btn = new JButton("Shape " + (idx + 1), new ImageIcon(img));
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
                int panelIdx = i;
                btn.addActionListener(e -> {
                    parentFrame.setContentPane(new CompoundShapePanel(parentFrame, panelIdx, completed));
                    parentFrame.revalidate();
                });
            }
            shapeButtons[i] = btn;
            gridPanel.add(btn);
        }
        gridPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
        add(gridPanel, BorderLayout.CENTER);

        // 美化进度条
        int done = 0;
        for (int i = 0; i < 6; i++) if (completed[i]) done++;
        progressBar = new JProgressBar(0, 6);
        progressBar.setValue(done);
        progressBar.setStringPainted(true);
        progressBar.setString("Completed: " + done + "/6");
        progressBar.setFont(StyleUtil.BIG_FONT);
        progressBar.setForeground(StyleUtil.MAIN_GREEN);
        progressBar.setBackground(new Color(230,230,255));
        progressBar.setBorder(BorderFactory.createLineBorder(StyleUtil.MAIN_PURPLE, 2, true));
        JPanel progressPanel = new JPanel(new BorderLayout());
        StyleUtil.stylePanel(progressPanel);
        progressPanel.setBorder(BorderFactory.createEmptyBorder(8, 80, 8, 80));
        progressPanel.add(progressBar, BorderLayout.CENTER);
        add(progressPanel, BorderLayout.SOUTH);

        // Home按钮美化
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
